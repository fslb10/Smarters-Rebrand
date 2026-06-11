package com.example.iptvplayer

import android.app.Application
import com.example.iptvplayer.di.ServiceLocator

class IptvApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
