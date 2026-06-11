package com.example.iptvplayer.di

import android.content.Context
import com.example.iptvplayer.data.SessionStore
import com.example.iptvplayer.data.XtreamRepository
import com.example.iptvplayer.data.remote.XtreamClient

/**
 * Tiny manual dependency container. Initialized once from [com.example.iptvplayer.IptvApp].
 */
object ServiceLocator {

    lateinit var session: SessionStore
        private set

    lateinit var repository: XtreamRepository
        private set

    fun init(context: Context) {
        session = SessionStore(context.applicationContext)
        repository = XtreamRepository(XtreamClient.create(), session)
    }
}
