package model;

/**
 * Created by jr on 6/15/15.
 */
public class TimeTable {
    private String id;
    private String chris_date;
    private String detail_info;
    private String main_date;
    private String sehri_time;
    private String iftari_time;
    private boolean is_kaderi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChris_date() {
        return chris_date;
    }

    public void setChris_date(String chris_date) {
        this.chris_date = chris_date;
    }

    public String getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(String detail_info) {
        this.detail_info = detail_info;
    }

    public String getMain_date() {
        return main_date;
    }

    public void setMain_date(String main_date) {
        this.main_date = main_date;
    }

    public String getSehri_time() {
        return sehri_time;
    }

    public void setSehri_time(String sehri_time) {
        this.sehri_time = sehri_time;
    }

    public String getIftari_time() {
        return iftari_time;
    }

    public void setIftari_time(String iftari_time) {
        this.iftari_time = iftari_time;
    }

    public boolean is_kaderi() {
        return is_kaderi;
    }

    public void setIs_kaderi(boolean is_kaderi) {
        this.is_kaderi = is_kaderi;
    }
}
