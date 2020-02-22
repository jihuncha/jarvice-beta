package huni.techtown.org.jarvice.component;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class RuntimeService extends Service {
    public static final String TAG = RuntimeService.class.getSimpleName();

    public static RuntimeService sInstance = null;

    private Context mContext;
    private Handler mHandler;
    private RuntimeServiceHelper mRuntimeHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        /*** init *************/
        mContext  = this;
        mHandler  = new Handler(getMainLooper());
        mRuntimeHelper = new RuntimeServiceHelper(this, mHandler);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
