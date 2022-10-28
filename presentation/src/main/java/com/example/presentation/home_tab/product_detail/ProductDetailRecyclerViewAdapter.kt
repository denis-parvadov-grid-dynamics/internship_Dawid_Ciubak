package com.example.presentation.home_tab.product_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.presentation.databinding.ProductDetailRecyclerViewImageBinding

class ProductDetailRecyclerViewAdapter(
    private val listOfProductImagesEntites: List<ProductRecyclerViewImage>
) : RecyclerView.Adapter<ProductDetailRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ProductDetailRecyclerViewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.imageView.setOnClickListener {
                listOfProductImagesEntites.forEach { it.isSelected = false }
                listOfProductImagesEntites[position].isSelected = true
                notifyDataSetChanged()
            }
            if (listOfProductImagesEntites[position].isSelected) {
                binding.imageView.alpha = 1f
            } else {
                binding.imageView.alpha = 0.5f
            }
            Glide.with(binding.root)
                .load(listOfProductImagesEntites[position].image)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductDetailRecyclerViewImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfProductImagesEntites.size
    }
}
