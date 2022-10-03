package com.example.presentation.home_tab

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.common.SortOrder
import com.example.domain.common.getOrAwaitValue
import com.example.domain.model.product_related.ProductModel
import com.example.domain.model.product_related.Rating
import com.example.domain.repository.FakeStoreRepository
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

class HomeFragmentViewModelUnitTest {
    companion object {

        // our mocked fake data source
        @Mock
        val fakeStoreRepository: FakeStoreRepository = Mockito.mock(FakeStoreRepository::class.java)

        // our fake products list
        private val testingListOfProducts = listOf(
            ProductModel(
                "men's clothing",
                "perfect pack",
                1,
                "",
                109.25,
                Rating(120, 3.9),
                "No.1 backbpack"
            ),
            ProductModel(
                "jewelery",
                "ocean's pearl",
                2,
                "",
                695.0,
                Rating(400, 4.6),
                "Chain Bracelet"
            ),
            ProductModel(
                "men's clothing",
                "great outwear",
                3,
                "",
                55.99,
                Rating(500, 4.1),
                "Cotton Jacket"
            )
        )

        @BeforeClass
        @JvmStatic
        fun setUp() {
            // order here doesn't really matter, it is handled by the viewModel later anyway,
            // so as a default/placeholder the ascending order is used
            whenever(fakeStoreRepository.getAllProducts(SortOrder.asc)).thenReturn(
                Observable.just(
                    testingListOfProducts
                )
            )

            // rule for Rx to run synchronously
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }

    // rule for our liveData to run synchronously
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun viewModelItemCountShouldEqualThree() {
        val homeFragmentViewModel = HomeFragmentViewModel(fakeStoreRepository)
        val listOfAllProducts =
            homeFragmentViewModel.filteredListOfAllProducts.getOrAwaitValue().getOrNull()
        TestCase.assertTrue(
            "view model item count did not return all of the items",
            // the "3" comes from the amount of data in our fake listOfProducts
            (listOfAllProducts != null) && (listOfAllProducts.count() == 3)
        )
    }

    @Test
    fun viewModelItemSortOrderShouldChangeOnDemand() {
        val homeFragmentViewModel = HomeFragmentViewModel(fakeStoreRepository)
        homeFragmentViewModel.onSortOrderChanged(SortOrder.desc) // changing the sort order

        val listOfProductsFromVM = homeFragmentViewModel
            .filteredListOfAllProducts.getOrAwaitValue().getOrNull()

        TestCase.assertTrue(
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

        TestCase.assertTrue(
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

        TestCase.assertTrue(
            "a search query with sort order changed didn't return expected values",
            // why id == 3: we have only 2 products in our fake list that start with the letter 'c'
            // one of them is 'Chain Bracelet' (id 2) and the other one is 'Cotton Jacket' (id 3)
            // the sort order was changed to descending, so the list will contain: id3 id2
            // (in the exact order)
            (listOfProductsFromVM != null) && (listOfProductsFromVM[0].id == 3)
        )
    }
}

// To avoid having to use backticks for "when"
fun <T> whenever(methodCall: T): OngoingStubbing<T> =
    Mockito.`when`(methodCall)
