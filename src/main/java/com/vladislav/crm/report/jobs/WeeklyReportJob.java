package com.vladislav.crm.report.jobs;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.GetAllMoveLeadLogByUserIdOperation;
import com.vladislav.crm.report.operations.GetAllUserIdFromMoveLeadLogsOperation;
import com.vladislav.crm.report.pojo.WeeklyReport;
import com.vladislav.crm.report.utils.LocalizedWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeeklyReportJob implements Runnable {

    private final GetAllUserIdFromMoveLeadLogsOperation getAllUserIdFromMoveLeadLogsOperation;
    private final GetAllMoveLeadLogByUserIdOperation getAllMoveLeadLogByUserIdOperation;

    @Override
    @Scheduled(cron = "0 0 1 * * MON")
    public void run() {
        final LocalizedWeek localizedWeek = new LocalizedWeek();
        final Function<Long, Collection<MoveLeadLog>> getMoveLeadLogs = (userId) -> getAllMoveLeadLogByUserIdOperation
                .execute(userId, localizedWeek.getFirstWeekDateTime(), localizedWeek.getLastWeekDateTime());

        final Set<Long> userIds = getAllUserIdFromMoveLeadLogsOperation.execute();

        userIds.parallelStream()
                .map(getMoveLeadLogs)
                .map(this::makeWeeklyReport)
                .forEach(this::handle);
    }

    // todo: implement
    private WeeklyReport makeWeeklyReport(Collection<MoveLeadLog> moveLeadLogs) {
        return new WeeklyReport();
    }

    // todo: implement
    private void handle(WeeklyReport report) {
    }
}
