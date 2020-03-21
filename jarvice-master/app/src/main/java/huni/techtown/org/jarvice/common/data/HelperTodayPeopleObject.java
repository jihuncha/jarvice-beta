package huni.techtown.org.jarvice.common.data;


/**
 * 오늘의 사람 - 앱 데이터
 *
 *
 *
 * **/
public class HelperTodayPeopleObject {
    private String TAG = HelperTodayPeopleObject.class.getSimpleName();

    private long id;
    private String peopleDate;
    private String notiPeople;
    private String notiWork;
    private String notiTime;

    public HelperTodayPeopleObject(){}

    public HelperTodayPeopleObject(String peopleDate, String notiPeople, String notiWork, String notiTime) {
        this.peopleDate = peopleDate;
        this.notiPeople = notiPeople;
        this.notiWork = notiWork;
        this.notiTime = notiTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPeopleDate() {
        return peopleDate;
    }

    public void setPeopleDate(String peopleDate) {
        this.peopleDate = peopleDate;
    }

    public String getNotiPeople() {
        return notiPeople;
    }

    public void setNotiPeople(String notiPeople) {
        this.notiPeople = notiPeople;
    }

    public String getNotiWork() {
        return notiWork;
    }

    public void setNotiWork(String notiWork) {
        this.notiWork = notiWork;
    }

    public String getNotiTime() {
        return notiTime;
    }

    public void setNotiTime(String notiTime) {
        this.notiTime = notiTime;
    }

    @Override
    public String toString() {
        return "HelperTodayPeopleObject{" +
                "id=" + id +
                ", peopleDate='" + peopleDate + '\'' +
                ", notiPeople='" + notiPeople + '\'' +
                ", notiWork='" + notiWork + '\'' +
                ", notiTime='" + notiTime + '\'' +
                '}';
    }
}
