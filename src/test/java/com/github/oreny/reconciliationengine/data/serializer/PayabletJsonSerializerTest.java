package com.github.oreny.reconciliationengine.data.serializer;

import com.github.oreny.reconciliationengine.data.Payable;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PayabletJsonSerializerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnNullJson() {
        getPayableFromJson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingId() {
        String json = "{\"amount\": 15.35, \"referenceId\": \"AB\", \"dateOccurred\": \"2016-10-01\" }";
        getPayableFromJson(json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingAmount() {
        String json = "{\"id\": \"XX\", \"referenceId\": \"AB\", \"dateOccurred\": \"2016-10-01\" }";
        getPayableFromJson(json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingPayableReference() {
        String json = "{\"id\": \"XX\", \"amount\": 15.35, \"dateOccurred\": \"2016-10-01\" }";
        getPayableFromJson(json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnMissingPaymmentDate() {
        String json = "{\"id\": \"XX\", \"amount\": 15.35, \"referenceId\": \"AB\" }";
        getPayableFromJson(json);
    }

    @Test
    public void testValidSerialization() {
        String json = "{\"id\": \"XX\", \"amount\": 15.35, \"referenceId\": \"AB\", \"dateOccurred\": \"2016-10-01\" }";
        Payable Payable = getPayableFromJson(json);
        assertNotNull(Payable);
        assertEquals("XX", Payable.id);
        assertEquals(new Double(15.35), Payable.amount);
        assertEquals("AB", Payable.referenceId);
        LocalDate localDate = Payable.dateOccurred.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(10, localDate.getMonthValue());
        assertEquals(2016, localDate.getYear());
        assertEquals(1, localDate.getDayOfMonth());
    }

    private Payable getPayableFromJson(String json) {
        PayableJsonSerializer serializer = new PayableJsonSerializer();
        return serializer.fromJson(json);
    }
}