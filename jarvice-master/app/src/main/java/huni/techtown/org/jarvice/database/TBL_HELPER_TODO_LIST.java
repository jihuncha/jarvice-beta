package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.beloo.widget.chipslayoutmanager.util.log.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.HelperTodoListObject;

/**
 *
 * 오늘 할일 list 테이블
 *
 * */
public class TBL_HELPER_TODO_LIST extends TABLE <HelperTodoListObject> {
    private String TAG = TBL_HELPER_TODO_LIST.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_helper_todo_list";

    /*** Column 이름 정의 **********************/
    public static final String ID					             = "_id";
    public static final String TODO_DATE		                 = "todo_date";                // 날짜 - 년월일로
    public static final String TODO_TITLE                        = "todo_title";               // 업무 타이틍
    public static final String TODO_COLUMN                       = "todo_column";              // 컬럼이 타이틀인가 아닌가 체크 -> 0 이면 타이틀 / 1이면 리스트 항목
    public static final String TODO_CHECK                        = "todo_check";               // 체크 여부
    public static final String TODO_WORK                         = "todo_work";                // 업무 내용

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
            "(" +
            ID						    + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TODO_DATE                   + " TEXT," +
            TODO_TITLE                  + " TEXT," +
            TODO_COLUMN                 + " INTEGER DEFAULT 0," +
            TODO_CHECK                  + " INTEGER," +
            TODO_WORK                   + " TEXT " +
            ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, TODO_DATE, TODO_TITLE, TODO_COLUMN, TODO_CHECK, TODO_WORK;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.TODO_DATE               = c.getColumnIndex(TODO_DATE);
        idx.TODO_TITLE              = c.getColumnIndex(TODO_TITLE);
        idx.TODO_COLUMN             = c.getColumnIndex(TODO_COLUMN);
        idx.TODO_CHECK              = c.getColumnIndex(TODO_CHECK);
        idx.TODO_WORK               = c.getColumnIndex(TODO_WORK);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_HELPER_TODO_LIST(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

    /**
     * 오브젝트를 DailySalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(HelperTodoListObject o) {
        ContentValues values = new ContentValues();

        values.put(TODO_DATE,               o.getTodoDate());
        values.put(TODO_TITLE,              o.getTodoTitle());
        values.put(TODO_COLUMN,             o.getTodoColumn());
        values.put(TODO_CHECK,              o.getTodoCheck());
        values.put(TODO_WORK,               o.getTodoWork());

        return values;
    }

    @Override
    public List<HelperTodoListObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<HelperTodoListObject> list = new ArrayList<HelperTodoListObject>();
        try {
            if (c.moveToFirst()) {
                INDEX idx = cursorToIndex(c);
                do {
                    list.add(fetchCursor2Object(idx, c));
                }
                while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 커서를 받아 리스트로 반환한다.
     * @param c
     * @return
     * @throws Exception
     */
    public HelperTodoListObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        HelperTodoListObject o = new HelperTodoListObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.TODO_DATE != -1) o.setTodoDate(c.getString(idx.TODO_DATE));
        if (idx.TODO_TITLE != -1) o.setTodoTitle(c.getString(idx.TODO_TITLE));
        if (idx.TODO_COLUMN != -1) o.setTodoColumn(c.getInt(idx.TODO_COLUMN));
        if (idx.TODO_CHECK != -1) o.setTodoCheck(c.getInt(idx.TODO_CHECK));
        if (idx.TODO_WORK != -1) o.setTodoWork(c.getString(idx.TODO_WORK));

        return o;
    }

    public void insert(List<HelperTodoListObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (HelperTodoListObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<HelperTodoListObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (HelperTodoListObject contact : addList) {
                if (insert(contact) > 0) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
            Log.d(TABLE_NAME, "insertForSync() - count: " + count);
            return count;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        Log.d(TABLE_NAME, "insertForSync() - count: " + -1);

        return -1;
    }

    public List<HelperTodoListObject> getList() {
        return select(null, null, null, null, null);
    }

    public List<HelperTodoListObject> getTodayList(String input) {
        Log.v(TABLE_NAME, "getTodayList() - input: " + input);
        String whereClause = "SELECT * FROM " + tableName + " WHERE " + TODO_DATE + " = '" + input + "'";
        return select_raw(whereClause);
    }

//    public boolean updateTodoList(String input) {
//        Log.v(TABLE_NAME, "updateNotification() - input: " + input);
//        String where = TODO_TITLE + " = " + input;
//        return update(input, where, null);
//    }


    public boolean updateTodoList(String input, String before) {
        Log.v(TABLE_NAME, "updateTodoList() - input: " + input + ", before - " + before);
        String sql =
                "UPDATE " + TABLE_NAME
                        + " SET "
                        +  TODO_TITLE + "= '" + input + "'"
                        + " WHERE " + TODO_TITLE + "= '" + before + "'";
        return update_raw(sql);
    }

    public boolean deleteTodoList(String input) {
        Log.v(TABLE_NAME, "updateTodoList() - input: " + input);
        String sql =
                "DELETE FROM " + TABLE_NAME
                        + " WHERE " + TODO_TITLE + "= '" + input + "'";
        return update_raw(sql);
    }

    public boolean updateTodoListDetail(HelperTodoListObject inputData) {
        Log.v(TABLE_NAME, "updateTodoListDetail() - inputData: " + inputData);
        String where = ID + " = " + inputData.getId();
//                + " AND " + TODO_TITLE + " = '" + input + "'";
        return update(inputData, where, null);
    }


    public int deleteTodoListDetail(HelperTodoListObject inputData) {
        Log.v(TABLE_NAME, "deleteTodoListDetail() - input: " + inputData);
        String where = ID + " = " + inputData.getId();
        return delete(where, null);
    }

    public boolean deleteTodoListNoColumn(String input) {
        Log.v(TABLE_NAME, "deleteTodoListNoColumn() - input: " + input);
        String sql =
                "DELETE FROM " + TABLE_NAME
                        + " WHERE " + TODO_TITLE + "= '" + input + "'"
                        + " AND " + TODO_COLUMN + "= " + 1;
        return update_raw(sql);
    }

    public boolean updateTodoListCheck(long input, int checkResult) {
        Log.v(TABLE_NAME, "updateTodoList() - input: " + input + ", checkResult - " + checkResult);
        String sql =
                "UPDATE " + TABLE_NAME
                        + " SET "
                        +  TODO_CHECK + "= " + checkResult + ""
                        + " WHERE " + ID + "= " + input + "";
        return update_raw(sql);
    }
}
