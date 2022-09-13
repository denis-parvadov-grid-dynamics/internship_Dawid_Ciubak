package com.example.presentation.home_tab

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.common.SortOrder
import com.example.domain.model.product_related.ProductModel
import com.example.presentation.BaseFragment
import com.example.presentation.R
import com.example.presentation.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpOrderSpinner()
        observeForChangesInApiData()
        handleSearchBar()
    }
    private fun setUpRecyclerView() {
        binding.productRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        // the adapter will be set whenever the api retrieves any data
    }

    private fun setUpOrderSpinner() {
        binding.sortOrderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> viewModel.onSortOrderChanged(SortOrder.asc) // latest option index = 0
                    1 -> viewModel.onSortOrderChanged(SortOrder.desc) // oldest option index = 1
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Not possible in our case, doesn't need handling
            }
        }
    }

    private fun handleSearchBar() {
        // set up to help with searching,
        // if the capitalisation mattered it would be more annoying to the user
        binding.searchForItemsEditText.filters = arrayOf(
            InputFilter { charSequence, _, _, _, _, _ ->
                charSequence.toString().lowercase().trim()
            }
        )

        binding.searchForItemsEditText.doOnTextChanged { text, start, before, count ->
            val searchQuery = text.toString()
            viewModel.onSearchQueryChanged(searchQuery)
        }
    }

    private fun observeForChangesInApiData() {
        viewModel.filteredListOfAllProducts.observe(viewLifecycleOwner) {
            it.onSuccess { listOfProducts ->
                updateRecyclersViewAdapterWithDataSet(listOfProducts)
            }
            it.onFailure { throwable ->
                Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecyclersViewAdapterWithDataSet(listOfProducts: List<ProductModel>) {
        binding.productRecyclerView.adapter = ProductRecyclerViewAdapter(listOfProducts) { position ->
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment)
        }
    }
}
