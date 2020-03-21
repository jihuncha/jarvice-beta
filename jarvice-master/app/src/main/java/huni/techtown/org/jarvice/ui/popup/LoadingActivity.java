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

    private Context mContext;
    private Handler mHandler;

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
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mControlRecevier, filter);

        startLoading();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mControlRecevier);
    }
}
