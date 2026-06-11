package com.example.iptvplayer.ui.series

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
import com.example.iptvplayer.data.model.Episode
import com.example.iptvplayer.di.ServiceLocator
import com.example.iptvplayer.ui.browse.CardPresenter
import com.example.iptvplayer.ui.player.PlaybackActivity
import kotlinx.coroutines.launch

/** Shows a series' episodes grouped into one row per season. */
class SeriesDetailFragment : RowsSupportFragment() {

    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private val repository get() = ServiceLocator.repository

    private val seriesId: Int get() = requireArguments().getInt(ARG_SERIES_ID)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = rowsAdapter

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            if (item is Episode) {
                startActivity(
                    PlaybackActivity.newIntent(
                        requireContext(),
                        repository.buildSeriesStreamUrl(item.id, item.containerExtension),
                        item.title
                    )
                )
            }
        }

        loadEpisodes()
    }

    private fun loadEpisodes() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val info = repository.getSeriesInfo(seriesId)
                val episodesBySeason = info.episodes.orEmpty()

                rowsAdapter.clear()
                episodesBySeason.keys
                    .sortedBy { it.toIntOrNull() ?: Int.MAX_VALUE }
                    .forEach { seasonKey ->
                        val episodes = episodesBySeason[seasonKey].orEmpty()
                        if (episodes.isEmpty()) return@forEach

                        val rowAdapter = ArrayObjectAdapter(CardPresenter())
                        episodes.forEach { rowAdapter.add(it) }

                        val header = HeaderItem(
                            seasonKey.toLongOrNull() ?: 0L,
                            getString(R.string.season_label, seasonKey)
                        )
                        rowsAdapter.add(ListRow(header, rowAdapter))
                    }

                if (rowsAdapter.size() == 0) {
                    toast(getString(R.string.error_no_episodes))
                }
            } catch (e: Exception) {
                toast(getString(R.string.error_network, e.localizedMessage ?: ""))
            }
        }
    }

    private fun toast(message: String) {
        if (isAdded) Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ARG_SERIES_ID = "series_id"
        private const val ARG_TITLE = "title"

        fun create(seriesId: Int, title: String): SeriesDetailFragment =
            SeriesDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SERIES_ID, seriesId)
                    putString(ARG_TITLE, title)
                }
            }
    }
}
