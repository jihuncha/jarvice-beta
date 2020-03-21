package huni.techtown.org.jarvice.common.proto;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

public class IntentUtil {
    public IntentUtil() {
    }

    public static final void show(String tag, String f, int startId, Intent intent) {
        String action = null;
        ComponentName component = null;
        if (intent != null) {
            action = intent.getAction();
            component = intent.getComponent();
        }

        StringBuffer sb = new StringBuffer();
        Log.d(tag, "=================================================");
        sb.append("[" + f + "][" + action + "][" + component + "]");
        if (intent != null) {
            sb.append("extra: {");
            if (intent.getExtras() != null) {
                Set<String> keys = intent.getExtras().keySet();
                Iterator i = keys.iterator();

                while (i.hasNext()) {
                    String key = (String) i.next();
                    sb.append(key + ":" + intent.getExtras().get(key) + ", ");
                }
            }

            sb.append("},");
            sb.append("data: {" + intent.getData() + "}");
        }

        Log.i(tag, "IntentUtil: " + sb.toString());
    }

    public static final void show(String tag, String f, Intent intent) {
        show(tag, f, -1, intent);
    }
}

