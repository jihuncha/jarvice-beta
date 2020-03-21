package huni.techtown.org.jarvice.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import huni.techtown.org.jarvice.ui.Fragment.TabLayoutHelper;
import huni.techtown.org.jarvice.ui.MainActivity;

public class CurrentManager {
    public static final String TAG = CurrentManager.class.getSimpleName();

    private volatile static CurrentManager sInstance;
    private Context mContext = null;

    private CurrentManager(Context context) {
        mContext  = context;

    }

    public static CurrentManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CurrentManager.class) {
                if (sInstance == null) {
                    sInstance = new CurrentManager(context);
                }
            }
        }
        return sInstance;
    }

    public boolean rawData = false;
    public boolean rawDataFinish(boolean check, String f) {
        Log.d(TAG, "rawDataFinish - " + check + " , f - " + f);

        if(check) {
            rawData = true;
            return true;
        } else {
            rawData = false;
            return false;
        }
    }

    public boolean rawDataCheck(){
        if (rawData) {
            return true;
        }
        return false;
    }

    public boolean dailyData = false;
    public boolean dailyDataFinish(boolean check, String f) {
        Log.d(TAG, "dailyDataFinish - " + check + " , f - " + f);

        if(check) {
            dailyData = true;
            return true;
        } else {
            dailyData = false;
            return false;
        }
    }

    public boolean dailyDataCheck(){
        if (dailyData) {
            return true;
        }
        return false;
    }

    public boolean weeklyData = false;
    public boolean weeklyDataFinish(boolean check, String f) {
        Log.d(TAG, "weeklyDataFinish - " + check + " , f - " + f);

        if(check) {
            weeklyData = true;
            return true;
        } else {
            weeklyData = false;
            return false;
        }
    }

    public boolean weeklyDataCheck(){
        if (weeklyData) {
            return true;
        }
        return false;
    }

    public boolean monthlyData = false;
    public boolean monthlyDataFinish(boolean check, String f) {
        Log.d(TAG, "monthlyDataFinish - " + check + " , f - " + f);

        if(check) {
            monthlyData = true;
            return true;
        } else {
            monthlyData = false;
            return false;
        }
    }

    public boolean monthlyDataCheck(){
        if (monthlyData) {
            return true;
        }
        return false;
    }


    //날짜값 조회
    public static String toFormatString(long time, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date now = new Date(time);
        return df.format(now);
    }

}
