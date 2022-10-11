package com.example.presentation.home_tab

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.common.SortOrder
import com.example.domain.model.product_related.ProductModel
import com.example.domain.repository.FakeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val apiRepository: FakeStoreRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val holderOfAllProducts = MutableLiveData<List<ProductModel>>()
    val filteredListOfAllProducts = MutableLiveData<Result<List<ProductModel>>>()
    val recyclerViewScrollState = Bundle()

    init {
        getAllProducts()
    }

    // initial setup, should be called once only
    private fun getAllProducts() {
        return apiRepository
            .getAllProducts(SortOrder.asc) // default order
            .subscribeOn(Schedulers.io())
            .subscribe({ listOfProducts ->
                holderOfAllProducts.postValue(listOfProducts)
                filteredListOfAllProducts.postValue(Result.success(listOfProducts))
            }, {
                filteredListOfAllProducts.postValue(Result.failure(it))
            }).let { compositeDisposable.add(it) }
    }

    fun onRefresh(sortOrder: SortOrder, itemTitleStartsWith: String) {
        apiRepository.getAllProducts(sortOrder).subscribeOn(Schedulers.io()).subscribe({
            holderOfAllProducts.postValue(it)
            onSortOrderChanged(sortOrder)
            onSearchQueryChanged(itemTitleStartsWith)
        }, {
            filteredListOfAllProducts.postValue(Result.failure(it))
        })
    }

    fun onSortOrderChanged(sortOrder: SortOrder) {
        val filteredList = filteredListOfAllProducts.value?.getOrNull()
        if (filteredList != null) {
            filteredListOfAllProducts.postValue(
                when (sortOrder) {
                    SortOrder.desc -> Result.success(filteredList.sortedByDescending { it.id })
                    SortOrder.asc -> Result.success(filteredList.sortedBy { it.id })
                }
            )
        }
    }

    fun onSearchQueryChanged(itemTitleStartsWith: String) {
        val listOfProducts = holderOfAllProducts.value
        if (listOfProducts != null) {
            filteredListOfAllProducts.postValue(
                Result.success(
                    listOfProducts.filter { it.title.lowercase().startsWith(itemTitleStartsWith) }
                )
            )
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
