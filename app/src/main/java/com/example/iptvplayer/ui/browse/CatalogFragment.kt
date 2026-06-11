package com.example.iptvplayer.ui.browse

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.lifecycle.lifecycleScope
import com.example.iptvplayer.R
import com.example.iptvplayer.data.model.Category
import com.example.iptvplayer.data.model.LiveStream
import com.example.iptvplayer.data.model.Series
import com.example.iptvplayer.data.model.VodStream
import com.example.iptvplayer.di.ServiceLocator
import com.example.iptvplayer.ui.player.PlaybackActivity
import com.example.iptvplayer.ui.series.SeriesDetailActivity
import kotlinx.coroutines.launch

/**
 * Renders one content section (Live / Movies / Series) as a vertical list of
 * category rows. Items are fetched once and grouped by category client-side.
 */
class CatalogFragment : RowsSupportFragment() {

    enum class Type { LIVE, VOD, SERIES }

    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private val repository get() = ServiceLocator.repository

    private val type: Type
        get() = Type.valueOf(requireArguments().getString(ARG_TYPE)!!)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = rowsAdapter
        setupClickListener()
        loadContent()
    }

    private fun setupClickListener() {
        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is LiveStream -> startActivity(
                    PlaybackActivity.newIntent(
                        requireContext(),
                        repository.buildLiveStreamUrl(item.streamId),
                        item.name
                    )
                )
                is VodStream -> startActivity(
                    PlaybackActivity.newIntent(
                        requireContext(),
                        repository.buildVodStreamUrl(item.streamId, item.containerExtension),
                        item.name
                    )
                )
                is Series -> startActivity(
                    SeriesDetailActivity.newIntent(requireContext(), item.seriesId, item.name)
                )
            }
        }
    }

    private fun loadContent() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                when (type) {
                    Type.LIVE -> {
                        val items = repository.getLiveStreams().groupBy { it.categoryId }
                        buildRows(repository.getLiveCategories(), items)
                    }
                    Type.VOD -> {
                        val items = repository.getVodStreams().groupBy { it.categoryId }
                        buildRows(repository.getVodCategories(), items)
                    }
                    Type.SERIES -> {
                        val items = repository.getSeries().groupBy { it.categoryId }
                        buildRows(repository.getSeriesCategories(), items)
                    }
                }

                if (rowsAdapter.size() == 0) {
                    toast(getString(R.string.error_no_content))
                }
            } catch (e: Exception) {
                toast(getString(R.string.error_network, e.localizedMessage ?: ""))
            }
        }
    }

    private fun buildRows(categories: List<Category>, byCategory: Map<String?, List<Any>>) {
        rowsAdapter.clear()
        categories.forEach { category ->
            val items = byCategory[category.categoryId].orEmpty()
            if (items.isEmpty()) return@forEach

            val rowAdapter = ArrayObjectAdapter(CardPresenter())
            items.forEach { rowAdapter.add(it) }

            val header = HeaderItem(
                category.categoryId.toLongOrNull() ?: 0L,
                category.categoryName
            )
            rowsAdapter.add(ListRow(header, rowAdapter))
        }
    }

    private fun toast(message: String) {
        if (isAdded) Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ARG_TYPE = "type"

        fun create(type: Type): CatalogFragment = CatalogFragment().apply {
            arguments = Bundle().apply { putString(ARG_TYPE, type.name) }
        }
    }
}
