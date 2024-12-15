package com.example.proyekuas_dwians.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.models.FoodRecipes

class AdminFoodRecipeAdapter(
    private val onEditClicked: (FoodRecipes) -> Unit,
    private val onDeleteClicked: (FoodRecipes) -> Unit,
    private val onDetailClicked: (FoodRecipes) -> Unit
) : RecyclerView.Adapter<AdminFoodRecipeAdapter.RecipeViewHolder>() {

    private var foodRecipes: List<FoodRecipes> = listOf()

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.admin_recipe_title)
        val description: TextView = view.findViewById(R.id.recipe_description)
        val estCookingTime: TextView = view.findViewById(R.id.est_cooking_time)
        val estFoodCalories: TextView = view.findViewById(R.id.est_food_calories)
        val editButton: ImageButton = view.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
        val detailButton: ImageButton = view.findViewById(R.id.detail_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val foodRecipe = foodRecipes[position]
        holder.title.text = if (foodRecipe.recipeTitle.length > 26) {
            "${foodRecipe.recipeTitle.take(25)}..."
        } else {
            foodRecipe.recipeTitle
        }
        holder.description.text = if (foodRecipe.recipeDescription.length > 50) {
            "${foodRecipe.recipeDescription.take(50)}..."
        } else {
            foodRecipe.recipeDescription
        }
        holder.estCookingTime.text = foodRecipe.estimatedCookingTime
        holder.estFoodCalories.text = foodRecipe.estimatedCalories
        holder.editButton.setOnClickListener { onEditClicked(foodRecipe) }
        holder.deleteButton.setOnClickListener { onDeleteClicked(foodRecipe) }
        holder.detailButton.setOnClickListener { onDetailClicked(foodRecipe) }
    }

    override fun getItemCount(): Int = foodRecipes.size

    fun submitList(newRecipes: List<FoodRecipes>) {
        foodRecipes = newRecipes
        notifyDataSetChanged()
    }
}