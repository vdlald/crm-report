package com.vladislav.crm.report.configurations;

import com.proto.leads.LeadServiceGrpc;
import com.proto.users.UserServiceGrpc;
import io.grpc.*;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.MetadataUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GrpcConfiguration {

    @Bean
    Server server(
            @Value("${app.grpc.server.port}") Integer port,
            @Autowired(required = false) List<BindableService> services,
            @Autowired(required = false) List<ServerInterceptor> interceptors
    ) {
        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
        if (interceptors != null) {
            interceptors.forEach(serverBuilder::intercept);
        }
        if (services != null) {
            services.forEach(serverBuilder::addService);
        }
        return serverBuilder.build();
    }

    @Bean
    UserServiceGrpc.UserServiceStub userService(Channel userChannel) {
        return attachAuth(UserServiceGrpc.newStub(userChannel));
    }

    @Bean
    LeadServiceGrpc.LeadServiceStub leadService(Channel userChannel) {
        return attachAuth(LeadServiceGrpc.newStub(userChannel));
    }

    @Bean
    LeadServiceGrpc.LeadServiceBlockingStub leadServiceBlocking(Channel userChannel) {
        return attachAuth(LeadServiceGrpc.newBlockingStub(userChannel));
    }

    @Bean
    Channel serverChannel(
            @Value("${app.grpc.user-client.host}") String host,
            @Value("${app.grpc.user-client.port}") Integer port
    ) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    private <T extends AbstractStub<T>> T attachAuth(T stub) {
        val key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        final Metadata metadata = new Metadata();
        metadata.put(key, "Basic ZGVtbzpkZW1vZGVtbw==");
        return MetadataUtils.attachHeaders(stub, metadata);
    }
}

