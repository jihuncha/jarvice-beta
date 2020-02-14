package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.MonthSalesObject;

/**
 *
 * 월 매출 테이블
 *
 * */
public class TBL_MONTH_SALES extends TABLE<MonthSalesObject> {
    private String TAG = TBL_MONTH_SALES.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_month_sales";

    /*** Column 이름 정의 **********************/
    public static final String ID					= "_id";
    public static final String SELL_MONTH		    = "sell_month";                //월
    public static final String SELL_ALL			    = "sell_all";             // 총매출
    public static final String SELL_CARD			= "sell_card";            // 카드매충
    public static final String SELL_CASH			= "sell_cash";            // 현금매출

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                ID						+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SELL_MONTH			    + " INTEGER," +
                SELL_ALL				+ " INTEGER," +
                SELL_CARD			    + " INTEGER," +
                SELL_CASH			    + " INTEGER " +
                ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, SELL_MONTH, SELL_ALL, SELL_CARD, SELL_CASH;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.SELL_MONTH              = c.getColumnIndex(SELL_MONTH);
        idx.SELL_ALL                = c.getColumnIndex(SELL_ALL);
        idx.SELL_CARD               = c.getColumnIndex(SELL_CARD);
        idx.SELL_CASH               = c.getColumnIndex(SELL_CASH);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_MONTH_SALES(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

//    long parseLong(String s) {
//        try {
//            return Long.parseLong(s);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return 0;
//    }

    /**
     * 오브젝트를 SalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(MonthSalesObject o) {
        ContentValues values = new ContentValues();

        values.put(ID,                      o.getId());
        values.put(SELL_MONTH,              o.getSell_month());
        values.put(SELL_ALL,                o.getSell_all());
        values.put(SELL_CARD,               o.getSell_card());
        values.put(SELL_CASH,               o.getSell_cash());

        return values;
    }

    @Override
    public List<MonthSalesObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<MonthSalesObject> list = new ArrayList<MonthSalesObject>();
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
    public MonthSalesObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        MonthSalesObject o = new MonthSalesObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.SELL_MONTH != -1) o.setSell_month(c.getLong(idx.SELL_MONTH));
        if (idx.SELL_ALL != -1) o.setSell_all(c.getLong(idx.SELL_ALL));
        if (idx.SELL_CARD != -1) o.setSell_card(c.getLong(idx.SELL_CARD));
        if (idx.SELL_CASH != -1) o.setSell_cash(c.getLong(idx.SELL_CASH));

        return o;
    }

    public void insert(List<MonthSalesObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (MonthSalesObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<MonthSalesObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (MonthSalesObject contact : addList) {
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

    public List<MonthSalesObject> getList() {
        return select(null, null, null, null, null);
    }
//
//    public int deleteForSync(List<ContactObject> delList) {
//        try {
//            int count = 0;
//            db.beginTransaction();
//            for (ContactObject contact : delList) {
//                int n = delete(OEM_CONTACT_ID + "=" + contact.getCID(), null);
//                if (n != -1) {
//                    count += n;
//                }
//            }
//            db.setTransactionSuccessful();
//
//            Log.d(TABLE_NAME, "deleteForSync() - count: " + count);
//            return count;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            db.endTransaction();
//        }
//
//        Log.d(TABLE_NAME, "deleteForSync() - count: " + -1);
//
//        return -1;
//    }
//
//    public int updateForSync(List<ContactObject> modList) {
//        try {
//            int count = -1;
//            db.beginTransaction();
//            for (ContactObject contact : modList) {
//                if (update(contact, OEM_CONTACT_ID + "=" + contact.getCID(), null)) {
//                    count++;
//                }
//            }
//            db.setTransactionSuccessful();
//            Log.d(TABLE_NAME, "updateForSync() - count: " + count);
//            return count;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            db.endTransaction();
//        }
//
//        Log.d(TABLE_NAME, "updateForSync() - count: " + -1);
//
//        return -1;
//    }

//    public int updateForSyncFromServer(List<JSScsContactValue> syncList) {
//        try {
//            int count = 0;
//            db.beginTransaction();
//            for (JSScsContactValue contact : syncList) {
//                JSProfileValue profile = contact.getProfile();
//                ContentValues values = new ContentValues();
//
//                //values.put(OEM_CONTACT_ID,      o.getCID());
//                //values.put(E164,                contact.getPhoneNumber());
//                //values.put(PHONE_NUMBER,        o.getNumber());
//                //values.put(DISPLAY_NAME,        o.getDisplayName());
//                values.put(IUID,                contact.getIuid());
//                values.put(NICKNAME,            profile.getNickName());
//                values.put(STATUS_MESSAGE,      profile.getStatusMessage());
//                values.put(PRESENCE,            contact.getPresence());
//                values.put(PROFILE_IMAGE_NO,    profile.getDefaultImg());
//                //values.put(PROFILE_COLOR_NO,    o.getColorImg());
//                values.put(PROFILE_TAG,         contact.getProfileTag());
//                //values.put(VERSION,             o.getVersion());
//                //values.put(FAVORITES,           o.getFavorites());
//                //values.put(RECOMMEND,           o.getRecommend());
//                //values.put(MAIN_PICTURE_PICNO,  o.getMainPicturePicno());
//                //values.put(PICNO,               o.getPicno());
//                //values.put(PPICID,              o.getPpicid());
//                values.put(THUMBNAIL,           profile.getThumbUrl());
//                values.put(IMAGE,               profile.getImageUrl());
//                values.put(FRIEND_TYPE,         contact.getFriendType());
//
//                if (db.update(tableName, values, E164 + "=?", new String[] { contact.getProfile().getE164() }) != 0) {
//                    count++;
//                }
//            }
//
//            db.setTransactionSuccessful();
//            Log.d(TABLE_NAME, "updateForSyncFromServer() - count: " + count);
//            return count;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            db.endTransaction();
//        }
//
//        Log.d(TABLE_NAME, "updateForSyncFromServer() - count: " + -1);
//
//        return -1;
//    }

//    public List<SalesObject> getContactList(ArrayList<Long> list) {
//        String where = IUID + " " + getInString(list);
//        return select(null, where,null,null,null);
//    }
//
//    public SalesObject getContact(long iuid) {
//        String where = IUID + "=" + iuid;
//        List<SalesObject> list = select(null,where,null,null,null);
//        if (list.size() > 0) {
//            return list.get(0);
//        }
//
//        return null;
//    }

    private String getInString(List<Long> delList) {
        StringBuffer buffer_select = new StringBuffer("in (");
        for (int i=0;i<delList.size();i++) {
            buffer_select.append(delList.get(i));
            if (i == delList.size() - 1) {
                buffer_select.append(")");
            } else {
                buffer_select.append(",");
            }
        }
        return buffer_select.toString();
    }

//    public List<SalesObject> getFavoriteList() {
//        String where = FAVORITES + " = 1";
//        return select(null, where,null,null,null);
//    }
//
//    public void setFavoriteListForContact(List<SalesObject> list) {
//        ArrayList<Long> fLIst = new ArrayList<>();
//        for (SalesObject contact : list) {
////            fLIst.add(contact.getIUid());
//        }
//
//        String sql = "UPDATE " + tableName + " SET " + FAVORITES + " = 1 where " + IUID + " " + getInString(fLIst);
//        Log.v(TAG, sql);
//        execSQL(sql);
//    }
//
//    public void removeFavoriteListForContact(List<SalesObject> list) {
//        ArrayList<Long> fLIst = new ArrayList<>();
//
//
//        String sql = "UPDATE " + tableName + " SET " + FAVORITES + " = 0 where " + IUID + " " + getInString(fLIst);
//        Log.v(TAG, sql);
//        execSQL(sql);
//    }
//
//    public void setFavoriteListForIUID(List<Long> delList) {
//        String sql = "UPDATE " + tableName + " SET " + FAVORITES + " = 1 where " + IUID + " " + getInString(delList);
//        Log.v(TAG, sql);
//        execSQL(sql);
//    }
//
//    public void removeFavoriteListForIUID(List<Long> delList) {
//        String sql = "UPDATE " + tableName + " SET " + FAVORITES + " = 0 where " + IUID + " " + getInString(delList);
//        Log.v(TAG, sql);
//        execSQL(sql);
//    }
//
//    // 친구 상태인 것만 연락처 조회
//    public List<SalesObject> getFriendContactList() {
//        String where = FRIEND_STATUS + " = 3";
//        return select(null, where,null,null,null);
//    }
//
//    // 친구 상태가 아닌 연락처 조회
//    public List<SalesObject> getNotFriendContactList() {
//        String where = FRIEND_STATUS + " != 3";
//        return select(null, where,null,null,null);
//    }
//
//    // 메인화면 (친구요청중 // 친구인) 연락처 조회
//    public List<SalesObject> getMainFriendContactList() {
//        String where = FRIEND_STATUS + " = 1" + " OR " + FRIEND_STATUS + " = 3";
//        return select(null, where,null,null,null);
//    }

    // 친구 정보 업데이트
//    public boolean updateContact(SalesObject contact) {
//        String where = E164 + "='" + contact.getE164Number() + "'";
//        return update(contact, where, null);
//    }

//    // 친구 상태 업데이트
//    public boolean updateFriendStatus(String mdn, int friendStatus) {
//        Log.d(TABLE_NAME, "updateFriendStatus. mdn: " + mdn + ", friendStatus: " + friendStatus);
//
//        String sql = "UPDATE " + TABLE_NAME
//                + " SET "
//                + FRIEND_STATUS + "=" + friendStatus
//                + " WHERE " + E164 + "='" + mdn + "'";
//
//        boolean ret = update_raw(sql);
//        Log.d(TABLE_NAME, "updateFriendStatus. ret: " + ret);
//
//        return ret;
//    }

}
