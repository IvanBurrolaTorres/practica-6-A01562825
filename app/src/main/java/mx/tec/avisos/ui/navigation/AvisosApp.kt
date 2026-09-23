package mx.tec.avisos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.avisos.ui.components.CargandoView
import mx.tec.avisos.ui.screens.LoginScreen
import mx.tec.avisos.ui.state.AppViewModelProvider
import mx.tec.avisos.ui.state.LoginViewModel
import mx.tec.avisos.ui.state.SesionEstado
import mx.tec.avisos.ui.state.SesionViewModel

/**
 * La raíz de la app. Hoy solo existe el login: no hay sesión que consultar.
 * Durante la práctica esto se convierte en un `when` sobre la sesión, y el
 * tablón (`AvisosNavHost`) aparece solo cuando hay una.
 */
@Composable
fun AvisosApp() {
    val sesionViewModel: SesionViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val estado by sesionViewModel.estado.collectAsStateWithLifecycle()

    when (val actual = estado) {
        SesionEstado.Cargando -> CargandoView()
        SesionEstado.Anonimo -> {
            val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LoginScreen(
                uiState = loginViewModel.uiState,
                onUsuarioChange = loginViewModel::onUsuarioChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onCodigoProfesorChange = loginViewModel::onCodigoProfesorChange,
                onAlternarModo = loginViewModel::alternarModo,
                onEnviar = loginViewModel::enviar
            )
        }
        is SesionEstado.Activa -> AvisosNavHost(
            sesion = actual.sesion,
            onSalir = sesionViewModel::salir
        )
    }
}
