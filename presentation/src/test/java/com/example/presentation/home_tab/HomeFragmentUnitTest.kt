package com.example.presentation.home_tab

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.common.SortOrder
import com.example.domain.common.getOrAwaitValue
import com.example.domain.model.product_related.ProductModel
import com.example.domain.model.product_related.Rating
import com.example.domain.model.user_related.ApiResponse
import com.example.domain.model.user_related.api_model.ApiUser
import com.example.domain.repository.FakeStoreRepository
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class HomeFragmentUnitTest {
    companion object {
        // list of our fake products designed for testing purposes
        val testingListOfProducts = listOf(
            ProductModel("men's clothing", "perfect pack", 1, "", 109.25, Rating(120, 3.9), "No.1 backbpack"),
            ProductModel("jewelery", "ocean's pearl", 2, "", 695.0, Rating(400, 4.6), "Chain Bracelet"),
            ProductModel("men's clothing", "great outwear", 3, "", 55.99, Rating(500, 4.1), "Cotton Jacket")
        )

        // our fake data source
        val fakeStoreRepository = object : FakeStoreRepository {
            override fun getAllProducts(sort: SortOrder): Observable<List<ProductModel>> {
                return Observable.just(testingListOfProducts)
            }

            override fun updateUser(user: ApiUser): Completable {
                TODO("Not yet implemented") // not used on Home Fragment
            }

            override fun loginUser(loginCredentials: LoginCredentialsWrapper): Single<ApiResponse> {
                TODO("Not yet implemented") // not used on Home Fragment
            }
        }

        // recycler view adapter that holds the fake data
        val recyclerViewAdapter = ProductRecyclerViewAdapter(testingListOfProducts) {
            // onCLick: do nothing
        }

        @BeforeClass
        @JvmStatic
        fun setUp() {
            // rule for Rx to run synchronously
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }

    // rule for our liveData to run synchronously
    @Rule @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun viewModelItemCountShouldEqualThree() {
        val homeFragmentViewModel = HomeFragmentViewModel(fakeStoreRepository)
        val listOfAllProducts = homeFragmentViewModel.filteredListOfAllProducts.getOrAwaitValue().getOrNull()
        assertTrue(
            "view model item count did not return all of the items",
            // the "3" comes from the amount of data in our fake listOfProducts
            (listOfAllProducts != null) && (listOfAllProducts.count() == 3)
        )
    }

    @Test
    fun productRecyclerViewAdapterItemCountShouldEqualThree() {
        assertEquals(
            "Product recycler view adapter itemCount did not return the expected value",
            3, // our fake products list contains only 3 items
            recyclerViewAdapter.itemCount
        )
    }

    @Test
    fun viewModelItemSortOrderShouldChangeOnDemand() {
        val homeFragmentViewModel = HomeFragmentViewModel(fakeStoreRepository)
        homeFragmentViewModel.onSortOrderChanged(SortOrder.desc) // changing the sort order

        val listOfProductsFromVM = homeFragmentViewModel
            .filteredListOfAllProducts.getOrAwaitValue().getOrNull()

        assertTrue(
            "A change in sort order did not change the data sequence",
            // after changing the sort order to DESC, the first item (index 0) should have the id == 3
            (listOfProductsFromVM != null) && (listOfProductsFromVM[0].id == 3)
        )
    }

    @Test
    fun viewModelSearchQueryShouldChangeTheItemCount() {
        // viewModel instance isn't shared between tests, because every test can change the
        // data values of certain variables (one test could cause error in another)
        val homeFragmentViewModel = HomeFragmentViewModel(fakeStoreRepository)
        homeFragmentViewModel.onSearchQueryChanged("c") // we have 2 items that start with the letter 'c'
        val listOfProductsFromVM = homeFragmentViewModel
            .filteredListOfAllProducts.getOrAwaitValue().getOrNull()

        assertTrue(
            "A search query did not return the expected amount of items",
            (listOfProductsFromVM != null) && (listOfProductsFromVM.size == 2)
        )
    }

    @Test
    fun viewModelSearchQueryWithSortOrderChangedShouldLimitTheResultsAndSortThemCorrectly() {
        val homeFragmentViewModel = HomeFragmentViewModel(fakeStoreRepository)
        homeFragmentViewModel.onSearchQueryChanged("c")
        homeFragmentViewModel.onSortOrderChanged(SortOrder.desc)
        val listOfProductsFromVM = homeFragmentViewModel
            .filteredListOfAllProducts.getOrAwaitValue().getOrNull()

        assertTrue(
            "a search query with sort order changed didn't return expected values",
            // why id == 3: we have only 2 products in our fake list that start with the letter 'c'
            // one of them is 'Chain Bracelet' (id 2) and the other one is 'Cotton Jacket' (id 3)
            // the sort order was changed to descending, so the list will contain: id3 id2
            // (in the exact order)
            (listOfProductsFromVM != null) && (listOfProductsFromVM[0].id == 3)
        )
    }
}
