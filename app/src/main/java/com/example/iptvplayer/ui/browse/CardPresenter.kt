package com.example.iptvplayer.ui.browse

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.iptvplayer.R
import com.example.iptvplayer.data.model.LiveStream

/** Renders a single live channel as a focusable Leanback card. */
class CardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val stream = item as LiveStream
        val card = viewHolder.view as ImageCardView

        card.titleText = stream.name
        card.contentText = stream.epgChannelId.orEmpty()
        card.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)

        val placeholder = ContextCompat.getDrawable(card.context, R.drawable.ic_channel_placeholder)
        if (!stream.streamIcon.isNullOrBlank()) {
            Glide.with(card.context)
                .load(stream.streamIcon)
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

    private companion object {
        const val CARD_WIDTH = 280
        const val CARD_HEIGHT = 180
    }
}
