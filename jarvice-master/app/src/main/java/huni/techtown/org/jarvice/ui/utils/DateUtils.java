package huni.techtown.org.jarvice.ui.utils;

import android.util.Log;

public class DateUtils {
    private static long mPrevious = 0L;

    public static long elapsedTime() {
        long current = System.currentTimeMillis();
        long elapsed = current - mPrevious;
        mPrevious = current;
        return elapsed;
    }

    public static void d(String tag, String desc) {
        Log.d(tag, "[ELAPSED=" + elapsedTime() + "] " + desc);
    }

}
