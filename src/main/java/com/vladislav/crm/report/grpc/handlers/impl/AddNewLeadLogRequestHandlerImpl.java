package com.vladislav.crm.report.grpc.handlers.impl;

import com.proto.report.AddNewLeadLogRequest;
import com.proto.report.AddNewLeadLogResponse;
import com.vladislav.crm.report.documents.NewLeadLog;
import com.vladislav.crm.report.grpc.handlers.AddNewLeadLogRequestHandler;
import com.vladislav.crm.report.operations.SaveNewLeadLogOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddNewLeadLogRequestHandlerImpl implements AddNewLeadLogRequestHandler {

    private final SaveNewLeadLogOperation saveNewLeadLogOperation;

    @Override
    public AddNewLeadLogResponse handle(AddNewLeadLogRequest request) {
        final NewLeadLog log = new NewLeadLog()
                .setUserId(request.getUserId())
                .setLeadId(request.getLeadId());

        saveNewLeadLogOperation.execute(log);

        return AddNewLeadLogResponse.newBuilder().build();
    }
}
