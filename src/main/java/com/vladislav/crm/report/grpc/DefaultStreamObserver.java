package com.vladislav.crm.report.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
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

    public static <T> DefaultStreamObserverBuilder<T> newBuilder() {
        return new DefaultStreamObserverBuilder<>();
    }

    @Accessors(chain = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DefaultStreamObserverBuilder<T> {

        @Setter
        private Consumer<T> onNext = (T object) -> {
        };

        @Setter
        private Consumer<Throwable> onError = t -> log.error(t.getLocalizedMessage(), t);

        @Setter
        private Runnable onCompleted = () -> {
        };

        public DefaultStreamObserver<T> build() {
            return new DefaultStreamObserver<>() {
                @Override
                public void onNext(T value) {
                    onNext.accept(value);
                }

                @Override
                public void onError(Throwable t) {
                    onError.accept(t);
                }

                @Override
                public void onCompleted() {
                    onCompleted.run();
                }
            };
        }
    }
}
