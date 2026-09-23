package mx.tec.avisos.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.tec.avisos.data.SesionRepository
import mx.tec.avisos.domain.Sesion

sealed interface SesionEstado {
    data object Cargando : SesionEstado
    data object Anonimo : SesionEstado
    data class Activa(val sesion: Sesion) : SesionEstado
}

class SesionViewModel(private val repository: SesionRepository) : ViewModel() {
    val estado: StateFlow<SesionEstado> = repository.sesion
        .map { sesion -> if (sesion == null) SesionEstado.Anonimo else SesionEstado.Activa(sesion) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SesionEstado.Cargando
        )

    fun salir() {
        viewModelScope.launch { repository.salir() }
    }
}
