package com.vladislav.crm.report.grpc.service;

import com.google.protobuf.Empty;
import com.proto.debug.DebugGrpc;
import com.vladislav.crm.report.jobs.WeeklyReportJob;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DebugService extends DebugGrpc.DebugImplBase {

    private final ApplicationContext context;
    private final WeeklyReportJob weeklyReportJob;

    @Override
    public void attachToDebug(Empty request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    @Override
    public void runWeeklyReportJob(Empty request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(request);
        responseObserver.onCompleted();
        weeklyReportJob.run();
    }
}
