package com.vladislav.crm.report.jobs;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.GetAllMoveLeadLogOperation;
import com.vladislav.crm.report.pojo.WeeklyReport;
import com.vladislav.crm.report.utils.LocalizedWeek;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeeklyReportJob implements Runnable {

    private final GetAllMoveLeadLogOperation getAllMoveLeadLogOperation;

    @Override
    @Scheduled(cron = "0 0 1 * * MON")
    public void run() {
        final LocalizedWeek localizedWeek = new LocalizedWeek();

        final Collection<MoveLeadLog> all = getAllMoveLeadLogOperation.execute(
                localizedWeek.getFirstWeekDateTime(), localizedWeek.getFirstWeekDateTime()
        );

        final Map<Pair<Long, Long>, List<MoveLeadLog>> indexToLeads = mapMoveLeadLogs(all);

        indexToLeads.entrySet()
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
                )
                .values()
                .parallelStream()
                .forEach(this::handle);
    }

    private Pair<Long, WeeklyReport.LeadMoveReport> makeLeadMoveReport(Map.Entry<Pair<Long, Long>, List<MoveLeadLog>> pair) {
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

    // todo: implement
    private void handle(WeeklyReport report) {
        System.out.println(report);
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
