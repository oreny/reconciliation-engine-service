package com.github.oreny.reconciliationengine.engine;

import com.github.oreny.reconciliationengine.data.Payable;
import com.github.oreny.reconciliationengine.data.Payment;

public class RecallDrivenPaymanetPayableMatcher implements PaymentPayableMatcher {

    @Override
    public boolean isMatch(Payment payment, Payable payable) {
        String paymentReferenceId = removeNonAlphaNumericChars(payment.referenceId);
        String payableReferenceId = removeNonAlphaNumericChars(payable.referenceId);
        return payment.amount.equals(payable.amount) || paymentReferenceId.equalsIgnoreCase(payableReferenceId);
    }

    static protected String removeNonAlphaNumericChars(String str) {
        return str.replaceAll("[^A-Za-z0-9]", "");
    }
}
