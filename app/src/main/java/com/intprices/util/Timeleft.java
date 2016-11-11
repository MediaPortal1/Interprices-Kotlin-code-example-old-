package com.intprices.util;

import android.content.Context;

import com.intprices.R;

import java.util.concurrent.TimeUnit;


class TimeLeft {
    static String timeLeftFormatted(long millisUntilFinished, Context context) {

        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

        if (days > 0) {
            return context.getString(R.string.time_days, days, hours);
        } else if (hours > 0) {
            return context.getString(R.string.time_hours, hours, minutes, seconds);
        } else if (minutes > 0) {
            return context.getString(R.string.time_minutes, minutes, seconds);
        } else if (seconds > 0) {
            return context.getString(R.string.time_second, seconds);
        } else {
            return "";
        }
    }
}
