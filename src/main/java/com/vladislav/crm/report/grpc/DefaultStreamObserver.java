package com.vladislav.crm.report.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class DefaultStreamObserver<T> implements StreamObserver<T> {
    @Override
    public void onNext(T value) {
    }

    @Override
    public void onError(Throwable t) {
        log.error(t.getLocalizedMessage(), t);
    }

    @Override
    public void onCompleted() {
    }

    public static <T> DefaultStreamObserver<T> onNext(Consumer<T> consumer) {
        return new DefaultStreamObserver<>() {
            @Override
            public void onNext(T value) {
                consumer.accept(value);
            }
        };
    }
}
