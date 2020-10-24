package com.vladislav.crm.report.clients;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class EmailClient {

    public void sendEmail(byte[] pdf, String email) {
        try {
            Files.write(Paths.get("./.temp/emails/" + email), pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
