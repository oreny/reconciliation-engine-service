package com.github.oreny.reconciliationengine.data.serializer;

import com.github.oreny.reconciliationengine.data.Payment;

public class PaymentJsonSerializer extends GsonSerializer<Payment> {

    public PaymentJsonSerializer() {
        super(Payment.class);
    }

    @Override
    protected boolean isValid(Payment payment) {
        return payment.amount != null && payment.referenceId != null && payment.date != null;
    }
}
