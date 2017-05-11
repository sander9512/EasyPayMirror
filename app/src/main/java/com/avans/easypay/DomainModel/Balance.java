package com.avans.easypay.DomainModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bart on 2-5-2017.
 */

public class Balance implements Serializable{

    private float amount;
    private Date timeLog;

    public Balance(float amount, Date updateTime) {
        this.amount = amount;
        this.timeLog = updateTime;
    }

    public Balance(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public Date getTimeLog() {
        return timeLog;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
