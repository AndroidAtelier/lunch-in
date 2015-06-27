package com.github.androidatelier.lunchin.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public final class DaysOfTheWeekTest {
    private DaysOfTheWeek daysOfTheWeek = new DaysOfTheWeek(new String[] {
            "Zero", "One", "Two", "Three", "Four", "Five", "Six"
    });

    @Before
    public void clear() {
        daysOfTheWeek.bitVector = 0;
    }

    @Test
    public void noneSelected() throws Exception {
        for (int i = 0; i < 7; ++i) {
            assertFalse(daysOfTheWeek.isSet(i));
        }

        assertEquals("", daysOfTheWeek.toString());
    }

    @Test
    public void setPositions2And3() throws Exception {
        daysOfTheWeek.set(2);
        daysOfTheWeek.set(3);

        assertFalse(daysOfTheWeek.isSet(0));
        assertFalse(daysOfTheWeek.isSet(1));
        assertTrue(daysOfTheWeek.isSet(2));
        assertTrue(daysOfTheWeek.isSet(3));
        assertFalse(daysOfTheWeek.isSet(4));
        assertFalse(daysOfTheWeek.isSet(5));
        assertFalse(daysOfTheWeek.isSet(6));

        assertEquals("Two, Three", daysOfTheWeek.toString());
    }

    @Test
    public void setBitVector() throws Exception {
        daysOfTheWeek.bitVector = 17;  // 1 + 16 = 2^0 + 2^4

        assertTrue(daysOfTheWeek.isSet(0));
        assertFalse(daysOfTheWeek.isSet(1));
        assertFalse(daysOfTheWeek.isSet(2));
        assertFalse(daysOfTheWeek.isSet(3));
        assertTrue(daysOfTheWeek.isSet(4));
        assertFalse(daysOfTheWeek.isSet(5));
        assertFalse(daysOfTheWeek.isSet(6));

        assertEquals("Zero, Four", daysOfTheWeek.toString());
    }

    @Test
    public void setAndClear() throws Exception {
        daysOfTheWeek.set(1);

        assertFalse(daysOfTheWeek.isSet(0));
        assertTrue(daysOfTheWeek.isSet(1));
        assertFalse(daysOfTheWeek.isSet(2));
        assertFalse(daysOfTheWeek.isSet(3));
        assertFalse(daysOfTheWeek.isSet(4));
        assertFalse(daysOfTheWeek.isSet(5));
        assertFalse(daysOfTheWeek.isSet(6));

        assertEquals("One", daysOfTheWeek.toString());

        daysOfTheWeek.clear(1);

        assertFalse(daysOfTheWeek.isSet(0));
        assertFalse(daysOfTheWeek.isSet(1));
        assertFalse(daysOfTheWeek.isSet(2));
        assertFalse(daysOfTheWeek.isSet(3));
        assertFalse(daysOfTheWeek.isSet(4));
        assertFalse(daysOfTheWeek.isSet(5));
        assertFalse(daysOfTheWeek.isSet(6));

        assertEquals("", daysOfTheWeek.toString());

    }
}