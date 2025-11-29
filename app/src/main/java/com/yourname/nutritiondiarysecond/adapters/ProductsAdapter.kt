package com.yourname.nutritiondiarysecond.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Product

class ProductsAdapter(
    private var products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.productName)
        val caloriesTextView: TextView = itemView.findViewById(R.id.productCalories)
        val nutritionTextView: TextView = itemView.findViewById(R.id.productNutrition)
        val cardView: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.productCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.nameTextView.text = product.name
        holder.caloriesTextView.text = "${product.caloriesPer100g} ккал/100г"
        holder.nutritionTextView.text = "Б: ${product.proteinPer100g}г, Ж: ${product.fatPer100g}г, У: ${product.carbsPer100g}г"

        // Подсветка выбранного продукта
        if (product.isSelected) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.selected_product_color))
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.default_product_color))
        }

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}