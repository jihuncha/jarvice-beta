package huni.techtown.org.jarvice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import huni.techtown.org.jarvice.common.DatabaseManager;

public class JarviceApp extends MultiDexApplication {
    private static final String TAG = JarviceApp.class.getSimpleName();

    private static Context sContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;

//        Stetho.initializeWithDefaults(this);
//
//        //데이터 베이스 인스턴스 생성
//        DatabaseManager.getInstance(this);
        //항상 CR 활성화
//        new CrashReporter(this);

        Log.i(TAG, "onCreate() ==================================");
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate() ==================================");
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
