package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;
import java.util.List;

/**
 * 항목 데이터 - 서버데이터 앱에서 가공
 * 순 매출액이 0 이 아닌 카테고리
 *
 *
 * **/
public class DailySalesList implements Serializable {
    private String TAG = DailySalesList.class.getSimpleName();

    private String sellDate;
    private String categoryName;
    private String categoryRealSell;
    private String categorySellPer;
    private int color;
    private List<DailySalesListItems> itemList;

    public boolean expanded = false;

    public DailySalesList(){}

    public DailySalesList(String sellDate, String categoryName, String categoryRealSell, String categorySellPer, int color, List<DailySalesListItems> itemList) {
        this.sellDate = sellDate;
        this.categoryName = categoryName;
        this.categoryRealSell = categoryRealSell;
        this.categorySellPer = categorySellPer;
        this.color = color;
        this.itemList = itemList;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryRealSell() {
        return categoryRealSell;
    }

    public void setCategoryRealSell(String categoryRealSell) {
        this.categoryRealSell = categoryRealSell;
    }

    public String getCategorySellPer() {
        return categorySellPer;
    }

    public void setCategorySellPer(String categorySellPer) {
        this.categorySellPer = categorySellPer;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<DailySalesListItems> getItemList() {
        return itemList;
    }

    public void setItemList(List<DailySalesListItems> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "DailySalesList{" +
                "sellDate='" + sellDate + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryRealSell='" + categoryRealSell + '\'' +
                ", categorySellPer='" + categorySellPer + '\'' +
                ", color=" + color +
                ", itemList=" + itemList +
                '}';
    }
}
