package com.vladislav.crm.report.jobs;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.proto.leads.LeadServiceGrpc.LeadServiceStub;
import com.proto.leads.ReadLeadInfoForReportRequest;
import com.proto.leads.ReadLeadInfoForReportResponse;
import com.proto.leads.ReadLeadInfoRequest;
import com.proto.leads.ReadLeadInfoResponse;
import com.proto.users.GetUserRequest;
import com.proto.users.UserServiceGrpc.UserServiceStub;
import com.vladislav.crm.report.clients.EmailClient;
import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.documents.NewLeadLog;
import com.vladislav.crm.report.grpc.DefaultStreamObserver;
import com.vladislav.crm.report.operations.GetAllMoveLeadLogOperation;
import com.vladislav.crm.report.operations.GetAllNewLeadLogOperation;
import com.vladislav.crm.report.pojo.WeeklyReport;
import com.vladislav.crm.report.pojo.WeeklyReport.LeadMoveReport;
import com.vladislav.crm.report.pojo.WeeklyReport.NewLeadReport;
import com.vladislav.crm.report.utils.LocalizedWeek;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.proto.leads.LeadServiceGrpc.LeadServiceBlockingStub;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeeklyReportJob implements Runnable {

    private final GetAllMoveLeadLogOperation getAllMoveLeadLogOperation;
    private final GetAllNewLeadLogOperation getAllNewLeadLogOperation;
    private final UserServiceStub userService;
    private final LeadServiceStub leadService;
    private final LeadServiceBlockingStub leadServiceBlocking;
    private final EmailClient emailClient;

    @Override
    @Scheduled(cron = "0 0 1 * * MON")
    public void run() {
        final LocalizedWeek localizedWeek = new LocalizedWeek();

        final LocalDateTime firstWeekDateTime = localizedWeek.getFirstWeekDateTime();
        final LocalDateTime lastWeekDateTime = localizedWeek.getLastWeekDateTime();

        final Collection<MoveLeadLog> moveLeadLogs = getAllMoveLeadLogOperation.execute(firstWeekDateTime, lastWeekDateTime);
        final Collection<NewLeadLog> newLeadLogs = getAllNewLeadLogOperation.execute(firstWeekDateTime, lastWeekDateTime);

        final Map<Pair<Long, Long>, List<MoveLeadLog>> indexToLeads = mapMoveLeadLogs(moveLeadLogs);

        final Map<Long, WeeklyReport> weeklyReports = indexToLeads.entrySet()
                .parallelStream()
                .map(this::makeLeadMoveReport)
                .collect(
                        Collectors.toMap(
                                Pair::getFirst,
                                t -> {
                                    final LeadMoveReport report = t.getSecond();
                                    final WeeklyReport weeklyReport = new WeeklyReport().setUserId(t.getFirst());
                                    weeklyReport.getLeadMoveReports().add(report);
                                    return weeklyReport;
                                },
                                (r1, r2) -> {
                                    r1.getLeadMoveReports().addAll(r2.getLeadMoveReports());
                                    return r1;
                                }
                        )
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

    private Pair<Long, LeadMoveReport> makeLeadMoveReport(
            Map.Entry<Pair<Long, Long>, List<MoveLeadLog>> pair
    ) {
        final Pair<Long, Long> key = pair.getKey();
        final Long leadId = key.getSecond();
        final List<MoveLeadLog> moveLeadLogs = pair.getValue();
        final MoveLeadLog firstLog = moveLeadLogs.get(0);
        final MoveLeadLog lastLog = moveLeadLogs.get(moveLeadLogs.size() - 1);

        final LeadMoveReport leadMoveReport = new LeadMoveReport()
                .setLeadId(leadId)
                .setPrevStatus(firstLog.getPrevStatusId())
                .setNextStatus(lastLog.getNextStatusId());

        return Pair.of(key.getFirst(), leadMoveReport);
    }

    private void handle(WeeklyReport report) {
        final GetUserRequest request = GetUserRequest.newBuilder()
                .setUserId(report.getUserId())
                .build();

        Context.current().fork().run(() -> userService.getUser(request, DefaultStreamObserver.onNext(response -> {
            final String email = response.getInfo().getEmail();
            final byte[] pdf = makePdf(report);
            emailClient.sendEmail(pdf, email);
        })));
    }

    private byte[] makePdf(WeeklyReport weeklyReport) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PdfWriter pdfWriter = new PdfWriter(outputStream);
        final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        final Document document = new Document(pdfDocument);

        document.add(new Paragraph("Weekly report").setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Leads movement").setFontSize(16));
        final CountDownLatch latch = new CountDownLatch(1);
        val responseObserver = DefaultStreamObserver.<ReadLeadInfoForReportResponse>newBuilder()
                .setOnNext(response -> {
                    final Paragraph paragraph = new Paragraph(
                            String.format(
                                    "\"%s\" move from \"%s\" to \"%s\"",
                                    response.getLeadName(),
                                    response.getPrevStatusName(),
                                    response.getNextStatusName()
                            )).setItalic();
                    synchronized (document) {
                        document.add(paragraph);
                    }
                })
                .setOnCompleted(latch::countDown)
                .build();
        val requestStreamObserver = leadService.readLeadInfoForReport(responseObserver);
        for (LeadMoveReport leadMoveReport : weeklyReport.getLeadMoveReports()) {
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

        document.add(new Paragraph("New leads").setFontSize(16));
        for (NewLeadReport newLeadReport : weeklyReport.getNewLeadReports()) {
            final ReadLeadInfoResponse response = leadServiceBlocking.readLeadInfo(ReadLeadInfoRequest.newBuilder()
                    .setLeadId(newLeadReport.getLeadId())
                    .build());

            document.add(new Paragraph(response.getName()).setItalic());
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
