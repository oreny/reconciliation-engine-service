package com.github.oreny.reconciliationengine.data.serializer;

import com.github.oreny.reconciliationengine.data.Payable;

public class PayableJsonSerializer extends GsonSerializer<Payable> {

    public PayableJsonSerializer() {
        super(Payable.class);
    }

    @Override
    protected  boolean isValid(Payable payable) {
        return payable.id != null && payable.amount != null && payable.referenceId != null
                && payable.dateOccurred != null;
    }}
