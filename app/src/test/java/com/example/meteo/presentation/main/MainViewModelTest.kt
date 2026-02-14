package com.example.meteo.presentation.main

import com.example.meteo.core.location.LocationProvider
import com.example.meteo.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `shows error when location unavailable`() = runTest {
        val repository = mockk<WeatherRepository>()
        every { repository.observeCachedCurrentWeather() } returns flowOf(null)

        val locationProvider = mockk<LocationProvider>()
        coEvery { locationProvider.getLastLocation() } returns null

        val vm = MainViewModel(repository, locationProvider)
        vm.refresh()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.uiState.value.error?.contains("Localisation") == true)
    }
}
