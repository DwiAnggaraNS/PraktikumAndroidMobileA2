package com.example.proyekuas_dwians.admin

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.network.ApiClient
import com.example.proyekuas_dwians.user.UserProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputRecipeDataActivity : AppCompatActivity() {

    private lateinit var recipeImageView: ImageView
    private lateinit var pickImageButton: Button
    private lateinit var deleteImageButton: Button
    private lateinit var saveRecipeButton: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var recipeTitle: TextInputEditText
    private lateinit var recipeDescription: TextInputEditText
    private lateinit var recipeIngredients: TextInputEditText
    private lateinit var recipeInstructions: TextInputEditText
    private lateinit var cookingTime: TextInputEditText
    private lateinit var calories: TextInputEditText

    private var selectedImageUri: Uri? = null // To store selected image URI
    private var isUploading = false // Prevent multiple submissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_recipe_data)

        // Initialize Views
        recipeImageView = findViewById(R.id.iv_recipe_image)
        pickImageButton = findViewById(R.id.btn_pick_image)
        deleteImageButton = findViewById(R.id.btn_delete_image)
        saveRecipeButton = findViewById(R.id.btn_save_recipe)
        progressBar = findViewById(R.id.progress_bar)

        recipeTitle = findViewById(R.id.et_recipe_title)
        recipeDescription = findViewById(R.id.et_recipe_description)
        recipeIngredients = findViewById(R.id.et_recipe_ingredients)
        recipeInstructions = findViewById(R.id.et_recipe_instructions)
        cookingTime = findViewById(R.id.et_cooking_time)
        calories = findViewById(R.id.et_calories)

        // Image Picker
        val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                recipeImageView.setImageURI(uri)
            }
        }

        pickImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        deleteImageButton.setOnClickListener {
            selectedImageUri = null
            recipeImageView.setImageResource(android.R.color.transparent)
        }

        saveRecipeButton.setOnClickListener {
            if (isUploading) return@setOnClickListener // Prevent multiple submissions
            if (checkAllFieldExceptImage()) {
                isUploading = true
                progressBar.visibility = View.VISIBLE
                uploadRecipe()
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        setUpBottomNavigation()
    }

    private fun checkAllFieldExceptImage(): Boolean {
        return recipeTitle.text.toString().isNotEmpty() &&
                recipeDescription.text.toString().isNotEmpty() &&
                recipeIngredients.text.toString().isNotEmpty() &&
                recipeInstructions.text.toString().isNotEmpty() &&
                cookingTime.text.toString().isNotEmpty() &&
                calories.text.toString().isNotEmpty()
    }

    private fun uploadRecipe() {
        val imagePath = selectedImageUri?.let { saveImageToExternalStorage(it) }
        saveRecipeToBackend(imagePath)
    }

    private fun saveImageToExternalStorage(uri: Uri): String? {
        val fileName = "recipe_${System.currentTimeMillis()}.jpg"
        val resolver = contentResolver
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Recipes")
        }

        return try {
            val uriSavedImage = resolver.insert(imageCollection, contentValues) ?: return null
            resolver.openOutputStream(uriSavedImage).use { outputStream ->
                resolver.openInputStream(uri).use { inputStream ->
                    inputStream?.copyTo(outputStream ?: return null)
                }
            }
            uriSavedImage.toString() // Return the saved image path
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image: ${e.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun saveRecipeToBackend(imagePath: String?) {
        val apiService = ApiClient.getInstance()

        val recipe = FoodRecipes(
            idRecipe = null,
            recipeTitle = recipeTitle.text.toString(),
            recipeImageLink = imagePath.orEmpty(),
            recipeDescription = recipeDescription.text.toString(),
            recipeIngredients = recipeIngredients.text.toString(),
            recipeInstructions = recipeInstructions.text.toString(),
            estimatedCookingTime = cookingTime.text.toString(),
            estimatedCalories = calories.text.toString(),
        )

        // Menggunakan method yang benar
        apiService.postNewRecipe(RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(recipe)))
            .enqueue(object : Callback<FoodRecipes> {
                override fun onResponse(call: Call<FoodRecipes>, response: Response<FoodRecipes>) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        isUploading = false
                        if (response.isSuccessful) {
                            Toast.makeText(this@InputRecipeDataActivity, "Recipe added successfully!", Toast.LENGTH_SHORT).show()
                            // Tambahkan delay sebelum finish
                            Handler(Looper.getMainLooper()).postDelayed({
                                startActivity(Intent(this@InputRecipeDataActivity, HomeAdminActivity::class.java))
                            }, 1000)
                        } else {
                            Toast.makeText(this@InputRecipeDataActivity, "Failed to save recipe!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<FoodRecipes>, t: Throwable) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        isUploading = false
                        Toast.makeText(this@InputRecipeDataActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@InputRecipeDataActivity, HomeAdminActivity::class.java))
                    true
                }
                R.id.navigation_add_data -> {
                    true
                }
                R.id.navigation_profile -> {
                    // Handle profile navigation
                    startActivity(Intent(this@InputRecipeDataActivity, AdminProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_add_data
    }

}
