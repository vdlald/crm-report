package com.vladislav.crm.report.grpc.service;

import com.proto.report.AddMoveLeadLogRequest;
import com.proto.report.AddMoveLeadLogResponse;
import com.proto.report.ReportServiceGrpc;
import com.vladislav.crm.report.grpc.GrpcServiceUtils;
import com.vladislav.crm.report.grpc.handlers.AddMoveLeadLogRequestHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService extends ReportServiceGrpc.ReportServiceImplBase {

    private final AddMoveLeadLogRequestHandler addMoveLeadLogRequestHandler;

    @Override
    public void addMoveLeadLog(
            AddMoveLeadLogRequest request,
            StreamObserver<AddMoveLeadLogResponse> responseObserver
    ) {
        GrpcServiceUtils.handle(addMoveLeadLogRequestHandler, request, responseObserver);
    }
}
