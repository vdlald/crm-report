package com.vladislav.crm.report.jobs;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.GetAllUserIdFromMoveLeadLogsOperation;
import com.vladislav.crm.report.operations.ReadAllMoveLeadLogByUserIdOperation;
import com.vladislav.crm.report.pojo.WeeklyReport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeeklyReportJob implements Runnable {

    private final GetAllUserIdFromMoveLeadLogsOperation getAllUserIdFromMoveLeadLogsOperation;
    private final ReadAllMoveLeadLogByUserIdOperation readAllMoveLeadLogByUserIdOperation;

    @Override
    @Scheduled(cron = "0 0 1 * * MON")
    public void run() {
        final Set<Long> userIds = getAllUserIdFromMoveLeadLogsOperation.execute();
        userIds.parallelStream()
                .map(readAllMoveLeadLogByUserIdOperation::execute)
                .map(this::makeWeeklyReport)
                .forEach(this::handle);
    }

    private WeeklyReport makeWeeklyReport(Collection<MoveLeadLog> moveLeadLogs) {
        // todo: implement
        return new WeeklyReport();
    }

    private void handle(WeeklyReport report) {
        // todo: implement
    }
}
