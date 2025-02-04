package org.noteam.be.grpc.config;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcChannelConfig {

//    @Bean
//    public ManagedChannel keyRotationChannel() {
//        return NettyChannelBuilder.forAddress("testserver.com", 9090)
//                .usePlaintext()
//                .build();
//    }
}