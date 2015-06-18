Include these permission in your app manifest:

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

Create your own receiver class that extends from LunchOutDetectionReceiver and override onPossibleLunchOut() with your custom implementation.

Include this receiver in your app manifest:

       <receiver android:name=".receiver.nameOfYourReceiver" >
                   <intent-filter>
                       <action android:name="android.net.wifi.WIFI_STATE_CHANGED" >
                       </action>
                   </intent-filter>
               </receiver>


Questions?  Ask Mark or Kelly
