//
//package org.noteam.be.grpc.client;
//
//import io.grpc.ManagedChannel;
//import lombok.extern.slf4j.Slf4j;
//import org.noteam.be.grpc.KeyRotationNotifyServiceGrpc;
//import org.noteam.be.grpc.NotifyKeyRolledRequest;
//import org.noteam.be.grpc.NotifyKeyRolledResponse;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//
//@Slf4j
//@Component
//public class KeyRotationNotifyClient {
//
//    private final KeyRotationNotifyServiceGrpc.KeyRotationNotifyServiceBlockingStub stub;
//
//    public KeyRotationNotifyClient(ManagedChannel keyRotationChannel) {
//        // 생성된 KeyRotationNotifyServiceGrpc ㅂㄷㅂㄷ...
//        this.stub = KeyRotationNotifyServiceGrpc.newBlockingStub(keyRotationChannel);
//    }
//
//    public void notifyKeyRolled(String prevKid, String currKid) {
//        NotifyKeyRolledRequest request = NotifyKeyRolledRequest.newBuilder()
//                .setPreviousKid(prevKid)
//                .setCurrentKid(currKid)
//                .setRolledAt(Instant.now().toString())
//                .build();
//
////        NotifyKeyRolledResponse response = stub.notifyKeyRolled(request);
////        log.info("GO SERVER 응답: " + response.getMessage());
//    }
//}