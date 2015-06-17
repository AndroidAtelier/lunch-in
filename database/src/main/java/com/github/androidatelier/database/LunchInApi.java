package com.github.androidatelier.database;

import java.util.Random;

/**
 * Created by Jenny on 6/16/2015.
 */
public class LunchInApi {

    /**
     * Get the number of lunch ins the person has used
     * @return Returns a random number between 0 and 50
     */
    public int GetNumberOfLunchIns()
    {
        Random generator = new Random();
        return generator.nextInt(50);
    }

    /**
     * Creates another lunch in line in database
     */
    public void SetLunchIn()
    {

    }

    /**
     * Indicates user went out to lunch instead of eating in
     */
    public void SetLunchOut()
    {

    }

    public void InitializeDatabase()
    {

    }

    public void UpgradeDatabase()
    {
        
    }
}
