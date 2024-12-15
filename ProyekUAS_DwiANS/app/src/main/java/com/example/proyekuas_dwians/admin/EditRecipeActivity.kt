package com.example.proyekuas_dwians.admin

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.databinding.ActivityEditRecipeBinding
import com.example.proyekuas_dwians.databinding.ActivityHomeAdminBinding
import com.example.proyekuas_dwians.databinding.ActivityHomeUserBinding
import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.network.ApiClient
import com.example.proyekuas_dwians.network.ApiServices
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecipeBinding
    private var selectedImageUri: Uri? = null
    private lateinit var progressBar: ProgressBar
    val apiService = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // View References
        val recipeTitleEditText = findViewById<TextInputEditText>(R.id.et_recipe_title)
        val recipeDescriptionEditText = findViewById<TextInputEditText>(R.id.et_recipe_description)
        val recipeIngredientsEditText = findViewById<TextInputEditText>(R.id.et_recipe_ingredients)
        val recipeInstructionsEditText = findViewById<TextInputEditText>(R.id.et_recipe_instructions)
        val cookingTimeEditText = findViewById<TextInputEditText>(R.id.et_cooking_time)
        val caloriesEditText = findViewById<TextInputEditText>(R.id.et_calories)
        val recipeImageView = findViewById<ImageView>(R.id.iv_recipe_image)
        val pickImageButton = findViewById<Button>(R.id.btn_pick_image)
        val deleteImageButton = findViewById<Button>(R.id.btn_delete_image)
        val saveButton = findViewById<Button>(R.id.btn_save_recipe)
        progressBar = findViewById(R.id.progress_bar)

        // Load data from intent
        val recipeId = intent.getStringExtra("RECIPE_ID")
        val oldImagePath = intent.getStringExtra("RECIPE_FOOD_IMAGE_LINK")

        // Set initial values
        recipeTitleEditText.setText(intent.getStringExtra("RECIPE_TITLE"))
        recipeDescriptionEditText.setText(intent.getStringExtra("RECIPE_DESCRIPTION"))
        recipeIngredientsEditText.setText(intent.getStringExtra("RECIPE_INGREDIENTS"))
        recipeInstructionsEditText.setText(intent.getStringExtra("RECIPE_INSTRUCTIONS"))
        cookingTimeEditText.setText(intent.getStringExtra("RECIPE_EST_COOKING_TIME"))
        caloriesEditText.setText(intent.getStringExtra("RECIPE_EST_CALORIES"))

        oldImagePath?.let {
            recipeImageView.setImageURI(Uri.parse(it))
        }

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

        saveButton.setOnClickListener {
            // Show progress bar
            progressBar.visibility = View.VISIBLE

            // Delete old image if exists
            oldImagePath?.let {
                deleteImageFromExternalStorage(Uri.parse(it))
            }

            // Save new image to external storage
            val newImagePath = selectedImageUri?.let { uri ->
                saveImageToExternalStorage(uri)
            }

            // Prepare data for API
            val jsonBody = JSONObject().apply {
                put("recipe_title", recipeTitleEditText.text.toString())
                put("recipe_description", recipeDescriptionEditText.text.toString())
                put("recipe_ingredients", recipeIngredientsEditText.text.toString())
                put("recipe_instructions", recipeInstructionsEditText.text.toString())
                put("estimated_cooking_time", cookingTimeEditText.text.toString())
                put("estimated_calories", caloriesEditText.text.toString())
                put("recipe_image_link", newImagePath ?: oldImagePath) // Use new or old image path
            }
            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                jsonBody.toString()
            )

            // Call API
            recipeId?.let { id ->
                apiService.updateRecipe(id, requestBody)
                    .enqueue(object : Callback<FoodRecipes> {
                        override fun onResponse(call: Call<FoodRecipes>, response: Response<FoodRecipes>) {
                            progressBar.visibility = View.GONE
                            if (response.isSuccessful) {
                                Toast.makeText(this@EditRecipeActivity, "Recipe updated successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@EditRecipeActivity, "Failed to update recipe", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<FoodRecipes>, t: Throwable) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@EditRecipeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

        with(binding){
            backToHome.setOnClickListener {
                startActivity(Intent(this@EditRecipeActivity, HomeAdminActivity::class.java))
            }
        }
    }

    private fun deleteImageFromExternalStorage(imageUri: Uri): Boolean {
        return try {
            val resolver = contentResolver
            if (resolver.delete(imageUri, null, null) > 0) {
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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
            uriSavedImage.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
