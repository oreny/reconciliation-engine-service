package com.github.oreny.reconciliationengine.data.serializer;

import com.github.oreny.reconciliationengine.data.Payment;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.*;

public class PaymentJsonSerializerTest {

    @Test
    public void testToString() {
        Payment payment = new Payment();
        payment.amount = 10.25;
        payment.date = new Date();
        payment.referenceId = "ABCD";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnNullJson() {
        getPaymentFromJson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingAmount() {
        String json = "{\"payment_reference\":\"AC123\", \"payment_date\":\"2016-10-22\"}";
        getPaymentFromJson(json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingPaymentReference() {
        String json = "{\"amount\":55.12, \"payment_date\":\"2016-10-22\"}";
        getPaymentFromJson(json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingPaymmentDate() {
        String json = "{\"amount\":55.12, \"payment_reference\":\"AC123\"}";
        getPaymentFromJson(json);
    }

    @Test
    public void testValidSerialization() {
        String json = "{\"amount\":55.12, \"payment_reference\":\"AC123\", \"payment_date\":\"2016-10-22\"}";
        Payment payment = getPaymentFromJson(json);
        assertNotNull(payment);
        assertEquals(new Double(55.12), payment.amount);
        assertEquals("AC123", payment.referenceId);
        LocalDate localDate = payment.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(10, localDate.getMonthValue());
        assertEquals(2016, localDate.getYear());
        assertEquals(22, localDate.getDayOfMonth());
    }

    private Payment getPaymentFromJson(String json) {
        PaymentJsonSerializer serializer = new PaymentJsonSerializer();
        return serializer.fromJson(json);
    }
}