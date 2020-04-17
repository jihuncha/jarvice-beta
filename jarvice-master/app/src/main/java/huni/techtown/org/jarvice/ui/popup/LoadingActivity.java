package huni.techtown.org.jarvice.ui.popup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import huni.techtown.org.jarvice.JarviceAction;
import huni.techtown.org.jarvice.R;

public class LoadingActivity extends Activity {
    private static final String TAG = LoadingActivity.class.getSimpleName();

    public static final String ACTION = "huni.techtown.org.jarvice.ui.popup.LoadingActivity";
    public static final String EXTRA  = "show-or-hide";
    public static final int    EXTRA_HIDE = 0;
    public static final int    EXTRA_SHOW = 1;

    private Context mContext;
    private Handler mHandler;

    private boolean   mRunning = false;
    private static boolean sShowRequested = false;
    private boolean   mFinishing = false;

    public static final void show(Context context, String f) {
        Log.d(TAG, "show() - f: " + f);
        Intent intent = new Intent(context, LoadingActivity.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA, EXTRA_SHOW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);

        //
        sShowRequested = true;
    }

    public static final void hide(Context context, String f) {
        Log.d(TAG, "hide() - f: " + f);

        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA, EXTRA_HIDE);
        boolean result = LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        //
        sShowRequested = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_loading);

        mContext = this;
        mHandler = new Handler();

        IntentFilter filter = new IntentFilter();
        filter.addAction(JarviceAction.Action.DATE_CHANGED);
        filter.addAction(ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mControlRecevier, filter);

        processIntent(getIntent(), "onCreate()");
//        startLoading();
    }

    public void startLoading() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }

    /**
     * 제어 메시지에 대한 수신 처리
     */
    private BroadcastReceiver mControlRecevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onProcess(intent, "onReceive");
        }
    };

    private void onProcess(Intent intent, String f) {
        Log.d(TAG, "[" + f + "] onProcess()");

        switch (JarviceAction.Action.valueOf(intent.getAction())) {
            case JarviceAction.Action.I_DATE_CHANGED:
                finish();
                break;
        }

        if(intent.getAction().equals(JarviceAction.Action.DATE_CHANGED)) {
            finish();
        }

    }

    public void processIntent(Intent intent, String f) {
        Log.d(TAG, "processIntent - f : " + f);
//        IntentUtil.show(TAG, "[" + f + "] processIntent", intent);
        if (intent.getIntExtra(EXTRA, EXTRA_HIDE) == EXTRA_SHOW) {
            if (!sShowRequested) {
                Log.d(TAG, "the Request is Canceled!!");
                exit("!sShowRequested");
                return;
            }

            mHandler.removeCallbacks(mSelfDestroy);
            mHandler.postDelayed    (mSelfDestroy, 10*1000);

            if (mRunning) {
                Log.e(TAG, "processIntent - Already Running!!");
                return;
            }
            //mLoadingAni.start();
//            startAnimation();
            mRunning = true;
        }
        else {
            //mLoadingAni.stop();
//            stopAnimation();
            exit("processIntent");
        }
        return;
    }

    private Runnable mSelfDestroy = new Runnable() {
        @Override
        public void run() {
            exit("SelfDestroy");
        }
    };

    private void exit(String f) {
        Log.d(TAG, "exit() - f: " + f + ", mFinishing: " + mFinishing);
        if (!mFinishing) {
            mFinishing = true;
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mControlRecevier);
    }
}
