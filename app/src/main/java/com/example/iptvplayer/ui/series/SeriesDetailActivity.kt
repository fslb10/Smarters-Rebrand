package com.example.iptvplayer.ui.series

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.iptvplayer.R

/** Hosts the seasons/episodes browser for a single series. */
class SeriesDetailActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        if (savedInstanceState == null) {
            val seriesId = intent.getIntExtra(EXTRA_SERIES_ID, 0)
            val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SeriesDetailFragment.create(seriesId, title))
                .commit()
        }
    }

    companion object {
        const val EXTRA_SERIES_ID = "series_id"
        const val EXTRA_TITLE = "title"

        fun newIntent(context: Context, seriesId: Int, title: String): Intent =
            Intent(context, SeriesDetailActivity::class.java)
                .putExtra(EXTRA_SERIES_ID, seriesId)
                .putExtra(EXTRA_TITLE, title)
    }
}
