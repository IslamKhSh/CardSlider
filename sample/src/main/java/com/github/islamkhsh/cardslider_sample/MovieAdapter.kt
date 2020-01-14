package com.github.islamkhsh.cardslider_sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.item_card_content.view.*

class MovieAdapter(private val movies: List<Movie>) : CardSliderAdapter<MovieAdapter.MovieViewHolder>() {


    override fun bindVH(holder: MovieViewHolder, position: Int) {

        val movie = movies[position]

        holder.itemView.run {
            movie_poster.setImageResource(movie.poster)
            movie_title.text = movie.title
            movie_overview.text = movie.overview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_content, parent, false)

        return MovieViewHolder(view)
    }

    override fun getItemCount() = movies.size


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view)
}