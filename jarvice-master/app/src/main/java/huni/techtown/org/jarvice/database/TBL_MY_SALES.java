package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;

/**
 *
 * 원본 데이터
 *
 * */
public class TBL_MY_SALES extends TABLE<SalesObject> {
    private String TAG = TBL_MY_SALES.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_my_sales";

    /*** Column 이름 정의 **********************/
    public static final String ID					= "_id";
    public static final String SELL_DATE		    = "sell_date";              // 날짜
    public static final String RECEIPT_NO			= "receipt_no";             // 영수증번호
    public static final String SORT			        = "sort";                   // 구분
    public static final String TABLE_NO			    = "table_no";               // 테이블명
    public static final String FIRST_ORDER			= "first_order";            // 최초주문
    public static final String PAYMENT_TIME			= "payment_time";           // 결제시각
    public static final String PRODUCT_CODE		    = "product_code";           // 상품코드
    public static final String BARCODE				= "barcode";                // 바코드
    public static final String PRODUCT_NAME		    = "product_name";           // 상품명
    public static final String PRODUCT_COUNT	    = "product_count";          // 수량
    public static final String SELL			        = "sell";                   // 총매출액
    public static final String DISCOUNT				= "discount";               // 할인액
    public static final String DISCOUNT_TYPE	    = "discount_type";          // 할인구분
    public static final String REAL_SALES			= "real_sales";             // 실매출액
    public static final String PLUS_SALES	        = "plus_sales";             // 가액
    public static final String VAT				    = "vat";                    // 부가세
    public static final String CATEGORY             = "category";               // 상품 분류
    public static final String DAY                  = "day";                    // 요일

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
            "(" +
                ID						+ " INTEGER NOT NULL UNIQUE PRIMARY KEY," +
                SELL_DATE			    + " TEXT," +
                RECEIPT_NO				+ " TEXT," +
                SORT			        + " TEXT," +
                TABLE_NO			    + " TEXT," +
                FIRST_ORDER				+ " TEXT," +
                PAYMENT_TIME			+ " TEXT," +
                PRODUCT_CODE			+ " INTEGER," +
                BARCODE				    + " TEXT," +
                PRODUCT_NAME		    + " TEXT," +
                PRODUCT_COUNT		    + " INTEGER," +
                SELL				    + " INTEGER," +
                DISCOUNT				+ " INTEGER DEFAULT 0," +
                DISCOUNT_TYPE			+ " TEXT," +
                REAL_SALES				+ " INTEGER," +
                PLUS_SALES		        + " INTEGER," +
                VAT					    + " INTEGER," +
                CATEGORY                + " TEXT," +
                DAY                     + " TEXT " +
                ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, SELL_DATE, RECEIPT_NO, SORT, TABLE_NO, FIRST_ORDER,
                PAYMENT_TIME, PRODUCT_CODE, BARCODE, PRODUCT_NAME, PRODUCT_COUNT,
                SELL, DISCOUNT, DISCOUNT_TYPE, REAL_SALES, PLUS_SALES, VAT,
                CATEGORY, DAY;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.SELL_DATE               = c.getColumnIndex(SELL_DATE);
        idx.RECEIPT_NO              = c.getColumnIndex(RECEIPT_NO);
        idx.SORT                    = c.getColumnIndex(SORT);
        idx.TABLE_NO                = c.getColumnIndex(TABLE_NO);
        idx.FIRST_ORDER             = c.getColumnIndex(FIRST_ORDER);
        idx.PAYMENT_TIME            = c.getColumnIndex(PAYMENT_TIME);
        idx.PRODUCT_CODE            = c.getColumnIndex(PRODUCT_CODE);
        idx.BARCODE                 = c.getColumnIndex(BARCODE);
        idx.PRODUCT_NAME            = c.getColumnIndex(PRODUCT_NAME);
        idx.PRODUCT_COUNT           = c.getColumnIndex(PRODUCT_COUNT);
        idx.SELL                    = c.getColumnIndex(SELL);
        idx.DISCOUNT                = c.getColumnIndex(DISCOUNT);
        idx.DISCOUNT_TYPE           = c.getColumnIndex(DISCOUNT_TYPE);
        idx.REAL_SALES              = c.getColumnIndex(REAL_SALES);
        idx.PLUS_SALES              = c.getColumnIndex(PLUS_SALES);
        idx.VAT                     = c.getColumnIndex(VAT);
        idx.CATEGORY                = c.getColumnIndex(CATEGORY);
        idx.DAY                     = c.getColumnIndex(DAY);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_MY_SALES(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

    /**
     * 오브젝트를 SalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(SalesObject o) {
        ContentValues values = new ContentValues();

        values.put(ID,                      o.getId());
        values.put(SELL_DATE,               o.getSellDate());
        values.put(RECEIPT_NO,              o.getReceiptNo());
        values.put(SORT,                    o.getSort());
        values.put(TABLE_NO,                o.getTableNo());
        values.put(FIRST_ORDER,             o.getFirstOrder());
        values.put(PAYMENT_TIME,            o.getPaymentTime());
        values.put(PRODUCT_CODE,            o.getProductCode());
        values.put(BARCODE,                 o.getBarcode());
        values.put(PRODUCT_NAME,            o.getProductName());
        values.put(PRODUCT_COUNT,           o.getProductCount());
        values.put(SELL,                    o.getSell());
        values.put(DISCOUNT,                o.getDiscount());
        values.put(DISCOUNT_TYPE,           o.getDiscountType());
        values.put(REAL_SALES,              o.getRealSales());
        values.put(PLUS_SALES,              o.getPlusSales());
        values.put(VAT,                     o.getVat());
        values.put(CATEGORY,                o.getCategory());
        values.put(DAY,                     o.getDay());

        return values;
    }

    @Override
    public List<SalesObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<SalesObject> list = new ArrayList<SalesObject>();
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
    public SalesObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        SalesObject o = new SalesObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.SELL_DATE != -1) o.setSellDate(c.getString(idx.SELL_DATE));
        if (idx.RECEIPT_NO != -1) o.setReceiptNo(c.getString(idx.RECEIPT_NO));
        if (idx.SORT != -1) o.setSort(c.getString(idx.SORT));
        if (idx.TABLE_NO != -1) o.setTableNo(c.getString(idx.TABLE_NO));
        if (idx.FIRST_ORDER != -1) o.setFirstOrder(c.getString(idx.FIRST_ORDER));
        if (idx.PAYMENT_TIME != -1) o.setPaymentTime(c.getString(idx.PAYMENT_TIME));
        if (idx.PRODUCT_CODE != -1) o.setProductCode(c.getString(idx.PRODUCT_CODE));
        if (idx.BARCODE != -1) o.setBarcode(c.getString(idx.BARCODE));
        if (idx.PRODUCT_NAME != -1) o.setProductName(c.getString(idx.PRODUCT_NAME));
        if (idx.PRODUCT_COUNT != -1) o.setProductCount(c.getString(idx.PRODUCT_COUNT));
        if (idx.SELL != -1) o.setSell(c.getString(idx.SELL));
        if (idx.DISCOUNT != -1) o.setDiscount(c.getString(idx.DISCOUNT));
        if (idx.DISCOUNT_TYPE != -1) o.setDiscountType(c.getString(idx.DISCOUNT_TYPE));
        if (idx.REAL_SALES != -1) o.setRealSales(c.getString(idx.REAL_SALES));
        if (idx.PLUS_SALES != -1) o.setPlusSales(c.getString(idx.PLUS_SALES));
        if (idx.VAT != -1) o.setVat(c.getString(idx.VAT));
        if (idx.CATEGORY != -1) o.setCategory(c.getString(idx.CATEGORY));
        if (idx.DAY != -1) o.setDay(c.getString(idx.DAY));

        return o;
    }

    public void insert(List<SalesObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (SalesObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<SalesObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (SalesObject contact : addList) {
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

    public List<SalesObject> getList() {
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

    public int getMemberCount(String sellDate) {
        String whereClause = SELL_DATE + "=" + sellDate;
        return getCount(whereClause, null);
    }

    public int getSum(String sellDate) {
        String whereClause = SELL_DATE + "=" + sellDate;
        return getSum(whereClause, null);
    }

    public List<SalesObject> getDailyData (String sellDate) {
        String whereClause = SELL_DATE + "=" + sellDate;
        return select_raw(whereClause);
    }
}
