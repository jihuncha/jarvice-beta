package huni.techtown.org.jarvice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import huni.techtown.org.jarvice.common.proto.IntentUtil;

public class JarviceAction {
    public static final String TAG = JarviceAction.class.getSimpleName();

    public static final int I_NONE = -1;

    /*** Action 정의 *********************************/
    public static class Action {
        /* 문자열 상수 */
        public static final String DATE_CHANGED = "huni.techtown.org.jarvice.action.DATE_CHANGED";

        /* 숫자 상수 */
        public static final int I_DATE_CHANGED   = 0;

        /* 변환 함수 */
        public static final int valueOf(String s) {
            if (DATE_CHANGED.equals(s)) {
                return I_DATE_CHANGED;
            }

            return I_NONE;
        }
    }

    public static boolean sendDateChangedEnd(Context context, String f){
        Log.d(TAG, "sendDateChangedEnd() - f: " + f);

        Intent intent = new Intent(Action.DATE_CHANGED);

//        IntentUtil.show(TAG, "sendDateChangedEnd()", intent);
        if (!LocalBroadcastManager.getInstance(context).sendBroadcast(intent)) {
            Log.w(TAG, "[" + f + "] sendDateChangedEnd() failed!");
            return false;
        }

        return true;
    }

}
