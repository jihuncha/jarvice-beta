package huni.techtown.org.jarvice.component;

import android.os.Handler;

public class RuntimeServiceHelper {
    public static final String TAG = RuntimeServiceHelper.class.getSimpleName();

    private RuntimeService mService;
    private Handler mHandler;


    public RuntimeServiceHelper(RuntimeService service, Handler handler) {
        mService    = service;
        mHandler    = handler;
    }

}
