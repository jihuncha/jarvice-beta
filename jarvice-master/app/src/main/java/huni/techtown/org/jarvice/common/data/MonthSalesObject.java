package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;


public class MonthSalesObject implements Serializable {
    private String TAG = MonthSalesObject.class.getSimpleName();

    private long id;
    private long sell_month;
    private long sell_all;
    private long sell_card;
    private long sell_cash;

    public MonthSalesObject(){}

    public MonthSalesObject(long id, long sell_month, long sell_all, long sell_card, long sell_cash) {
        this.id = id;
        this.sell_month = sell_month;
        this.sell_all = sell_all;
        this.sell_card = sell_card;
        this.sell_cash = sell_cash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSell_month() {
        return sell_month;
    }

    public void setSell_month(long sell_month) {
        this.sell_month = sell_month;
    }

    public long getSell_all() {
        return sell_all;
    }

    public void setSell_all(long sell_all) {
        this.sell_all = sell_all;
    }

    public long getSell_card() {
        return sell_card;
    }

    public void setSell_card(long sell_card) {
        this.sell_card = sell_card;
    }

    public long getSell_cash() {
        return sell_cash;
    }

    public void setSell_cash(long sell_cash) {
        this.sell_cash = sell_cash;
    }

    @Override
    public String toString() {
        return "MonthSalesObject{" +
                "id=" + id +
                ", sell_month=" + sell_month +
                ", sell_all=" + sell_all +
                ", sell_card=" + sell_card +
                ", sell_cash=" + sell_cash +
                '}';
    }
}
