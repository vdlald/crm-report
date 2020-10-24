package com.vladislav.crm.report.grpc.service;

import com.proto.report.*;
import com.vladislav.crm.report.grpc.GrpcServiceUtils;
import com.vladislav.crm.report.grpc.handlers.AddMoveLeadLogRequestHandler;
import com.vladislav.crm.report.grpc.handlers.AddNewLeadLogRequestHandler;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService extends ReportServiceGrpc.ReportServiceImplBase {

    private final AddMoveLeadLogRequestHandler addMoveLeadLogRequestHandler;
    private final AddNewLeadLogRequestHandler addNewLeadLogRequestHandler;

    @Override
    public void addMoveLeadLog(
            AddMoveLeadLogRequest request,
            StreamObserver<AddMoveLeadLogResponse> responseObserver
    ) {
        GrpcServiceUtils.handle(addMoveLeadLogRequestHandler, request, responseObserver);
    }

    @Override
    public void addNewLeadLog(
            AddNewLeadLogRequest request,
            StreamObserver<AddNewLeadLogResponse> responseObserver
    ) {
        GrpcServiceUtils.handle(addNewLeadLogRequestHandler, request, responseObserver);
    }
}
