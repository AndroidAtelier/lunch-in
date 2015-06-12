Include these permission in your app manifest:

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

Include this service in your app manifest:

       <service
                android:name="com.github.androidatelier.lunchin.LunchOutDetectionService"
                android:exported="false"/>

Your activity needs to implement LunchOutDetectionListener and override possibleLunchOutDetected()

In your activity you need to start the service, see the playground sample app for example.  When starting pass in work SSID and lunch start and end time as shown.

Questions?  Ask Mark or Kelly
