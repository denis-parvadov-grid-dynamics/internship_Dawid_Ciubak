package com.example.presentation.home_tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.product_related.ProductModel
import com.example.presentation.databinding.ShoppingListItemBinding

class ProductRecyclerViewAdapter(
    private val listOfProducts: List<ProductModel>,
    private val onProductSelected: (position: Int) -> Unit
) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ShoppingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductModel, position: Int) {
            binding.productCardView.setOnClickListener { onProductSelected(position) }
            binding.itemPriceTextView.text = "$${data.price}"
            binding.itemNameTextView.text = data.title
            binding.itemRatingCountTextView.text = "(${data.rating.count})"
            binding.itemRatingBar.rating = data.rating.rate.toFloat()
            Glide.with(binding.root).load(data.image).into(binding.productImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ShoppingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfProducts[position], position)
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }


}
