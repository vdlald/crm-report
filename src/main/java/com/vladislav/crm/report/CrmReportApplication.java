package com.vladislav.crm.report;

import com.vladislav.crm.report.repositories.MoveLeadLogRepository;
import com.vladislav.crm.report.repositories.NewLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CrmReportApplication implements ApplicationRunner {

    private final MoveLeadLogRepository moveLeadLogRepository;
    private final NewLeadLogRepository newLeadLogRepository;

    public static void main(String[] args) {
        SpringApplication.run(CrmReportApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        moveLeadLogRepository.deleteAll();
        newLeadLogRepository.deleteAll();
    }
}
