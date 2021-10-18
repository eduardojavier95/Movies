package com.example.movies.features.movies.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.features.movies.data.models.data.MoviesResponse
import com.squareup.picasso.Picasso

class MoviesAdapter(private val movies: MutableList<MoviesResponse>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemMovieBinding.bind(view)

        val name = binding.tvName
        val date = binding.tvDate

        fun bind(movie: MoviesResponse) {
            name.text = movie.title
            date.text = movie.release_date
            Picasso.get().load(movie.poster_path).into(binding.ivMovie)
        }
    }
}