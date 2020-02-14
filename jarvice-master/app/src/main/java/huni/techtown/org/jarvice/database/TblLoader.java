package huni.techtown.org.jarvice.database;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;
import java.util.Vector;

/**
* 부하가 큰 쿼리를 별도의 쓰레드로 처리한다...
* */
public class TblLoader {
    public static final String TAG = TblLoader.class.getSimpleName();

    private String  name = null;
    private TblTask task = null;
    private Vector<Request> queue;

    private boolean running = false;

    public TblLoader(String name) {
        this.name  = name;
        this.task  = new TblTask();
        this.queue = new Vector<>();
    }

    public interface Listener {
        public void onCompleted(Cursor cursor);
    }

    public void load(TABLE table, String[] columns, String whereClause, String[] whereArgs, String orderBy, String limit, Listener listener) {
        Log.d(TAG + "." + name, "load()");
        Request request = new Request(table, columns, whereClause, whereArgs, orderBy, limit, listener);
        synchronized (queue) {
            //항상 마지막 요청만 유지한다.
            queue.clear();
            queue.add(request);

            //Task를 깨운다.
            queue.notifyAll();
        }
    }

    private Request next() {
        Log.d(TAG + "." + name, "next()");
        synchronized (queue) {
            if (queue.size() > 0) {
                return queue.remove(0);
            }
        }
        return null;
    }

    public void waitWakeup(String f) {
        Log.d(TAG, "waitWakeup() - f: " + f);
        try {
            synchronized (queue) {
                if (queue.size() == 0) {
                    queue.wait(300 * 1000);
                }
            }
        }
        catch (Exception ignore) {
        }
    }

    public void startLoader() {
        synchronized (queue) {
            running = true;
            task.start();
        }
    }

    public void stopLoader() {
        synchronized (queue) {
            running = false;
            queue.notifyAll();
        }
    }

    public class TblTask extends Thread {
        private long    lastRequestTime = 0;
        public TblTask() { }

        @Override
        public void run() {
            Log.d(TAG + "." + name, "TblTask START.....");
            while (running) {
                //여기서 대기!!
                waitWakeup("run");

                //Request 조회 및 처리
                Request request = next();
                if (request != null) {
                    Log.d(TAG + "." + name, "run() - request: " + request.toString());
                    Cursor cursor = cursor(request);
                    if (request.listener != null) {
                        request.listener.onCompleted(cursor);
                    }
                }
            }
            Log.d(TAG + "." + name, "TblTask STOP.....");

            //
            running = false;

            return;
        }

        private Cursor cursor(Request request) {
            try {
                Cursor c = request.table.cursor(
                        request.columns,
                        request.whereClause,
                        request.whereArgs,
                        request.orderBy,
                        request.limit);
                return c;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class Request {
        public TABLE    table;
        public String[] columns;
        public String   whereClause;
        public String[] whereArgs;
        public String   orderBy;
        public String   limit;
        public Listener listener;
        public Request(TABLE table, String[] columns, String whereClause, String[] whereArgs, String orderBy, String limit, Listener listener) {
            this.table       = table;
            this.columns     = columns;
            this.whereClause = whereClause;
            this.whereArgs   = whereArgs;
            this.orderBy  = orderBy;
            this.limit    = limit;
            this.listener = listener;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "table='" + table.tableName + '\'' +
                    ", columns=" + Arrays.toString(columns) +
                    ", whereClause='" + whereClause + '\'' +
                    ", whereArgs=" + Arrays.toString(whereArgs) +
                    ", orderBy='" + orderBy + '\'' +
                    ", limit='" + limit + '\'' +
                    '}';
        }
    }
}
