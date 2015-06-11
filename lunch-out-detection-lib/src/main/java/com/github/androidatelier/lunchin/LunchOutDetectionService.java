package com.github.androidatelier.lunchin;

/**
 * Created by A596969 on 6/11/15.
 */
public class LunchOutDetectionService {

    public LunchOutDetectionService(LunchOutDetectionListener listener) {

        while(true) {

            try {
                Thread.sleep(1000 * 60);
                listener.possibleLunchOutDetected();
            } catch(InterruptedException e) {
                //silent fail, bad!
            }

        }
    }

}
