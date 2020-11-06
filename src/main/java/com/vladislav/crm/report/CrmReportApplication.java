package com.vladislav.crm.report;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CrmReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmReportApplication.class, args);
    }
}
