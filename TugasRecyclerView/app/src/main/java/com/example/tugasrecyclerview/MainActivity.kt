package com.example.tugasrecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.example.latihanrecyclerview.MoviesAdapter
import com.example.tugasrecyclerview.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigation()

        val adapterMovie = MoviesAdapter(generateDummy()) { movie ->
            val intent = Intent(this, DetailMovieActivity::class.java).apply {
                putExtra("movieTitle", movie.movieTitle)
                putExtra("movieDuration", movie.movieDuration)
                putExtra("movieAgeGroup", movie.movieAgeGroup)
                putExtra("moviePoster", movie.moviePoster)  // Send poster resource ID
                putExtra("movieSinopsis", movie.movieSinopsis)
            }
            startActivity(intent)
        }


        with(binding) {
            rvMovies.apply {
                adapter = adapterMovie
            }
        }
    }
    fun generateDummy(): List<Movies> {
        return listOf(
            Movies("Venom: The Last Dance", "1h 49m", "R13+", R.drawable.venom_poster, "Journalist Eddie Brock is trying to take down Carlton Drake, the notorious and brilliant founder of the Life Foundation. While investigating one of Drake's experiments, Eddie's body merges with the alien Venom -- leaving him with superhuman strength and power. Twisted, dark and fueled by rage, Venom tries to control the new and dangerous abilities that Eddie finds so intoxicating."),
            Movies("Dosa Musyrik", "1h 32m", "D17+", R.drawable.dosa_musyrik_poster, "Film ini menggambarkan akibat mengerikan dari perbuatan syirik, menunjukkan bagaimana keputusasaan bisa membawa seseorang terjerumus ke dalam situasi berbahaya. Pesan moral yang disampaikan adalah pentingnya tetap waspada dalam menghadapi cobaan hidup dan menghindari keyakinan yang menyimpang dari nilai-nilai agama."),
            Movies("Inside Out", "1h 36m", "SU", R.drawable.inside_out_poster, "Inside Out adalah film animasi yang mengajak kita menjelajahi dunia dalam pikiran seorang gadis berusia 11 tahun bernama Riley. Di dalam pikiran Riley, ada lima emosi yang dipersonifikasikan: Joy, Sadness, Fear, Anger, dan Disgust. Ketika keluarga Riley pindah ke kota baru, emosi-emosi ini mengalami kekacauan. Joy, yang ingin selalu menjaga Riley tetap bahagia, berusaha keras untuk mengendalikan situasi. Namun, Sadness mulai muncul dan menyebabkan kekacauan lebih lanjut. Petualangan seru pun dimulai ketika Joy dan Sadness terjebak di dalam pikiran Riley dan harus bekerja sama untuk mengembalikan keseimbangan emosi Riley serta membantunya beradaptasi dengan kehidupan baru."),
            Movies("Transformer One", "1h 44m", "R13+", R.drawable.transformer_poster, "Transformers One membawa kita kembali ke masa lalu planet Cybertron, sebelum perang besar antara Autobots dan Decepticons pecah. Film ini berfokus pada kisah dua sahabat, Orion Pax dan D-16, yang nantinya kita kenal sebagai Optimus Prime dan Megatron. Kisah ini akan mengungkap bagaimana persahabatan mereka yang erat berubah menjadi permusuhan yang mendalam. Kita akan menyaksikan bagaimana konflik dan perbedaan ideologi memisahkan mereka, serta bagaimana perang saudara meletus di Cybertron. Film ini tidak hanya menyajikan aksi robot yang spektakuler, tetapi juga menggali sisi emosional dari karakter-karakter utama, serta asal-usul dari konflik yang telah berlangsung selama berabad-abad di semesta Transformers."),
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    fun setUpBottomNavigation(){
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    // Kita sudah di MainActivity, jadi tidak perlu melakukan apa-apa
                    true
                }
                R.id.navigation_book -> {
                    Toast.makeText(this@MainActivity, "COMING SOON", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_discount -> {
                    Toast.makeText(this@MainActivity, "COMING SOON", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_profil -> {
                    Toast.makeText(this@MainActivity, "COMING SOON", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
