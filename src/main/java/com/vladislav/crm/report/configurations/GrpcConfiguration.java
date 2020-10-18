package com.vladislav.crm.report.configurations;

import com.proto.users.UserServiceGrpc;
import io.grpc.*;
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
        return UserServiceGrpc.newStub(userChannel);
    }

    @Bean
    Channel userChannel(
            @Value("${app.grpc.user-client.host}") String host,
            @Value("${app.grpc.user-client.port}") Integer port
    ) {
        return ManagedChannelBuilder.forAddress(host, port).build();
    }
}

