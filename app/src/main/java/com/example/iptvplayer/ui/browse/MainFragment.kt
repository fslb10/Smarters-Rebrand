package com.example.iptvplayer.ui.browse

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.lifecycleScope
import com.example.iptvplayer.R
import com.example.iptvplayer.data.model.LiveStream
import com.example.iptvplayer.di.ServiceLocator
import com.example.iptvplayer.ui.player.PlaybackActivity
import kotlinx.coroutines.launch

/**
 * Shows live channels grouped into rows by category. All streams are
 * fetched once and grouped client-side to avoid a request per category.
 */
class MainFragment : BrowseSupportFragment() {

    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private val repository get() = ServiceLocator.repository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        adapter = rowsAdapter

        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is LiveStream) {
                val url = repository.buildLiveStreamUrl(item.streamId)
                startActivity(PlaybackActivity.newIntent(requireContext(), url, item.name))
            }
        }

        loadContent()
    }

    private fun loadContent() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val categories = repository.getLiveCategories()
                val streamsByCategory = repository.getLiveStreams()
                    .groupBy { it.categoryId }

                rowsAdapter.clear()
                categories.forEach { category ->
                    val streams = streamsByCategory[category.categoryId].orEmpty()
                    if (streams.isEmpty()) return@forEach

                    val rowAdapter = ArrayObjectAdapter(CardPresenter())
                    streams.forEach { rowAdapter.add(it) }

                    val header = HeaderItem(
                        category.categoryId.toLongOrNull() ?: 0L,
                        category.categoryName
                    )
                    rowsAdapter.add(ListRow(header, rowAdapter))
                }

                if (rowsAdapter.size() == 0) {
                    Toast.makeText(
                        requireContext(),
                        R.string.error_no_channels,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_network, e.localizedMessage ?: ""),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
