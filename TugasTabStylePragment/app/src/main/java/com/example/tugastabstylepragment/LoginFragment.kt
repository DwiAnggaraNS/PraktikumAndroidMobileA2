package com.example.tugastabstylepragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tugastabstylepragment.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewPager = requireActivity().findViewById(R.id.view_pager)

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.buttonRegister.setOnClickListener {
            viewPager.currentItem = 0 // Navigate to RegisterFragment
        }

        binding.loginBang.setOnClickListener {
            handleLoginData()
        }
    }

    private fun handleLoginData() {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()
        if (validateInputs(username, password)){
            navigateToHome(username)
        }
    }

    private fun navigateToHome(username: String) {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        requireActivity().finish() // Close the activity containing the fragment
    }

    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isEmpty()  || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}