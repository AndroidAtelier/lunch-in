package com.github.androidatelier.lunchin.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

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
            assertThat(daysOfTheWeek.isSet(i)).isFalse();
        }

        assertThat(daysOfTheWeek.toString()).isEqualTo("");
    }

    @Test
    public void setPositions2And3() throws Exception {
        daysOfTheWeek.set(2);
        daysOfTheWeek.set(3);

        assertThat(daysOfTheWeek.isSet(0)).isFalse();
        assertThat(daysOfTheWeek.isSet(1)).isFalse();
        assertThat(daysOfTheWeek.isSet(2)).isTrue();
        assertThat(daysOfTheWeek.isSet(3)).isTrue();
        assertThat(daysOfTheWeek.isSet(4)).isFalse();
        assertThat(daysOfTheWeek.isSet(5)).isFalse();
        assertThat(daysOfTheWeek.isSet(6)).isFalse();

        assertThat(daysOfTheWeek.toString()).isEqualTo("Two, Three");
    }

    @Test
    public void setBitVector() throws Exception {
        daysOfTheWeek.bitVector = 17;  // 1 + 16 = 2^0 + 2^4

        assertThat(daysOfTheWeek.isSet(0)).isTrue();
        assertThat(daysOfTheWeek.isSet(1)).isFalse();
        assertThat(daysOfTheWeek.isSet(2)).isFalse();
        assertThat(daysOfTheWeek.isSet(3)).isFalse();
        assertThat(daysOfTheWeek.isSet(4)).isTrue();
        assertThat(daysOfTheWeek.isSet(5)).isFalse();
        assertThat(daysOfTheWeek.isSet(6)).isFalse();

        assertThat(daysOfTheWeek.toString()).isEqualTo("Zero, Four");
    }

    @Test
    public void setAndClear() throws Exception {
        daysOfTheWeek.set(1);

        assertThat(daysOfTheWeek.isSet(0)).isFalse();
        assertThat(daysOfTheWeek.isSet(1)).isTrue();
        assertThat(daysOfTheWeek.isSet(2)).isFalse();
        assertThat(daysOfTheWeek.isSet(3)).isFalse();
        assertThat(daysOfTheWeek.isSet(4)).isFalse();
        assertThat(daysOfTheWeek.isSet(5)).isFalse();
        assertThat(daysOfTheWeek.isSet(6)).isFalse();

        assertThat(daysOfTheWeek.toString()).isEqualTo("One");

        daysOfTheWeek.clear(1);

        assertThat(daysOfTheWeek.isSet(0)).isFalse();
        assertThat(daysOfTheWeek.isSet(1)).isFalse();
        assertThat(daysOfTheWeek.isSet(2)).isFalse();
        assertThat(daysOfTheWeek.isSet(3)).isFalse();
        assertThat(daysOfTheWeek.isSet(4)).isFalse();
        assertThat(daysOfTheWeek.isSet(5)).isFalse();
        assertThat(daysOfTheWeek.isSet(6)).isFalse();

        assertThat(daysOfTheWeek.toString()).isEqualTo("");

    }
}