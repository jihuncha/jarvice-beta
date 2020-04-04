package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;


/**
 * 주간 매출 통계 - 서버 데이터
 *
 *
 *
 * **/
public class WeeklySalesObject implements Serializable {
    private String TAG = WeeklySalesObject.class.getSimpleName();

    private long id;
    private String sellYear;
    private String sellWeek;
    private String startWeek;
    private String endWeek;
    private String sellReal;
    private String sellFood;
    private String sellBear;
    private String sellCock;
    private String sellLiquor;
    private String sellDrink;

    public WeeklySalesObject(){}

    public WeeklySalesObject(long id, String sellYear, String sellWeek, String startWeek,
                             String endWeek, String sellReal, String sellFood, String sellBear,
                             String sellCock, String sellLiquor, String sellDrink) {
        this.id = id;
        this.sellYear = sellYear;
        this.sellWeek = sellWeek;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.sellReal = sellReal;
        this.sellFood = sellFood;
        this.sellBear = sellBear;
        this.sellCock = sellCock;
        this.sellLiquor = sellLiquor;
        this.sellDrink = sellDrink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSellYear() {
        return sellYear;
    }

    public void setSellYear(String sellYear) {
        this.sellYear = sellYear;
    }

    public String getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(String startWeek) {
        this.startWeek = startWeek;
    }

    public String getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(String endWeek) {
        this.endWeek = endWeek;
    }

    public String getSellReal() {
        return sellReal;
    }

    public void setSellReal(String sellReal) {
        this.sellReal = sellReal;
    }

    public String getSellWeek() {
        return sellWeek;
    }

    public void setSellWeek(String sellWeek) {
        this.sellWeek = sellWeek;
    }

    public String getSellFood() {
        return sellFood;
    }

    public void setSellFood(String sellFood) {
        this.sellFood = sellFood;
    }

    public String getSellBear() {
        return sellBear;
    }

    public void setSellBear(String sellBear) {
        this.sellBear = sellBear;
    }

    public String getSellCock() {
        return sellCock;
    }

    public void setSellCock(String sellCock) {
        this.sellCock = sellCock;
    }

    public String getSellLiquor() {
        return sellLiquor;
    }

    public void setSellLiquor(String sellLiquor) {
        this.sellLiquor = sellLiquor;
    }

    public String getSellDrink() {
        return sellDrink;
    }

    public void setSellDrink(String sellDrink) {
        this.sellDrink = sellDrink;
    }

    @Override
    public String toString() {
        return "WeeklySalesObject{" +
                "id=" + id +
                ", sellYear='" + sellYear + '\'' +
                ", sellWeek='" + sellWeek + '\'' +
                ", startWeek='" + startWeek + '\'' +
                ", endWeek='" + endWeek + '\'' +
                ", sellReal='" + sellReal + '\'' +
                ", sellFood='" + sellFood + '\'' +
                ", sellBear='" + sellBear + '\'' +
                ", sellCock='" + sellCock + '\'' +
                ", sellLiquor='" + sellLiquor + '\'' +
                ", sellDrink='" + sellDrink + '\'' +
                '}';
    }
}
