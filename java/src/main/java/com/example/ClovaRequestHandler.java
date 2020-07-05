package com.example;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.linecorp.clova.extension.boot.handler.annnotation.CEKRequestHandler;
import com.linecorp.clova.extension.boot.handler.annnotation.IntentMapping;
import com.linecorp.clova.extension.boot.handler.annnotation.LaunchMapping;
import com.linecorp.clova.extension.boot.handler.annnotation.SessionEndedMapping;
import com.linecorp.clova.extension.boot.message.response.CEKResponse;
import com.linecorp.clova.extension.boot.message.speech.OutputSpeech;

@CEKRequestHandler
public class ClovaRequestHandler {

    @LaunchMapping
    public CEKResponse handleLaunch() {
        return createResponse("起動しました", false);
    }

    @IntentMapping("Clova.GuideIntent")
    public CEKResponse handleGuideIntent() {
        return createResponse("これはデモ用のスキルです", false);
    }

    @IntentMapping("Clova.YesIntent")
    public CEKResponse handleYesIntent() {
        return createResponse("はい", false);
    }

    @IntentMapping("Clova.NoIntent")
    public CEKResponse handleNoIntent() {
        return createResponse("いいえ", true);
    }

    @IntentMapping("Clova.CancelIntent")
    public CEKResponse handleCancelIntent() {
        return createResponse("終了します", true);
    }

    @SessionEndedMapping
    public CEKResponse handleSessionEnded() {
        return CEKResponse.empty();
    }

    @ExceptionHandler
    public CEKResponse handleException(Exception e) {
        return createResponse("もう一度、お願いします", false);
    }

    private static CEKResponse createResponse(String text, boolean shouldEndSession) {
        return CEKResponse.builder()
                          .outputSpeech(OutputSpeech.text(text))
                          .shouldEndSession(shouldEndSession)
                          .build();
    }
}
