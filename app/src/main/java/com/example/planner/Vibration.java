package com.example.planner;

import android.content.Context;
import android.os.Vibrator;

public class Vibration {
    public Vibration(Context parent) {
        Vibrator v = (Vibrator) parent.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
    }
}
