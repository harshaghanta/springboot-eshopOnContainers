package com.eshoponcontainers.basketapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import com.eshoponcontainers.proto.catalog.CatalogGrpc;
import com.eshoponcontainers.proto.catalog.CatalogGrpc.CatalogBlockingStub;

@Configuration
public class GrpcClientConfig {

    @Bean
    public CatalogBlockingStub catalogClient(GrpcChannelFactory channelFactory) {
        return CatalogGrpc.newBlockingStub(channelFactory.createChannel("catalog-grpc"));
    }
}
