package cz.eman.tddaplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.ReceiveTurbine
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
        assertSwitchFromLoadingStateToLoaded()
    }

    @Test
    fun `viewModel in loading state is restored from savedStateHandle, verify that stocks are loaded again`() =
        runTest {
            mockGetStocks(null, 5000)
            val savedStateHandle = SavedStateHandle()
            init(savedStateHandle)
            tested.model.test {
                skipItems(1)
            }

            mockGetStocks(stockItems)
            init(savedStateHandle)
            assertSwitchFromLoadingStateToLoaded()
        }

    @Test
    fun `viewModel in loaded state is restored from savedStateHandle`() = runTest {
        mockGetStocks(emptyList())
        val savedStateHandle = SavedStateHandle()
        init(savedStateHandle)
        tested.model.test {
            skipItems(2)
        }
        mockGetStocks(stockItems)
        init(savedStateHandle)
        tested.model.test {
            awaitItem() shouldBe StocksItemsModel(state = State.LOADED, emptyList())
            verifyThatNoFurtherItemWillBeEmitted()
        }
    }

    private suspend fun assertSwitchFromLoadingStateToLoaded() {
        tested.model.test {
            awaitItem() shouldBe StocksItemsModel(
                stockItems = null,
                state = State.LOADING,
            )
            awaitItem() shouldBe StocksItemsModel(
                stockItems = stockItems.map { it.copy() },
                state = State.LOADED
            )
        }
    }

    private fun mockGetStocks(stockItems: List<StockItem>?, delay: Long = 50L) {
        coEvery { repository.getStocks() } coAnswers {
            delay(delay)
            stockItems
        }
    }

    private suspend fun <T> ReceiveTurbine<T>.verifyThatNoFurtherItemWillBeEmitted() {
        val emittedItem = try {
            awaitItem()
        } catch (e: AssertionError) {
            // no - op
            null
        }
        assert(emittedItem == null) {
            "Expected no further item, but $emittedItem was received"
        }
    }

    private fun init(savedStateHandle: SavedStateHandle = SavedStateHandle()) {
        tested = StockItemsViewModel(savedStateHandle, repository)
    }


    @After
    fun afterTests() {
        Dispatchers.resetMain()
    }
}