package com.vladislav.crm.report.grpc;

import com.google.protobuf.GeneratedMessageV3;
import com.vladislav.crm.report.grpc.handlers.RequestHandler;
import io.grpc.stub.StreamObserver;

public class GrpcServiceUtils {

    private GrpcServiceUtils() {
    }

    public static <REQ extends GeneratedMessageV3, RES extends GeneratedMessageV3> void handle(
            RequestHandler<REQ, RES> handler, REQ request, StreamObserver<RES> responseObserver
    ) {
        final RES response = handler.handle(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
