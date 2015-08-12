package com.androidatelier.lunchin.database;

import android.test.InstrumentationTestCase;

import com.androidatelier.lunchin.database.Clock;

import org.joda.time.DateTime;
import org.mockito.Mockito;

public class LunchInApiTest extends InstrumentationTestCase {
    private static final String DATABASE_NAME = "DatabaseUtilityTest.db";

    private Clock mClock;
    private LunchInApi mLunchInApi;

    @Override
    public void setUp() {
        System.setProperty(
            "dexmaker.dexcache",
            getInstrumentation().getTargetContext().getCacheDir().getPath());

        mClock = Mockito.mock(Clock.class);
        mLunchInApi = new LunchInApi(
                getInstrumentation().getTargetContext(), mClock, DATABASE_NAME);

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 9, 18, 0, 0));
        assertEquals(0, mLunchInApi.getLunchTotal());
        assertEquals(0, mLunchInApi.getNumberOfLunchIns());
        assertEquals(0, mLunchInApi.getNumberOfLunchOuts());
        assertEquals(false, mLunchInApi.didUserLunchOutToday());
    }

    public void testLunchIn() {
        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 10, 12, 30, 0));
        mLunchInApi.setLunchIn();
        assertEquals(1, mLunchInApi.getLunchTotal());
        assertEquals(1, mLunchInApi.getNumberOfLunchIns());
        assertEquals(0, mLunchInApi.getNumberOfLunchOuts());

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 11, 12, 20, 0));
        mLunchInApi.setLunchIn();
        assertEquals(2, mLunchInApi.getLunchTotal());
        assertEquals(2, mLunchInApi.getNumberOfLunchIns());
        assertEquals(0, mLunchInApi.getNumberOfLunchOuts());
    }

    public void testLunchOut() {
        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 10, 12, 30, 0));
        mLunchInApi.setLunchOut();
        assertEquals(1, mLunchInApi.getLunchTotal());
        assertEquals(0, mLunchInApi.getNumberOfLunchIns());
        assertEquals(1, mLunchInApi.getNumberOfLunchOuts());

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 11, 12, 34, 56));
        mLunchInApi.setLunchIn();
        assertEquals(2, mLunchInApi.getLunchTotal());
        assertEquals(1, mLunchInApi.getNumberOfLunchIns());
        assertEquals(1, mLunchInApi.getNumberOfLunchOuts());

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 14, 13, 0, 0));
        mLunchInApi.setLunchOut();
        assertEquals(3, mLunchInApi.getLunchTotal());
        assertEquals(1, mLunchInApi.getNumberOfLunchIns());
        assertEquals(2, mLunchInApi.getNumberOfLunchOuts());
    }

    public void testDidUserLunchOutToday() {
        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 10, 12, 30, 0));
        mLunchInApi.setLunchIn();
        assertEquals(false, mLunchInApi.didUserLunchOutToday());

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 12, 12, 15, 0));
        assertEquals(false, mLunchInApi.didUserLunchOutToday());
        mLunchInApi.setLunchOut();
        assertEquals(true, mLunchInApi.didUserLunchOutToday());

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 13, 18, 0, 0));
        assertEquals(false, mLunchInApi.didUserLunchOutToday());
    }

    public void testLunchInsThisMonth() {
        assertEquals(0, mLunchInApi.getLunchTotal());

        Mockito.when(mClock.getNow()).thenReturn(
                new DateTime(2015, 6, 1, 0, 0, 0).minusMillis(1));
        mLunchInApi.setLunchIn();
        
        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 9, 18, 0, 0));
        assertEquals(1, mLunchInApi.getLunchTotal());
        assertEquals(1, mLunchInApi.getNumberOfLunchIns());
        assertEquals(0, mLunchInApi.getNumberOfLunchInsThisMonth());

        Mockito.when(mClock.getNow()).thenReturn(new DateTime(2015, 6, 12, 12, 15, 0));
        mLunchInApi.setLunchIn();
        assertEquals(2, mLunchInApi.getLunchTotal());
        assertEquals(2, mLunchInApi.getNumberOfLunchIns());
        assertEquals(1, mLunchInApi.getNumberOfLunchInsThisMonth());
    }

    @Override
    protected void tearDown() throws Exception {
        getInstrumentation().getTargetContext().deleteDatabase(DATABASE_NAME);
        super.tearDown();
    }
}