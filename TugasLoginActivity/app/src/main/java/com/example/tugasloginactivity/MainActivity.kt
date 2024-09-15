package com.example.tugasloginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tugasloginactivity.databinding.ActivityMainBinding
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            with(binding) {
                submit.setOnClickListener {
                    val username = username.text.toString()
                    if (username.isNotEmpty()) {
                        Toast.makeText(this@MainActivity, "Welcome $username !", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Please enter a username", Toast.LENGTH_SHORT).show()
                    }
                }

                buttonRegister.setOnClickListener {
                    Toast.makeText(this@MainActivity, "COMING SOON", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}