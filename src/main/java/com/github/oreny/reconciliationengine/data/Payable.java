package com.github.oreny.reconciliationengine.data;

import java.util.Date;

public class Payable {
    public String id;
    public String referenceId;
    public Double amount;
    public Date dateOccurred;

    @Override
    public String toString() {
        return String.format("[Payable: id: %s amount=%f referenceId=%s date=%s]", id, amount, referenceId,
                dateOccurred);
    }
}
