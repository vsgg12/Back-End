package com.example.mdmggreal.sse.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SSEController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter events() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        // 클라이언트 연결 종료 시 처리
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void sendNotification(String message) {
        for(SseEmitter emitter : emitters) {
            try {
                emitter.send(message, MediaType.TEXT_PLAIN);
            } catch (IOException e) {
                emitters.remove(emitter);
                e.printStackTrace();
            }
        }
    }
}
