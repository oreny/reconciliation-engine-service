package com.github.oreny.reconciliationengine.engine;

import com.github.oreny.reconciliationengine.data.Payable;
import com.github.oreny.reconciliationengine.data.Payment;

public interface PaymentPayableMatcher {
    boolean isMatch(Payment payment, Payable payable);
}
