package com.github.oreny.reconciliationengine.engine;

import com.github.oreny.reconciliationengine.data.Payable;
import com.github.oreny.reconciliationengine.data.Payment;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecallDrivenPaymanetPayableMatcherTest {

    @Test
    public void testRemoveNonAlphaNumericChars() {
        assertEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("ABC"));
        assertEquals("12ABC3", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("12ABC3"));
        assertEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("  ABC  "));
        assertEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("  A/B/C  "));
        assertEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("A - B - C"));
        assertEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("!@#$%^&*()-+A - B - C!  "));
        assertNotEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("ABD"));
        assertNotEquals("ABC", RecallDrivenPaymanetPayableMatcher.removeNonAlphaNumericChars("!@#$%^&*()-+A - B - D!  "));
    }

    @Test
    public void testNoMatch() {
        Payment payment = new Payment();
        payment.amount = 20.0;
        payment.referenceId = "ABC";
        Payable payable = new Payable();
        payable.amount = 30.0;
        payable.referenceId = "ABD";
        assertFalse(new RecallDrivenPaymanetPayableMatcher().isMatch(payment, payable));
    }

    @Test
    public void testMatchBasedOnAmount() {
        double amount = 24.23;
        Payment payment = new Payment();
        payment.amount = amount;
        payment.referenceId = "ABC";
        Payable payable = new Payable();
        payable.amount = amount;
        payable.referenceId = "ABD";
        assertTrue(new RecallDrivenPaymanetPayableMatcher().isMatch(payment, payable));
    }

    @Test
    public void testMatchBasedOnReferenceId() {
        Payment payment = new Payment();
        payment.amount = 20.0;
        payment.referenceId = "AB/1213/ABCA";
        Payable payable = new Payable();
        payable.amount = 30.0;
        payable.referenceId = "AB-1213-ABCA";
        assertTrue(new RecallDrivenPaymanetPayableMatcher().isMatch(payment, payable));
    }

}