package com.github.oreny.reconciliationengine.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Payment {

    public Double amount;

    @SerializedName(value = "payment_reference")
    public String referenceId;

    @SerializedName(value = "payment_date")
    public Date date;

    @Override
    public String toString() {
        return String.format("[Payment: amount=%f referenceId=%s date=%s]", amount, referenceId, date);
    }
}
