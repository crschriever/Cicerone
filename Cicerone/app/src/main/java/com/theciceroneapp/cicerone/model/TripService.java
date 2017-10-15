package com.theciceroneapp.cicerone.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * Created by crsch on 10/14/2017.
 */

public class TripService extends Service {

    public static TripService singleton;
    private TextToSpeech toSpeech;
    private int result;
    private MyUtteranceProgressListener progressListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        singleton = this;
        System.out.println("initializing trip service");

        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.US);
                } else {
                    System.out.println("Error! Text to speech not supported");
                }
            }
        });

        progressListener = new MyUtteranceProgressListener();

        toSpeech.setOnUtteranceProgressListener(progressListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toSpeech.stop();
    }

    public static void say(String text, TalkPromise promise) {
        singleton.progressListener.promise = promise;
        String utteranceId = singleton.hashCode() + "";
        singleton.toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    public static void stop() {
        singleton.toSpeech.stop();
    }

    private class MyUtteranceProgressListener extends UtteranceProgressListener {
        private TalkPromise promise;

        @Override
        public void onStart(String utteranceId) {
            System.out.println("Talking");
        }

        @Override
        public void onDone(String utteranceId) {
            System.out.println("Done Talking");
            promise.talkingDone();
        }

        @Override
        public void onStop(String utterandeID, boolean interrupted) {
            System.out.println("Stopped Talking");
            promise.talkingDone();
        }

        @Override
        public void onError(String utteranceId) {
            System.out.println("Error Talking");
            promise.talkingDone();
        }
    }

}
