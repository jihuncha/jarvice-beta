package huni.techtown.org.jarvice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import huni.techtown.org.jarvice.ui.utils.Tools;

/**
* SharedPreferences
*
* */
public class JarviceSettings {
    private volatile static JarviceSettings sInstance;

    private Context mContext;
    private SharedPreferences mSharedPrefs;

    private JarviceSettings(Context context) {
        this.mContext = context;
        this.mSharedPrefs = context.getSharedPreferences("jarvice_settings", Context.MODE_PRIVATE);
    }

    public static JarviceSettings getInstance(Context context) {
        if (sInstance == null) {
            synchronized (JarviceSettings.class) {
                if (sInstance == null) {
                    sInstance = new JarviceSettings(context);
                }
            }
        }

        return sInstance;
    }

    /* save 값들 */
    protected long save(String key, long value) {
        Log.d("jarvice_settings", "save - " + key + "=" + value);
        SharedPreferences.Editor edit = this.mSharedPrefs.edit();
        edit.putLong(key, value);
        edit.commit();
        return value;
    }

    protected int save(String key, int value) {
        Log.d("jarvice_settings", "save - " + key + "=" + value);
        SharedPreferences.Editor edit = this.mSharedPrefs.edit();
        edit.putInt(key, value);
        edit.commit();
        return value;
    }

    protected boolean save(String key, boolean value) {
        Log.d("jarvice_settings", "save - " + key + "=" + value);
        SharedPreferences.Editor edit = this.mSharedPrefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
        return value;
    }

    protected String save(String key, String value) {
        Log.d("jarvice_settings", "save - " + key + "=" + value);
        SharedPreferences.Editor edit = this.mSharedPrefs.edit();
        edit.putString(key, value);
        edit.commit();
        return value;
    }

    public int getMondaySellAvg() {
        return mSharedPrefs.getInt("mondaySellAvg", 0);
    }
    public int setMondaySellAvg(String input) {
        return save("mondaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

    public int getTuesdaySellAvg() {
        return mSharedPrefs.getInt("tuesdaySellAvg", 0);
    }
    public int setTuesdaySellAvg(String input) {
        return save("tuesdaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

    public int getWednesdayySellAvg() {
        return mSharedPrefs.getInt("wednesdaySellAvg", 0);
    }
    public int setWednesdaySellAvg(String input) {
        return save("wednesdaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

    public int getThursdaySellAvg() {
        return mSharedPrefs.getInt("thursdaySellAvg", 0);
    }
    public int setThursdaySellAvg(String input) {
        return save("thursdaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

    public int getFridaySellAvg() {
        return mSharedPrefs.getInt("fridaySellAvg", 0);
    }
    public int setFridaySellAvg(String input) {
        return save("fridaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

    public int getSaturdaySellAvg() {
        return mSharedPrefs.getInt("saturdaySellAvg", 0);
    }
    public int setSaturdaySellAvg(String input) {
        return save("saturdaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

    public int getSundaySellAvg() {
        return mSharedPrefs.getInt("sundaySellAvg", 0);
    }
    public int setSundaySellAvg(String input) {
        return save("sundaySellAvg", Integer.parseInt(Tools.deleteComma(input)));
    }

}
