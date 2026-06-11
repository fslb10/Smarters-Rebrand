package com.example.iptvplayer.ui.browse

import androidx.fragment.app.Fragment
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.Row

/** Maps each [androidx.leanback.widget.PageRow] header id to its content fragment. */
class PageRowFragmentFactory : BrowseSupportFragment.FragmentFactory<Fragment>() {

    override fun createFragment(rowObj: Any): Fragment {
        val row = rowObj as Row
        return when (row.headerItem.id) {
            MainFragment.PAGE_LIVE -> CatalogFragment.create(CatalogFragment.Type.LIVE)
            MainFragment.PAGE_VOD -> CatalogFragment.create(CatalogFragment.Type.VOD)
            MainFragment.PAGE_SERIES -> CatalogFragment.create(CatalogFragment.Type.SERIES)
            else -> throw IllegalArgumentException("Unknown page row: ${row.headerItem.id}")
        }
    }
}
