package com.example.meteo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteo.core.location.LocationProvider
import com.example.meteo.data.repository.WeatherRepository
import com.example.meteo.domain.model.CurrentWeather
import com.example.meteo.domain.model.DailyForecast
import com.example.meteo.domain.model.HourlyForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(isLoading = true))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeCachedCurrentWeather().collect { cached ->
                if (cached != null && _uiState.value.current == null) {
                    _uiState.value = _uiState.value.copy(current = cached, isLoading = false)
                }
            }
        }
        refresh()
    }

    fun onPermissionResult(result: Map<String, Boolean>) {
        if (result.values.any { it }) refresh() else {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Permission de localisation refusée"
            )
        }
    }

    fun refresh(forceNetwork: Boolean = false) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        val loc = locationProvider.getLastLocation()
        if (loc == null) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Localisation indisponible")
            return@launch
        }

        runCatching {
            repository.refresh(loc.first, loc.second)
        }.onSuccess { bundle ->
            _uiState.value = MainUiState(
                isLoading = false,
                current = bundle.current,
                hourly = bundle.hourly24h,
                daily = bundle.daily7d
            )
        }.onFailure {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = if (forceNetwork) "Pas d'internet ou API indisponible" else "Données en cache affichées"
            )
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val current: CurrentWeather? = null,
    val hourly: List<HourlyForecast> = emptyList(),
    val daily: List<DailyForecast> = emptyList(),
    val error: String? = null
)
