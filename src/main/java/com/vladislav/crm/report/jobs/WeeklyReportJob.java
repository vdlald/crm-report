package com.vladislav.crm.report.jobs;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.proto.leads.LeadServiceGrpc.LeadServiceStub;
import com.proto.leads.ReadLeadInfoForReportRequest;
import com.proto.leads.ReadLeadInfoForReportResponse;
import com.proto.users.GetUserRequest;
import com.proto.users.UserServiceGrpc.UserServiceStub;
import com.vladislav.crm.report.clients.EmailClient;
import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.documents.NewLeadLog;
import com.vladislav.crm.report.grpc.DefaultStreamObserver;
import com.vladislav.crm.report.operations.GetAllMoveLeadLogOperation;
import com.vladislav.crm.report.operations.GetAllNewLeadLogOperation;
import com.vladislav.crm.report.pojo.WeeklyReport;
import com.vladislav.crm.report.pojo.WeeklyReport.NewLeadReport;
import com.vladislav.crm.report.utils.LocalizedWeek;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeeklyReportJob implements Runnable {

    private final GetAllMoveLeadLogOperation getAllMoveLeadLogOperation;
    private final GetAllNewLeadLogOperation getAllNewLeadLogOperation;
    private final UserServiceStub userService;
    private final LeadServiceStub leadService;
    private final EmailClient emailClient;

    @Override
    @Scheduled(cron = "0 0 1 * * MON")
    public void run() {
        final LocalizedWeek localizedWeek = new LocalizedWeek();

        final Collection<MoveLeadLog> moveLeadLogs = getAllMoveLeadLogOperation.execute(
                localizedWeek.getFirstWeekDateTime(), localizedWeek.getFirstWeekDateTime()
        );

        final Collection<NewLeadLog> newLeadLogs = getAllNewLeadLogOperation.execute(
                localizedWeek.getFirstWeekDateTime(), localizedWeek.getFirstWeekDateTime()
        );

        final Map<Pair<Long, Long>, List<MoveLeadLog>> indexToLeads = mapMoveLeadLogs(moveLeadLogs);

        final HashMap<Long, WeeklyReport> weeklyReports = indexToLeads.entrySet()
                .parallelStream()
                .map(this::makeLeadMoveReport)
                .collect(
                        (Supplier<HashMap<Long, WeeklyReport>>) HashMap::new,
                        (map, pairReport) -> {
                            final Long userId = pairReport.getFirst();
                            val leadMoveReport = pairReport.getSecond();
                            final WeeklyReport weeklyReport = map.get(userId);
                            if (weeklyReport == null) {
                                final WeeklyReport newWeeklyReport = new WeeklyReport();
                                newWeeklyReport.setUserId(userId);
                                newWeeklyReport.getLeadMoveReports().add(leadMoveReport);
                                map.put(userId, newWeeklyReport);
                            } else {
                                weeklyReport.getLeadMoveReports().add(leadMoveReport);
                            }
                        },
                        Map::putAll
                );

        newLeadLogs.forEach(newLeadLog -> {
            final Long userId = newLeadLog.getUserId();

            final WeeklyReport weeklyReport = weeklyReports.get(userId);
            final NewLeadReport report = new NewLeadReport()
                    .setLeadId(newLeadLog.getLeadId());

            weeklyReport.getNewLeadReports().add(report);
        });

        weeklyReports.values()
                .parallelStream()
                .forEach(this::handle);
    }

    private Pair<Long, WeeklyReport.LeadMoveReport> makeLeadMoveReport(
            Map.Entry<Pair<Long, Long>, List<MoveLeadLog>> pair
    ) {
        final Pair<Long, Long> key = pair.getKey();
        final Long leadId = key.getSecond();
        final List<MoveLeadLog> moveLeadLogs = pair.getValue();
        final MoveLeadLog firstLog = moveLeadLogs.get(0);
        final MoveLeadLog lastLog = moveLeadLogs.get(moveLeadLogs.size() - 1);

        final WeeklyReport.LeadMoveReport leadMoveReport = new WeeklyReport.LeadMoveReport()
                .setLeadId(leadId)
                .setPrevStatus(firstLog.getPrevStatusId())
                .setNextStatus(lastLog.getNextStatusId());

        return Pair.of(key.getFirst(), leadMoveReport);
    }

    private void handle(WeeklyReport report) {
        final GetUserRequest request = GetUserRequest.newBuilder()
                .setUserId(report.getUserId())
                .build();

        userService.getUser(request, DefaultStreamObserver.onNext(response -> {
            final String email = response.getInfo().getEmail();
            emailClient.sendEmail(makePdf(report), email);
        }));
    }

    private byte[] makePdf(WeeklyReport weeklyReport) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PdfWriter pdfWriter = new PdfWriter(outputStream);
        final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        final Document document = new Document(pdfDocument);

        document.add(new Paragraph("Отчет за неделю"));

        document.add(new Paragraph("Движение сделок"));
        final CountDownLatch latch = new CountDownLatch(1);
        val responseObserver = DefaultStreamObserver.<ReadLeadInfoForReportResponse>newBuilder()
                .setOnNext(response -> {
                    final Paragraph paragraph = new Paragraph(
                            String.format(
                                    "%s перешла из %s в %s",
                                    response.getLeadName(),
                                    response.getPrevStatusName(),
                                    response.getNextStatusName()
                            ));
                    synchronized (document) {
                        document.add(paragraph);
                    }
                })
                .setOnCompleted(latch::countDown)
                .build();
        val requestStreamObserver = leadService.readLeadInfoForReport(responseObserver);
        for (WeeklyReport.LeadMoveReport leadMoveReport : weeklyReport.getLeadMoveReports()) {
            requestStreamObserver.onNext(ReadLeadInfoForReportRequest.newBuilder()
                    .setLeadId(leadMoveReport.getLeadId())
                    .setPrevStatusId(leadMoveReport.getPrevStatus())
                    .setNextStatusId(leadMoveReport.getNextStatus())
                    .build());
        }
        requestStreamObserver.onCompleted();

        try {
            latch.await(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        document.add(new Paragraph("Новые сделки"));
        for (NewLeadReport newLeadReport : weeklyReport.getNewLeadReports()) {
            document.add(new Paragraph(newLeadReport.getLeadId().toString()));
        }

        document.close();
        return outputStream.toByteArray();
    }

    private static Map<Pair<Long, Long>, List<MoveLeadLog>> mapMoveLeadLogs(Collection<MoveLeadLog> all) {
        final Map<Pair<Long, Long>, List<MoveLeadLog>> indexToLeads = new HashMap<>();
        all.forEach(log -> {
            final Pair<Long, Long> indexPair = Pair.of(log.getUserId(), log.getLeadId());
            final List<MoveLeadLog> userIds = indexToLeads.get(indexPair);
            if (userIds == null) {
                final ArrayList<MoveLeadLog> newList = new ArrayList<>();
                newList.add(log);
                indexToLeads.put(indexPair, newList);
            } else {
                userIds.add(log);
            }
        });
        return indexToLeads;
    }
}
