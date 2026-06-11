package com.example.iptvplayer.ui.browse

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.iptvplayer.R
import com.example.iptvplayer.data.model.Episode
import com.example.iptvplayer.data.model.LiveStream
import com.example.iptvplayer.data.model.Series
import com.example.iptvplayer.data.model.VodStream

/**
 * Renders any catalog item (channel, movie, series or episode) as a focusable
 * Leanback card. Channels/episodes use a wide card; movies/series use a poster.
 */
class CardPresenter : Presenter() {

    private class CardMeta(
        val title: String,
        val subtitle: String,
        val imageUrl: String?,
        val width: Int,
        val height: Int
    )

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val card = viewHolder.view as ImageCardView
        val meta = metaFor(item)

        card.titleText = meta.title
        card.contentText = meta.subtitle
        card.setMainImageDimensions(meta.width, meta.height)

        val placeholder =
            ContextCompat.getDrawable(card.context, R.drawable.ic_channel_placeholder)
        if (!meta.imageUrl.isNullOrBlank()) {
            Glide.with(card.context)
                .load(meta.imageUrl)
                .centerInside()
                .placeholder(placeholder)
                .error(placeholder)
                .into(card.mainImageView)
        } else {
            card.mainImage = placeholder
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val card = viewHolder.view as ImageCardView
        card.badgeImage = null
        card.mainImage = null
    }

    private fun metaFor(item: Any): CardMeta = when (item) {
        is LiveStream -> CardMeta(item.name, item.epgChannelId.orEmpty(), item.streamIcon, WIDE_W, WIDE_H)
        is VodStream -> CardMeta(item.name, item.rating.orEmpty(), item.streamIcon, POSTER_W, POSTER_H)
        is Series -> CardMeta(item.name, "", item.cover, POSTER_W, POSTER_H)
        is Episode -> CardMeta(
            item.title.ifBlank { "Episode ${item.episodeNum}" },
            "",
            item.info?.movieImage,
            WIDE_W,
            WIDE_H
        )
        else -> CardMeta(item.toString(), "", null, WIDE_W, WIDE_H)
    }

    private companion object {
        const val WIDE_W = 280
        const val WIDE_H = 158
        const val POSTER_W = 220
        const val POSTER_H = 330
    }
}
