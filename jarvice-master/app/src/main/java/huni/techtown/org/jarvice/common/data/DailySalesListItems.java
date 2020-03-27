package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;

/**
 * 항목 데이터 - 서버데이터 앱에서 가공
 * 순 매출액이 0 이 아닌 카테고리 의 세부 내역 (어떤 식품이 얼마나 팔린것인가??)
 *
 *
 * **/
public class DailySalesListItems implements Serializable {
    private String TAG = DailySalesListItems.class.getSimpleName();

    private String itemName;
    private String itemCount;
    private String itemRealSell;

    public DailySalesListItems() {
    }

    public DailySalesListItems(String itemName, String itemCount, String itemRealSell) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemRealSell = itemRealSell;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemRealSell() {
        return itemRealSell;
    }

    public void setItemRealSell(String itemRealSell) {
        this.itemRealSell = itemRealSell;
    }

    @Override
    public String toString() {
        return "DailySalesListItems{" +
                "itemName='" + itemName + '\'' +
                ", itemCount='" + itemCount + '\'' +
                ", itemRealSell='" + itemRealSell + '\'' +
                '}';
    }

}
