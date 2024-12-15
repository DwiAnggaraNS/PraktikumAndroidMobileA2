package com.example.proyekuas_dwians.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.models.FoodRecipes

class UserDataFoodRecipeAdapter(
    private val onSaveClicked: (FoodRecipes) -> Unit,
    private val onDetailClicked: (FoodRecipes) -> Unit
) : RecyclerView.Adapter<UserDataFoodRecipeAdapter.DataFoodRecipeViewHolder>() {

    private var foodRecipes: List<FoodRecipes> = emptyList()
    private val savedRecipeIds: MutableSet<String> = mutableSetOf()

    inner class DataFoodRecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.user_recipe_title)
        val description: TextView = view.findViewById(R.id.user_recipe_description)
        val estCookingTime: TextView = view.findViewById(R.id.user_est_cooking_time)
        val estFoodCalories: TextView = view.findViewById(R.id.user_est_food_calories)
        val savedButton: ImageButton = view.findViewById(R.id.btn_to_saved)
        val foodImage: ImageView = view.findViewById(R.id.food_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataFoodRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_card_recipe, parent, false)
        return DataFoodRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataFoodRecipeViewHolder, position: Int) {
        val foodRecipe = foodRecipes[position]
        holder.title.text = foodRecipe.recipeTitle.takeIf { it.length <= 26 } ?: "${foodRecipe.recipeTitle.take(25)}..."
        holder.description.text = foodRecipe.recipeDescription.takeIf { it.length <= 50 } ?: "${foodRecipe.recipeDescription.take(50)}..."
        holder.estCookingTime.text = foodRecipe.estimatedCookingTime
        holder.estFoodCalories.text = foodRecipe.estimatedCalories

        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(foodRecipe.recipeImageLink)
            .into(holder.foodImage)

        // Update the ImageButton src based on your condition
        if (savedRecipeIds.contains(foodRecipe.idRecipe)) {
            holder.savedButton.setImageResource(R.drawable.icon_saved_filled) // Use the filled button drawable
        } else {
            holder.savedButton.setImageResource(R.drawable.save_button) // Use the default button drawable
        }

        holder.savedButton.setOnClickListener {
            onSaveClicked(foodRecipe)
            if (savedRecipeIds.contains(foodRecipe.idRecipe)) {
                savedRecipeIds.remove(foodRecipe.idRecipe)
                holder.savedButton.setImageResource(R.drawable.save_button)
            } else {
                foodRecipe.idRecipe?.let { it1 -> savedRecipeIds.add(it1) }
                holder.savedButton.setImageResource(R.drawable.icon_saved_filled)
            }
        }
        holder.itemView.setOnClickListener { onDetailClicked(foodRecipe) }
    }

    override fun getItemCount(): Int = foodRecipes.size

    fun submitList(newRecipes: List<FoodRecipes>) {
        foodRecipes = newRecipes
        notifyDataSetChanged()
    }

    fun setSavedRecipes(savedRecipes: List<String>) {
        savedRecipeIds.clear()
        savedRecipeIds.addAll(savedRecipes)
        notifyDataSetChanged()
    }
}