package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;


/**
 * 월간 매출 통계 - 서버 데이터
 *
 *
 *
 * **/
public class MonthSalesObject implements Serializable {
    private String TAG = MonthSalesObject.class.getSimpleName();

    private long id;
    private String sellYear;
    private String sellMonth;
    private String sellReal;
    private String sellCard;
    private String sellCash;

    public MonthSalesObject(){}

    public MonthSalesObject(long id, String sellYear, String sellMonth, String sellReal, String sellCard, String sellCash) {
        this.id = id;
        this.sellYear = sellYear;
        this.sellMonth = sellMonth;
        this.sellReal = sellReal;
        this.sellCard = sellCard;
        this.sellCash = sellCash;
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

    public String getSellMonth() {
        return sellMonth;
    }

    public void setSellMonth(String sellMonth) {
        this.sellMonth = sellMonth;
    }

    public String getSellReal() {
        return sellReal;
    }

    public void setSellReal(String sellReal) {
        this.sellReal = sellReal;
    }

    public String getSellCard() {
        return sellCard;
    }

    public void setSellCard(String sellCard) {
        this.sellCard = sellCard;
    }

    public String getSellCash() {
        return sellCash;
    }

    public void setSellCash(String sellCash) {
        this.sellCash = sellCash;
    }

    @Override
    public String toString() {
        return "MonthSalesObject{" +
                "id=" + id +
                ", sellYear='" + sellYear + '\'' +
                ", sellWeek='" + sellMonth + '\'' +
                ", sellReal='" + sellReal + '\'' +
                ", sellCard='" + sellCard + '\'' +
                ", sellCash='" + sellCash + '\'' +
                '}';
    }
}
