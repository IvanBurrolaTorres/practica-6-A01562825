package mx.tec.avisos

import android.app.Application
import android.content.Context
import mx.tec.avisos.data.AvisosRepository
import mx.tec.avisos.data.SesionRepository
import mx.tec.avisos.data.local.SesionStore
import mx.tec.avisos.data.remote.AuthInterceptor
import mx.tec.avisos.data.remote.AvisosApi
import mx.tec.avisos.data.remote.Network

/**
 * El contenedor de dependencias: quién construye a quién, en un solo lugar.
 * Igual que en la Práctica 5. Hoy solo hay un repositorio; durante la práctica
 * va a aparecer el de la sesión, y la API va a necesitar el token que él guarda.
 */
class AppContainer(context: Context) {
    private val sesionStore = SesionStore(context)
    private val api: AvisosApi by lazy {
        Network.crearApi(interceptor = AuthInterceptor { sesionRepository.tokenActual() })
    }

    val sesionRepository: SesionRepository by lazy { SesionRepository(api, sesionStore) }
    val avisosRepository: AvisosRepository by lazy { AvisosRepository(api) }
}

/** Vive tanto como el proceso. Declarada en el manifiesto con `android:name`. */
class AvisosApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
