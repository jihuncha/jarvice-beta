package huni.techtown.org.jarvice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import huni.techtown.org.jarvice.ui.MainActivity;

public class JarviceConfig {
    public static final String TAG = JarviceConfig.class.getSimpleName();

    /*** Action 정의 *********************************/
    public static class Action {
        public static final String MAIN_ACTIVITY    = "huni.techtown.org.jarvice.action.MAIN_ACTIVITY";
    }

    /*** Shortcut 정의 (startActivity) ********************************/
    /**
     * Main Activity를 실행한다.
     * @param context
     * @param originIntent
     * @param f
     */
    public static void startMainActivity(Context context, Intent originIntent, String action, String f) {
        Log.d(TAG, "startMainActivity() - f: " + f + ", originIntent: " + originIntent);

        Intent intent = new Intent(context, MainActivity.class);
        if (originIntent != null) {
            if (originIntent.getAction() != null) {
                intent.setAction(originIntent.getAction());
            }
            if (originIntent.getExtras() != null) {
                intent.putExtras(originIntent.getExtras());
            }
            if (originIntent.getData() != null) {
                intent.setData(originIntent.getData());
            }
        }
        if (action != null) {
            intent.setAction(Action.MAIN_ACTIVITY);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }
}
