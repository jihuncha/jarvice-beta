package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;
import java.util.List;


public class WeeklySalesObject implements Serializable {
    private String TAG = WeeklySalesObject.class.getSimpleName();

    private long id;
    private String sellYear;
    private String sellWeek;
    private String startWeek;
    private String endWeek;
    private String sellReal;

    public WeeklySalesObject(){}

    public WeeklySalesObject(long id, String sellYear, String sellWeek, String startWeek, String endWeek, String sellReal) {
        this.id = id;
        this.sellYear = sellYear;
        this.sellWeek = sellWeek;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.sellReal = sellReal;
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

    @Override
    public String toString() {
        return "WeeklySalesObject{" +
                "id=" + id +
                ", sellYear='" + sellYear + '\'' +
                ", sellWeek='" + sellWeek + '\'' +
                ", startWeek='" + startWeek + '\'' +
                ", endWeek='" + endWeek + '\'' +
                ", sellReal='" + sellReal + '\'' +
                '}';
    }

}
