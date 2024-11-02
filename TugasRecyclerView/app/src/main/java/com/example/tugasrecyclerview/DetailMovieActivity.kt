package com.example.tugasrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.tugasrecyclerview.databinding.ActivityDetailMovieBinding
import android.content.Intent

class DetailMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Retrieve data passed from MainActivity
        val movieTitle = intent.getStringExtra("movieTitle")
        val movieGenre = intent.getStringExtra("movieGenre")
        val movieDuration = intent.getStringExtra("movieDuration")
        val movieRating = intent.getStringExtra("movieAgeGroup")
        val movieSynopsis = intent.getStringExtra("movieSinopsis")
        val moviePoster = intent.getIntExtra("moviePoster", R.drawable.venom_poster) // assuming a default poster if none provided

        // Set data to the views
        val titleTextView = findViewById<TextView>(R.id.movie_title)
        val genreTextView = findViewById<TextView>(R.id.movie_genre)
        val durationTextView = findViewById<TextView>(R.id.movie_duration)
        val ratingTextView = findViewById<TextView>(R.id.movie_rating)
        val synopsisTextView = findViewById<TextView>(R.id.movie_synopsis)
        val posterImageView = findViewById<ImageView>(R.id.movie_poster)

        titleTextView.text = movieTitle
        genreTextView.text = movieGenre
        durationTextView.text = movieDuration
        ratingTextView.text = movieRating
        synopsisTextView.text = movieSynopsis
        posterImageView.setImageResource(moviePoster)

        with(binding) {
            backToHome.setOnClickListener {
                val intent = Intent(this@DetailMovieActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
