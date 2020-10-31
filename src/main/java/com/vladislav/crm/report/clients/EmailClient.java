package com.vladislav.crm.report.clients;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class EmailClient {

    public void sendEmail(byte[] pdf, String email) {
        try {
            Files.write(Path.of("emails/" + email + ".pdf"), pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
