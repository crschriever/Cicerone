package com.theciceroneapp.cicerone.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * Created by crsch on 10/14/2017.
 */

public class TripService extends Service {

    public static TripService singleton;
    private TextToSpeech toSpeech;
    private int result;

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
    }

    public static void say(String text) {
        String utteranceId = singleton.hashCode() + "";
        singleton.toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
