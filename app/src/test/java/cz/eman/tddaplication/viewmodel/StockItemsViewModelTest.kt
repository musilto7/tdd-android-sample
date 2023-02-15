package cz.eman.tddaplication.viewmodel

import cz.eman.tddaplication.repository.StocksRepository
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Test

class StockItemsViewModelTest {

    private val repository: StocksRepository = mockk()
    private lateinit var tested: StockItemsViewModel

    @Test
    fun `viewModel is in loading state after initialization`() {
        tested = StockItemsViewModel(repository)
        tested.model.value.isLoading shouldBe true
    }
}