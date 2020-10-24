package com.vladislav.crm.report.grpc.handlers.impl;

import com.proto.report.AddMoveLeadLogRequest;
import com.proto.report.AddMoveLeadLogResponse;
import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.grpc.handlers.AddMoveLeadLogRequestHandler;
import com.vladislav.crm.report.operations.SaveMoveLeadLogOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddMoveLeadLogRequestHandlerImpl implements AddMoveLeadLogRequestHandler {

    private final SaveMoveLeadLogOperation saveMoveLeadLogOperation;

    @Override
    public AddMoveLeadLogResponse handle(AddMoveLeadLogRequest request) {
        final MoveLeadLog log = new MoveLeadLog()
                .setLeadId(request.getLeadId())
                .setUserId(request.getUserId())
                .setNextStatusId(request.getNextStatusId())
                .setPrevStatusId(request.getPrevStatusId());

        saveMoveLeadLogOperation.execute(log);
        return AddMoveLeadLogResponse.newBuilder().build();
    }
}
