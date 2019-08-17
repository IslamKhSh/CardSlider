package com.github.islamkhsh.cardslider_sample

import android.view.View
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.item_card_content.view.*

class MovieAdapter(items : ArrayList<Movie>) : CardSliderAdapter<Movie>(items) {

    override fun bindView(position: Int, itemContentView: View, item: Movie?) {

        item?.run {
            itemContentView.movie_poster.setImageResource(poster)
            itemContentView.movie_title.text = title
            itemContentView.movie_overview.text = overview
        }
    }

    override fun getItemContentLayout(position: Int) = R.layout.item_card_content
}