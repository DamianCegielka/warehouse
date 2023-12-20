package org.project.repository.entity;


public class RateFormat {

    private String Date;
    private Double Rate;

    RateFormat(String Date, Double Rate) {
        this.Date = Date;
        this.Rate = Rate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }


}
