package com.vladislav.crm.report.grpc.service;

import com.proto.report.ReportServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService extends ReportServiceGrpc.ReportServiceImplBase {
    
}
