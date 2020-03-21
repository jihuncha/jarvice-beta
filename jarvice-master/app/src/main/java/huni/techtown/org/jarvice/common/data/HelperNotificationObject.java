package huni.techtown.org.jarvice.common.data;


/**
 * 공지/예약 - 앱 데이터
 *
 *
 *
 * **/
public class HelperNotificationObject {
    private String TAG = HelperNotificationObject.class.getSimpleName();

    private long id;
    private String notiDate;
    private String notiInfo;
    private int notiCheck;              //0 - check 안됨 / 1 - check 됨.

    public HelperNotificationObject(){}

    public HelperNotificationObject(String notiDate, String notiInfo, int notiCheck) {
        this.notiDate = notiDate;
        this.notiInfo = notiInfo;
        this.notiCheck = notiCheck;
    }

    public HelperNotificationObject(long id, String notiDate, String notiInfo, int notiCheck) {
        this.id = id;
        this.notiDate = notiDate;
        this.notiInfo = notiInfo;
        this.notiCheck = notiCheck;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNotiDate() {
        return notiDate;
    }

    public void setNotiDate(String notiDate) {
        this.notiDate = notiDate;
    }

    public String getNotiInfo() {
        return notiInfo;
    }

    public void setNotiInfo(String notiInfo) {
        this.notiInfo = notiInfo;
    }

    public int getNotiCheck() {
        return notiCheck;
    }

    public void setNotiCheck(int notiCheck) {
        this.notiCheck = notiCheck;
    }

    @Override
    public String toString() {
        return "HelperNotificationObject{" +
                "id=" + id +
                ", notiDate='" + notiDate + '\'' +
                ", notiInfo='" + notiInfo + '\'' +
                ", notiCheck=" + notiCheck +
                '}';
    }
}
