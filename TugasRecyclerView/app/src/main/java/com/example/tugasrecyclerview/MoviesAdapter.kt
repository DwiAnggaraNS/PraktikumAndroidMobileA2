package com.example.latihanrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasrecyclerview.databinding.ItemCardMoviesBinding
import com.example.tugasrecyclerview.Movies

typealias OnClickMovies = (Movies) -> Unit

class MoviesAdapter  (private val lisMovies : List<Movies>,private val onClickMovies: OnClickMovies)  : RecyclerView.Adapter<MoviesAdapter.ItemMoviesViewHolder>() {

    inner class ItemMoviesViewHolder(private val binding: ItemCardMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Movies) {
            with(binding) {
                movieTitle.text = data.movieTitle
                movieDuration.text = data.movieDuration
                movieAgeGroup.text = data.movieAgeGroup
                moviePoster.setImageResource(data.moviePoster) // Set the image resource
                itemView.setOnClickListener {
                    onClickMovies(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMoviesViewHolder {
        val binding = ItemCardMoviesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemMoviesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return lisMovies.size
    }

    override fun onBindViewHolder(holder: ItemMoviesViewHolder, position: Int) {
        holder.bind(lisMovies[position])
    }
}
