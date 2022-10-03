package com.example.presentation.home_tab

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.model.product_related.ProductModel
import com.example.domain.model.product_related.Rating
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class ProductRecyclerViewAdapterUnitTest {
    companion object {
        // list of our fake products designed for testing purposes
        private val testingListOfProducts = listOf(
            ProductModel("men's clothing", "perfect pack", 1, "", 109.25, Rating(120, 3.9), "No.1 backbpack"),
            ProductModel("jewelery", "ocean's pearl", 2, "", 695.0, Rating(400, 4.6), "Chain Bracelet"),
            ProductModel("men's clothing", "great outwear", 3, "", 55.99, Rating(500, 4.1), "Cotton Jacket")
        )

        // recycler view adapter that holds the fake data
        val recyclerViewAdapter = ProductRecyclerViewAdapter(testingListOfProducts) {
            // onCLick: do nothing
        }

        @BeforeClass
        @JvmStatic
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            // rule for Rx to run synchronously
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }

    // rule for our liveData to run synchronously
    @Rule @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun productRecyclerViewAdapterItemCountShouldEqualThree() {
        assertEquals(
            "Product recycler view adapter itemCount did not return the expected value",
            3, // our fake products list contains only 3 items
            recyclerViewAdapter.itemCount
        )
    }
}
