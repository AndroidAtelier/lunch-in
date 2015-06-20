package com.github.androidatelier.lunchin.util;

public class DaysOfTheWeek {
    private final String[] labels;
    public int bitVector = 0;

    public DaysOfTheWeek(String[] labels) {
        this.labels = labels;
    }

    public boolean isSet(int position) {
        int dayBit = 1 << position;
        return ((bitVector & dayBit) == dayBit);
    }

    public void set(int position) {
        int dayBit = 1 << position;
        bitVector = bitVector | dayBit;
    }

    public void clear(int position) {
        int dayBit = 1 << position;
        bitVector = bitVector & ~dayBit;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < labels.length; ++i) {
            int dayBit = 1 << i;
            if ((bitVector & dayBit) == dayBit) {
                if (buf.length() > 0) {
                    buf.append(", ");
                }
                buf.append(labels[i]);
            }
        }
        return buf.toString();
    }
}