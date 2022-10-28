package com.example.presentation.home_tab.product_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.common.SortOrder
import com.example.domain.model.product_related.ProductModel
import com.example.domain.repository.FakeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val apiRepository: FakeStoreRepository
) : ViewModel() {
    var holderOfAllProducts = MutableLiveData<List<ProductModel>>()

    init {
        setUp()
    }

    private fun setUp() {
        apiRepository
            .getAllProducts(SortOrder.asc)
            .subscribeOn(Schedulers.io())
            .subscribe({ listOfProducts ->
                holderOfAllProducts.postValue(listOfProducts)
            }, {})
    }
}
