package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.DailySalesObject;

/**
 *
 * 원본 데이터
 *
 * */
public class TBL_DAILY_SALES extends TABLE<DailySalesObject> {
    private String TAG = TBL_DAILY_SALES.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_daily_sales";

    /*** Column 이름 정의 **********************/
    public static final String ID					            = "_id";
    public static final String SELL_DATE		                = "sell_date";                  // 날짜
    public static final String SELL_DAY_OF_WEEK		            = "sell_day_of_week";           // 요일
    public static final String SELL_WEEK		                = "sell_week";                  // 몇번쨰주?
    public static final String SELL_ALL			                = "sell_all";                   // 전체매출
    public static final String SELL_REAL			            = "sell_real";                  // 전체 실 매출
    public static final String SELL_FOOD			            = "sell_food";                  // 음식 판매 금액
    public static final String SELL_FOOD_PERCENT			    = "sell_food_percent";          // 음식 판매 퍼센트
    public static final String SELL_FOOD_PRODUCT			    = "sell_food_product";          // 음식 판매 갯수
    public static final String SELL_BEER			            = "sell_beer";                  // 주류 판매 금액
    public static final String SELL_BEER_PERCENT			    = "sell_beer_percent";          // 주류 판매 퍼센트
    public static final String SELL_BEER_PRODUCT			    = "sell_beer_product";          // 주류 판매 갯수
    public static final String SELL_COCK			            = "sell_cock";                  // 칵테일 판매 금액
    public static final String SELL_COCK_PERCENT			    = "sell_cock_percent";          // 칵테일 판매 퍼센트
    public static final String SELL_COCK_PRODUCT			    = "sell_cock_product";          // 칵테일 판매 갯수
    public static final String SELL_LIQUOR			            = "sell_liquor";                // 양주 판매 금액
    public static final String SELL_LIQUOR_PERCENT			    = "sell_liquor_percent";        // 양주 판매 퍼센트
    public static final String SELL_LIQUOR_PRODUCT			    = "sell_liquor_product";        // 양주 판매 갯수
    public static final String SELL_DRINK			            = "sell_drink";                 // 음료 판매 금액
    public static final String SELL_DRINK_PERCENT			    = "sell_drink_percent";         // 음료 판매 퍼센트
    public static final String SELL_DRINK_PRODUCT			    = "sell_drink_product";         // 음료 판매 갯수
    public static final String SELL_LUNCH			            = "sell_lunch";                 // 점심 판매 금액
    public static final String SELL_LUNCH_PERCENT			    = "sell_lunch_percent";         // 점심 판매 퍼센트
    public static final String SELL_DELIVERY			        = "sell_delivery";              // 배달 판매 금액
    public static final String SELL_DELIVERY_PERCENT			= "sell_delivery_percent";      // 배달 판매 퍼센트
    public static final String DINNER_VISIT_FIVE			    = "dinner_visit_five";          // 오후 5-6시 방문자
    public static final String DINNER_VISIT_SIX			        = "dinner_visit_six";           // 오후 6-7시 방문자
    public static final String DINNER_VISIT_SEVEN			    = "dinner_visit_seven";         // 오후 7-8시 방문자
    public static final String DINNER_VISIT_EIGHT			    = "dinner_visit_eight";         // 오후 8-9시 방문자
    public static final String DINNER_VISIT_NINE			    = "dinner_visit_nine";          // 오후 9-10시 방문자
    public static final String DINNER_VISIT_TEN			        = "dinner_visit_ten";           // 오후 10-11시 방문자
    public static final String DINNER_VISIT_ELEVEN			    = "dinner_visit_eleven";        // 오후 11-12시 방문자
    public static final String DINNER_VISIT_TWELVE			    = "dinner_visit_twelve";        // 새벽 12-1시 방문자
    public static final String DINNER_VISIT_ONE			        = "dinner_visit_one";           // 새벽 1-2시 방문자
    public static final String DINNER_VISIT_TOTAL		        = "dinner_visit_total";         // 오후 방문자 전체

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
            "(" +
                ID						+ " INTEGER NOT NULL UNIQUE PRIMARY KEY," +
                SELL_DATE			    + " TEXT," +
                SELL_DAY_OF_WEEK	    + " TEXT," +
                SELL_WEEK	            + " TEXT," +
                SELL_ALL				+ " TEXT," +
                SELL_REAL			    + " TEXT," +
                SELL_FOOD			    + " TEXT," +
                SELL_FOOD_PERCENT	    + " TEXT," +
                SELL_FOOD_PRODUCT		+ " TEXT," +
                SELL_BEER			    + " TEXT," +
                SELL_BEER_PERCENT	    + " TEXT," +
                SELL_BEER_PRODUCT		+ " TEXT," +
                SELL_COCK		        + " TEXT," +
                SELL_COCK_PERCENT		+ " TEXT," +
                SELL_COCK_PRODUCT		+ " TEXT," +
                SELL_LIQUOR				+ " TEXT," +
                SELL_LIQUOR_PERCENT		+ " TEXT," +
                SELL_LIQUOR_PRODUCT     + " TEXT," +
                SELL_DRINK              + " TEXT," +
                SELL_DRINK_PERCENT      + " TEXT," +
                SELL_DRINK_PRODUCT      + " TEXT," +
                SELL_LUNCH              + " TEXT," +
                SELL_LUNCH_PERCENT      + " TEXT, "+
                SELL_DELIVERY           + " TEXT," +
                SELL_DELIVERY_PERCENT   + " TEXT," +
                DINNER_VISIT_FIVE       + " TEXT," +
                DINNER_VISIT_SIX        + " TEXT," +
                DINNER_VISIT_SEVEN      + " TEXT," +
                DINNER_VISIT_EIGHT      + " TEXT," +
                DINNER_VISIT_NINE       + " TEXT," +
                DINNER_VISIT_TEN        + " TEXT," +
                DINNER_VISIT_ELEVEN     + " TEXT," +
                DINNER_VISIT_TWELVE     + " TEXT," +
                DINNER_VISIT_ONE        + " TEXT," +
                DINNER_VISIT_TOTAL      + " TEXT " +
                ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, SELL_DATE, SELL_DAY_OF_WEEK, SELL_WEEK,  SELL_ALL, SELL_REAL, SELL_FOOD, SELL_FOOD_PERCENT,
                SELL_FOOD_PRODUCT, SELL_BEER, SELL_BEER_PERCENT, SELL_BEER_PRODUCT, SELL_COCK,
                SELL_COCK_PERCENT, SELL_COCK_PRODUCT, SELL_LIQUOR, SELL_LIQUOR_PERCENT, SELL_LIQUOR_PRODUCT,
                SELL_DRINK, SELL_DRINK_PERCENT, SELL_DRINK_PRODUCT, SELL_LUNCH, SELL_LUNCH_PERCENT,
                SELL_DELIVERY, SELL_DELIVERY_PERCENT, DINNER_VISIT_FIVE, DINNER_VISIT_SIX, DINNER_VISIT_SEVEN,
                DINNER_VISIT_EIGHT, DINNER_VISIT_NINE, DINNER_VISIT_TEN, DINNER_VISIT_ELEVEN, DINNER_VISIT_TWELVE,
                DINNER_VISIT_ONE, DINNER_VISIT_TOTAL;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.SELL_DATE               = c.getColumnIndex(SELL_DATE);
        idx.SELL_DAY_OF_WEEK        = c.getColumnIndex(SELL_DAY_OF_WEEK);
        idx.SELL_WEEK               = c.getColumnIndex(SELL_WEEK);
        idx.SELL_ALL                = c.getColumnIndex(SELL_ALL);
        idx.SELL_REAL               = c.getColumnIndex(SELL_REAL);
        idx.SELL_FOOD               = c.getColumnIndex(SELL_FOOD);
        idx.SELL_FOOD_PERCENT       = c.getColumnIndex(SELL_FOOD_PERCENT);
        idx.SELL_FOOD_PRODUCT       = c.getColumnIndex(SELL_FOOD_PRODUCT);
        idx.SELL_BEER               = c.getColumnIndex(SELL_BEER);
        idx.SELL_BEER_PERCENT       = c.getColumnIndex(SELL_BEER_PERCENT);
        idx.SELL_BEER_PRODUCT       = c.getColumnIndex(SELL_BEER_PRODUCT);
        idx.SELL_COCK               = c.getColumnIndex(SELL_COCK);
        idx.SELL_COCK_PERCENT       = c.getColumnIndex(SELL_COCK_PERCENT);
        idx.SELL_COCK_PRODUCT       = c.getColumnIndex(SELL_COCK_PRODUCT);
        idx.SELL_LIQUOR             = c.getColumnIndex(SELL_LIQUOR);
        idx.SELL_LIQUOR_PERCENT     = c.getColumnIndex(SELL_LIQUOR_PERCENT);
        idx.SELL_LIQUOR_PRODUCT     = c.getColumnIndex(SELL_LIQUOR_PRODUCT);
        idx.SELL_DRINK              = c.getColumnIndex(SELL_DRINK);
        idx.SELL_DRINK_PERCENT      = c.getColumnIndex(SELL_DRINK_PERCENT);
        idx.SELL_DRINK_PRODUCT      = c.getColumnIndex(SELL_DRINK_PRODUCT);
        idx.SELL_LUNCH              = c.getColumnIndex(SELL_LUNCH);
        idx.SELL_LUNCH_PERCENT      = c.getColumnIndex(SELL_LUNCH_PERCENT);
        idx.SELL_DELIVERY           = c.getColumnIndex(SELL_DELIVERY);
        idx.SELL_DELIVERY_PERCENT   = c.getColumnIndex(SELL_DELIVERY_PERCENT);
        idx.DINNER_VISIT_FIVE       = c.getColumnIndex(DINNER_VISIT_FIVE);
        idx.DINNER_VISIT_SIX        = c.getColumnIndex(DINNER_VISIT_SIX);
        idx.DINNER_VISIT_SEVEN      = c.getColumnIndex(DINNER_VISIT_SEVEN);
        idx.DINNER_VISIT_EIGHT      = c.getColumnIndex(DINNER_VISIT_EIGHT);
        idx.DINNER_VISIT_NINE       = c.getColumnIndex(DINNER_VISIT_NINE);
        idx.DINNER_VISIT_TEN        = c.getColumnIndex(DINNER_VISIT_TEN);
        idx.DINNER_VISIT_ELEVEN     = c.getColumnIndex(DINNER_VISIT_ELEVEN);
        idx.DINNER_VISIT_TWELVE     = c.getColumnIndex(DINNER_VISIT_TWELVE);
        idx.DINNER_VISIT_ONE        = c.getColumnIndex(DINNER_VISIT_ONE);
        idx.DINNER_VISIT_TOTAL      = c.getColumnIndex(DINNER_VISIT_TOTAL);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_DAILY_SALES(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

    /**
     * 오브젝트를 DailySalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(DailySalesObject o) {
        ContentValues values = new ContentValues();

        values.put(ID,                      o.getId());
        values.put(SELL_DATE,               o.getSellDate());
        values.put(SELL_DAY_OF_WEEK,        o.getSellDayOfWeek());
        values.put(SELL_WEEK,               o.getSellWeek());
        values.put(SELL_ALL,                o.getSellAll());
        values.put(SELL_REAL,               o.getSellReal());
        values.put(SELL_FOOD,               o.getSellFood());
        values.put(SELL_FOOD_PERCENT,       o.getSellFoodPercent());
        values.put(SELL_FOOD_PRODUCT,       o.getSellFoodProduct());
        values.put(SELL_BEER,               o.getSellBeer());
        values.put(SELL_BEER_PERCENT,       o.getSellBeerPercent());
        values.put(SELL_BEER_PRODUCT,       o.getSellBeerProduct());
        values.put(SELL_COCK,               o.getSellCock());
        values.put(SELL_COCK_PERCENT,       o.getSellCockPercent());
        values.put(SELL_COCK_PRODUCT,       o.getSellCockProduct());
        values.put(SELL_LIQUOR,             o.getSellLiquor());
        values.put(SELL_LIQUOR_PERCENT,     o.getSellLiquorPercent());
        values.put(SELL_LIQUOR_PRODUCT,     o.getSellLiquorProduct());
        values.put(SELL_DRINK,              o.getSellDrink());
        values.put(SELL_DRINK_PERCENT,      o.getSellDrinkPercent());
        values.put(SELL_DRINK_PRODUCT,      o.getSellDrinkProduct());
        values.put(SELL_LUNCH,              o.getSellLunch());
        values.put(SELL_LUNCH_PERCENT,      o.getSellLunchPercent());
        values.put(SELL_DELIVERY,           o.getSellDelivery());
        values.put(SELL_DELIVERY_PERCENT,   o.getSellDeliveryPercent());
        values.put(DINNER_VISIT_FIVE,       o.getDinnerVisitFive());
        values.put(DINNER_VISIT_SIX,        o.getDinnerVisitSix());
        values.put(DINNER_VISIT_SEVEN,      o.getDinnerVisitSeven());
        values.put(DINNER_VISIT_EIGHT,      o.getDinnerVisitEight());
        values.put(DINNER_VISIT_NINE,       o.getDinnerVisitNine());
        values.put(DINNER_VISIT_TEN,        o.getDinnerVisitTen());
        values.put(DINNER_VISIT_ELEVEN,     o.getDinnerVisitEleven());
        values.put(DINNER_VISIT_TWELVE,     o.getDinnerVisitTwelve());
        values.put(DINNER_VISIT_ONE,        o.getDinnerVisitOne());
        values.put(DINNER_VISIT_TOTAL,      o.getDinnerVisitTotal());

        return values;
    }

    @Override
    public List<DailySalesObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<DailySalesObject> list = new ArrayList<DailySalesObject>();
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
    public DailySalesObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        DailySalesObject o = new DailySalesObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.SELL_DATE != -1) o.setSellDate(c.getString(idx.SELL_DATE));
        if (idx.SELL_DAY_OF_WEEK != -1) o.setSellDayOfWeek(c.getString(idx.SELL_DAY_OF_WEEK));
        if (idx.SELL_WEEK != -1) o.setSellWeek(c.getString(idx.SELL_WEEK));
        if (idx.SELL_ALL != -1) o.setSellAll(c.getString(idx.SELL_ALL));
        if (idx.SELL_REAL != -1) o.setSellReal(c.getString(idx.SELL_REAL));
        if (idx.SELL_FOOD != -1) o.setSellFood(c.getString(idx.SELL_FOOD));
        if (idx.SELL_FOOD_PERCENT != -1) o.setSellFoodPercent(c.getString(idx.SELL_FOOD_PERCENT));
        if (idx.SELL_FOOD_PRODUCT != -1) o.setSellFoodProduct(c.getString(idx.SELL_FOOD_PRODUCT));
        if (idx.SELL_BEER != -1) o.setSellBeer(c.getString(idx.SELL_BEER));
        if (idx.SELL_BEER_PERCENT != -1) o.setSellBeerPercent(c.getString(idx.SELL_BEER_PERCENT));
        if (idx.SELL_BEER_PRODUCT != -1) o.setSellBeerProduct(c.getString(idx.SELL_BEER_PRODUCT));
        if (idx.SELL_COCK != -1) o.setSellCock(c.getString(idx.SELL_COCK));
        if (idx.SELL_COCK_PERCENT != -1) o.setSellCockPercent(c.getString(idx.SELL_COCK_PERCENT));
        if (idx.SELL_COCK_PRODUCT != -1) o.setSellCockProduct(c.getString(idx.SELL_COCK_PRODUCT));
        if (idx.SELL_LIQUOR != -1) o.setSellLiquor(c.getString(idx.SELL_LIQUOR));
        if (idx.SELL_LIQUOR_PERCENT != -1) o.setSellLiquorPercent(c.getString(idx.SELL_LIQUOR_PERCENT));
        if (idx.SELL_LIQUOR_PRODUCT != -1) o.setSellLiquorProduct(c.getString(idx.SELL_LIQUOR_PRODUCT));
        if (idx.SELL_DRINK != -1) o.setSellDrink(c.getString(idx.SELL_DRINK));
        if (idx.SELL_DRINK_PERCENT != -1) o.setSellDrinkPercent(c.getString(idx.SELL_DRINK_PERCENT));
        if (idx.SELL_DRINK_PRODUCT != -1) o.setSellDrinkProduct(c.getString(idx.SELL_DRINK_PRODUCT));
        if (idx.SELL_LUNCH != -1) o.setSellLunch(c.getString(idx.SELL_LUNCH));
        if (idx.SELL_LUNCH_PERCENT != -1) o.setSellLunchPercent(c.getString(idx.SELL_LUNCH_PERCENT));
        if (idx.SELL_DELIVERY != -1) o.setSellDelivery(c.getString(idx.SELL_DELIVERY));
        if (idx.SELL_DELIVERY_PERCENT != -1) o.setSellDeliveryPercent(c.getString(idx.SELL_DELIVERY_PERCENT));
        if (idx.DINNER_VISIT_FIVE != -1) o.setDinnerVisitFive(c.getString(idx.DINNER_VISIT_FIVE));
        if (idx.DINNER_VISIT_SIX != -1) o.setDinnerVisitSix(c.getString(idx.DINNER_VISIT_SIX));
        if (idx.DINNER_VISIT_SEVEN != -1) o.setDinnerVisitSeven(c.getString(idx.DINNER_VISIT_SEVEN));
        if (idx.DINNER_VISIT_EIGHT != -1) o.setDinnerVisitEight(c.getString(idx.DINNER_VISIT_EIGHT));
        if (idx.DINNER_VISIT_NINE != -1) o.setDinnerVisitNine(c.getString(idx.DINNER_VISIT_NINE));
        if (idx.DINNER_VISIT_TEN != -1) o.setDinnerVisitTen(c.getString(idx.DINNER_VISIT_TEN));
        if (idx.DINNER_VISIT_ELEVEN != -1) o.setDinnerVisitEleven(c.getString(idx.DINNER_VISIT_ELEVEN));
        if (idx.DINNER_VISIT_TWELVE != -1) o.setDinnerVisitTwelve(c.getString(idx.DINNER_VISIT_TWELVE));
        if (idx.DINNER_VISIT_ONE != -1) o.setDinnerVisitOne(c.getString(idx.DINNER_VISIT_ONE));
        if (idx.DINNER_VISIT_TOTAL != -1) o.setDinnerVisitTotal(c.getString(idx.DINNER_VISIT_TOTAL));

        return o;
    }

    public void insert(List<DailySalesObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (DailySalesObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<DailySalesObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (DailySalesObject contact : addList) {
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

    public List<DailySalesObject> getList() {
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

//    public List<DailySalesObject> getContactList(ArrayList<Long> list) {
//        String where = IUID + " " + getInString(list);
//        return select(null, where,null,null,null);
//    }
//
//    public DailySalesObject getContact(long iuid) {
//        String where = IUID + "=" + iuid;
//        List<DailySalesObject> list = select(null,where,null,null,null);
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

//    public List<DailySalesObject> getFavoriteList() {
//        String where = FAVORITES + " = 1";
//        return select(null, where,null,null,null);
//    }
//
//    public void setFavoriteListForContact(List<DailySalesObject> list) {
//        ArrayList<Long> fLIst = new ArrayList<>();
//        for (DailySalesObject contact : list) {
////            fLIst.add(contact.getIUid());
//        }
//
//        String sql = "UPDATE " + tableName + " SET " + FAVORITES + " = 1 where " + IUID + " " + getInString(fLIst);
//        Log.v(TAG, sql);
//        execSQL(sql);
//    }
//
//    public void removeFavoriteListForContact(List<DailySalesObject> list) {
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
//    public List<DailySalesObject> getFriendContactList() {
//        String where = FRIEND_STATUS + " = 3";
//        return select(null, where,null,null,null);
//    }
//
//    // 친구 상태가 아닌 연락처 조회
//    public List<DailySalesObject> getNotFriendContactList() {
//        String where = FRIEND_STATUS + " != 3";
//        return select(null, where,null,null,null);
//    }
//
//    // 메인화면 (친구요청중 // 친구인) 연락처 조회
//    public List<DailySalesObject> getMainFriendContactList() {
//        String where = FRIEND_STATUS + " = 1" + " OR " + FRIEND_STATUS + " = 3";
//        return select(null, where,null,null,null);
//    }

    // 친구 정보 업데이트
//    public boolean updateContact(DailySalesObject contact) {
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

//    public int getMemberCount(String sellDate) {
//        String whereClause = SELL_DATE + "=" + sellDate;
//        return getCount(whereClause, null);
//    }
//
//    public int getSum(String sellDate) {
//        String whereClause = SELL_DATE + "=" + sellDate;
//        return getSum(whereClause, null);
//    }

    //TODO date형태가 이상하여 LIKE 사용
    public List<DailySalesObject> getDailyData(String sellDate) {
        String whereClause = SELL_DATE + " LIKE '%" + sellDate + "%'";
        return select(null, whereClause,null,null,null);
    }

//    public List<DailySalesObject> getLastData() {
//        String whereClause =
//    }

    //id 기준으로 정렬 및 결과값이 Total 이 아닌 것으로 추출. -> 3개 추출
    public List<DailySalesObject> getLastData() {
//        String sql = "SELECT * FROM " + tableName + " WHERE " + SELL_DATE + " != '" + "Total" +  "' ORDER BY " + ID + " DESC LIMIT 1";

        String sql = "SELECT * FROM " + tableName + " WHERE " + SELL_DATE + " != '" + "Total" +  "' AND " + SELL_REAL + " != '" + "0" + "' ORDER BY " + ID + " DESC LIMIT 3";

        return select_raw(sql);
    }

    //id 기준으로 정렬 및 결과값이 Total 이 아닌 것으로 추출.
    public List<DailySalesObject> getDailyDataForAnalysis(String sellYear, String sellWeek) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + SELL_DATE + " LIKE '%" + sellYear + "%' AND " + SELL_WEEK + " = '" + sellWeek +"'";
        return select_raw(sql);
    }
}
