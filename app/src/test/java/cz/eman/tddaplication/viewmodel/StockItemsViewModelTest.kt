package cz.eman.tddaplication.viewmodel

import app.cash.turbine.test
import cz.eman.tddaplication.model.State
import cz.eman.tddaplication.model.StockItem
import cz.eman.tddaplication.model.StocksItemsModel
import cz.eman.tddaplication.repository.StocksRepository
import kotlinx.coroutines.test.runTest
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockItemsViewModelTest {

    private val stockItems: List<StockItem> = listOf(
        StockItem(
            id = "sjdjfkdlkfjf",
            name = "stock item 1"
        )
    )

    private val repository: StocksRepository = mockk()
    private lateinit var tested: StockItemsViewModel

    @Before
    fun beforeTests() {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `viewModel is in error state when loading stocks from repository fails`() = runTest {
        mockGetStocks(null)
        init()
        tested.model.test {
            awaitItem() shouldBe StocksItemsModel(
                stockItems = null,
                state = State.LOADING,
            )
            awaitItem() shouldBe StocksItemsModel(
                stockItems = null,
                state = State.ERROR
            )
        }
    }

    @Test
    fun `viewModel is in loaded state when loading stocks from repository succeed`() = runTest {
        mockGetStocks(stockItems)
        init()
        tested.model.test {
            skipItems(1)
            awaitItem() shouldBe StocksItemsModel(
                state = State.LOADED,
                stockItems = stockItems.map { it.copy() })
        }
    }

    private fun mockGetStocks(stockItems: List<StockItem>?) {
        coEvery { repository.getStocks() } coAnswers {
            delay(50)
            stockItems
        }
    }

    private fun init() {
        tested = StockItemsViewModel(repository)
    }


    @After
    fun afterTests() {
        Dispatchers.resetMain()
    }
}