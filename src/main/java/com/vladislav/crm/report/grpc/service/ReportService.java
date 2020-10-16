package com.vladislav.crm.report.grpc.service;

import com.proto.report.AddMoveLeadLogRequest;
import com.proto.report.AddMoveLeadLogResponse;
import com.proto.report.ReportServiceGrpc;
import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.SaveMoveLeadLogOperation;
import com.vladislav.crm.report.repositories.MoveLeadLogRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService extends ReportServiceGrpc.ReportServiceImplBase {

    private final SaveMoveLeadLogOperation saveMoveLeadLogOperation;

    @Override
    public void addMoveLeadLog(AddMoveLeadLogRequest request, StreamObserver<AddMoveLeadLogResponse> responseObserver) {
        final MoveLeadLog log = new MoveLeadLog()
                .setLeadId(request.getLeadId())
                .setUserId(request.getUserId())
                .setNextStatusId(request.getNextStatusId())
                .setPrevStatusId(request.getPrevStatusId());

        saveMoveLeadLogOperation.execute(log);

        responseObserver.onNext(AddMoveLeadLogResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
