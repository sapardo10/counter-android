package com.cornershop.counterstest.features.suggestions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.counterstest.MainCoroutineRule
import com.cornershop.counterstest.utils.SingleLiveEventObserver
import com.cornershop.data.models.Suggestion
import com.cornershop.data.models.SuggestionsCategory
import com.cornershop.domain.IGetAllSuggestionsCategoriesUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class SuggestionsViewModelTest {

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SuggestionsViewModel

    private var mockActionsObserver: SingleLiveEventObserver<SuggestionsViewModelActions> =
        mock(SingleLiveEventObserver::class.java) as SingleLiveEventObserver<SuggestionsViewModelActions>
    private val mockGetAllSuggestionsCategoriesUseCase: IGetAllSuggestionsCategoriesUseCase =
        mock(IGetAllSuggestionsCategoriesUseCase::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = SuggestionsViewModel(mockGetAllSuggestionsCategoriesUseCase)
        viewModel.actions.observeForever(mockActionsObserver)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        verifyNoMoreInteractions(mockActionsObserver)
        verifyNoMoreInteractions(mockGetAllSuggestionsCategoriesUseCase)

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Get actions success`() {
        runBlocking {
            val suggestion = Suggestion(
                name = "reijg3ijosf"
            )
            val category = SuggestionsCategory(
                title = "kjsasad", suggestions = listOf(suggestion)
            )

            `when`(mockGetAllSuggestionsCategoriesUseCase()).thenReturn(listOf(category))

            val result = viewModel.getSuggestionsCategoriesViewModels()

            verify(mockGetAllSuggestionsCategoriesUseCase)()
            assertEquals(1, result.size)
            assertEquals(category.title, result[0].title)
            assertEquals(suggestion.name, result[0].suggestions[0].name)
        }
    }
}