package com.intprices.util;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * Created by Alex Poltavets on 07.11.2016.
 */

public class Timeleft {
    public static String timeLeftFormatted(long millisUntilFinished, Context context) {

        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

        // TODO translate
        if (days > 0) {
            return String.format("%d day(s) %d hour(s)", days, hours);
        } else if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else if (minutes > 0){
            return String.format("%02d:%02d", minutes, seconds);
        } else if (seconds > 0) {
            return String.format("%02d second(s)", seconds);
        } else {
            return "";
        }
    }
}
