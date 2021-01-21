package com.androrier.buttontoaction.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androrier.buttontoaction.interfaces.OnActionSelectedListener
import com.androrier.buttontoaction.repository.FakeActionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class MainViewModelTest {
    @InjectMocks     //This will inject the "eventListener" mock into your "viewModel" instance.
    private lateinit var viewModel: MainViewModel
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //This will create a mock of OnActionSelected
    @Mock
    var eventListener: OnActionSelectedListener? = null



    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup(){
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = MainViewModel(FakeActionRepository(), null)
        MockitoAnnotations.initMocks(this);
        viewModel.initViewModel(eventListener!!)
    }

    @Test
    fun `ViewModel loads data correctly`() =
    runBlockingTest{
        val action = viewModel.getNextAction()
        assertEquals("toast", action?.type)
    }

    @Test
    fun `ViewModel callback tests`() =
        runBlockingTest{
            viewModel.onActionButtonPressed()
            Thread.sleep(500)
            verify(eventListener, times(1))?.onToastAction();

            viewModel.onActionButtonPressed()
            Thread.sleep(500)
            verify(eventListener, times(1))?.onNotificationAction();
        }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}