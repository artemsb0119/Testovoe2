package com.example.testovoe2
import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "edc4a38a-41bb-44c9-abd3-f6ee0685dec8"

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}