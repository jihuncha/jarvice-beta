package huni.techtown.org.jarvice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = Settings.class.getSimpleName();

    private final static String NAME = "Settings";
    private Context mContext;
    private OnPTTSetCfgChangeListener mListener;

    protected SharedPreferences mPrefs;

    private volatile static Settings sInstance;

    public static Settings getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Settings.class) {
                if (sInstance == null) {
                    sInstance = new Settings(context);
                }
            }
        }

        return sInstance;
    }

    @SuppressLint("InlinedApi")
    private Settings(Context context) {
        this.mContext = context;
        this.mPrefs   = context.getSharedPreferences(NAME, 0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "onSharedPreferenceChanged(" + key + ")");

        if (mListener != null) {
            mListener.onChanged(key);
        }
    }

    public interface OnPTTSetCfgChangeListener {
        public void onChanged(String key);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    protected int save(String key, int value) {
        Log.d(TAG, "save - " + key + "=" + value);
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putInt(key, value);
        edit.commit();

        return value;
    }
    protected String save(String key, String value) {
        Log.d(TAG, "save - " + key + "=" + value);
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(key, value);
        edit.commit();

        return value;
    }
    protected boolean save(String key, boolean value) {
        Log.d(TAG, "save - " + key + "=" + value);
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean(key, value);
        edit.commit();

        return value;
    }

    public void clear() {
        Log.e(TAG, "PTTSetCfg are Cleared!!!");
        //
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.clear();
        edit.commit();
        //

        return;
    }


}
