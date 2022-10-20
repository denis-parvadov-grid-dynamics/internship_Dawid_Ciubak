package com.example.presentation.home_tab.product_detail

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.domain.model.product_related.ProductModel
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.ProductDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment :
    BaseFragment<ProductDetailFragmentBinding>(R.layout.product_detail_fragment) {

    private val viewModel: ProductDetailViewModel by viewModels()
    var holderOfAllProducts = listOf<ProductModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProductDataIntoUi(getTheSelectedProduct())
        setUpAmount()
    }

    private fun getTheSelectedProduct(): ProductModel {
        // we are sure that the product IS in the arguments, thus the usage of "!!"
        return arguments!!.getParcelable("selectedProduct")!!
    }

    private fun loadProductDataIntoUi(product: ProductModel) {
        // picture
        Glide.with(requireContext()).load(product.image).into(binding.productImageView)

        // rating
        binding.productRatingBar.rating = product.rating.rate.toFloat()
        binding.productRatingCountTextView.text =
            getString(R.string.reviewCount, product.rating.count)
        binding.readAllReviewsTextView.text =
            getString(R.string.readAllReviews, product.rating.count)
        binding.readAllReviewsTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        // title and price
        binding.productTitleTextView.text = product.title
        binding.productPriceTextView.text = getString(R.string.productPrice, product.price)

        // recycler view with photos
        // filling the list with 10 images of the same thing
        val listOfProductImagesEntities =
            MutableList(10) { ProductRecyclerViewImage(product.image) }

        // after generating the list, mark the first item as "selected"
        listOfProductImagesEntities[0].isSelected = true

        binding.photosRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.photosRecyclerView.adapter = ProductDetailRecyclerViewAdapter(
            listOfProductImagesEntities
        )

        viewModel.holderOfAllProducts.observe(viewLifecycleOwner) {
            // whenever we receive the list of products from our api, update the suggestions
            // if the list is not updated, it's blank
            val listOfItems = it
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.fragment_dropdown_menu_card,
                // get just the titles for the suggestions
                listOfItems.map { item -> item.title }
            )
            binding.autoCompleteTextView.setAdapter(adapter)
            binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
                navigateToProductDetailFragment(
                    // idk why "i" is the position of the item in the suggestion list and NOT the
                    // position at the provided list of items, but because of that, I had to use this:
                    listOfItems.find { item -> item.title == adapterView.getItemAtPosition(i) }
                    // just to get the position of the item in our listOfItems
                )
            }
        }
    }

    private fun setUpAmount() {
        binding.addOneToAmountImageButton.setOnClickListener {
            val amount = binding.amountTextView.text.toString().toInt() + 1
            binding.amountTextView.text = amount.toString()
        }

        binding.subtractOneFromAmountImageButton.setOnClickListener {
            var amount = binding.amountTextView.text.toString().toInt() - 1

            // just a check to prevent adding 0 or -1 products to the cart
            if (amount < 1) {
                amount = 1
            }
            binding.amountTextView.text = amount.toString()
        }
    }

    private fun navigateToProductDetailFragment(selectedProduct: ProductModel?) {
        val bundle = Bundle()
        bundle.putParcelable(
            "selectedProduct",
            selectedProduct
        )
        findNavController().navigate(R.id.action_productDetailFragment_self, bundle)
    }
}

// this data class is related only to this screen (this file and ProductDetailRecyclerViewAdapter),
// that's why it is placed here
data class ProductRecyclerViewImage(
    val image: String,
    var isSelected: Boolean = false
)
