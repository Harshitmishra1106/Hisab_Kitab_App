package com.example.myapplication;

//this class is only used when listData is used to insert items not in case of ArrayList
public class MyListData {
    private String Purpose;
    private String Date;
    private Integer Amount;
    private String Time;
    public MyListData(String Purpose, Integer Amount, String Date,String Time) {
        this.Purpose = Purpose;
        this.Amount = Amount;
        this.Date= Date;
        this.Time= Time;
    }
    public String getPurpose() {
        return Purpose;
    }
    public void setPurpose(String Purpose) {
        this.Purpose = Purpose;
    }
    public Integer getAmount() {
        return Amount;
    }
    public void setAmount(Integer Amount) {
        this.Amount = Amount;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }
    public String getTime() {return Time;}
    public void setTime(String time) {Time = time;}
}
