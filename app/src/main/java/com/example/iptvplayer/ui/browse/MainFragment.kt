package com.example.iptvplayer.ui.browse

import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.PageRow
import com.example.iptvplayer.R

/**
 * Top-level TV browse screen. Shows three sections — Live TV, Movies and
 * Series — as page rows. Each section's content is supplied by a
 * [CatalogFragment] via [PageRowFragmentFactory].
 */
class MainFragment : BrowseSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        mainFragmentRegistry.registerFragment(PageRow::class.java, PageRowFragmentFactory())

        val rows = ArrayObjectAdapter(ListRowPresenter())
        rows.add(PageRow(HeaderItem(PAGE_LIVE, getString(R.string.section_live))))
        rows.add(PageRow(HeaderItem(PAGE_VOD, getString(R.string.section_movies))))
        rows.add(PageRow(HeaderItem(PAGE_SERIES, getString(R.string.section_series))))
        adapter = rows
    }

    companion object {
        const val PAGE_LIVE = 0L
        const val PAGE_VOD = 1L
        const val PAGE_SERIES = 2L
    }
}
