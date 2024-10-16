package com.example.tugastabstylepragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tugastabstylepragment.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewPager = requireActivity().findViewById(R.id.view_pager)

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.submit.setOnClickListener {
            handleRegistration()
        }
        binding.buttonLogin.setOnClickListener {
            viewPager.currentItem = 1 // Navigate to RegisterFragment
        }
    }

    private fun handleRegistration() {
        val username = binding.username.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (validateInputs(username, email, phone, password)) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
            requireActivity().finish() // Close the activity containing the fragment
        }
    }

    private fun validateInputs(username: String, email: String, phone: String, password: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}