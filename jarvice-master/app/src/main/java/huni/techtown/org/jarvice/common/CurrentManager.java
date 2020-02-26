package huni.techtown.org.jarvice.common;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    //날짜값 조회
    public static String toFormatString(long time, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date now = new Date(time);
        return df.format(now);
    }

//    //String , 없애기
//    public static int stringToInt(String input) {
//        for (int i = 0; i < test.size(); i++) {
//            String result = "";
//            if (test.get(i).getSell().contains(",")) {
//                result = test.get(i).getSell().replace(",", "");
//            } else {
//                result = test.get(i).getSell();
//            }
//            sum += Integer.parseInt(result);
//        }
//    }
}
