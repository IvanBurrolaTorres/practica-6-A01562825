package mx.tec.avisos

import android.app.Application
import android.content.Context
import mx.tec.avisos.data.AvisosRepository
import mx.tec.avisos.data.SesionRepository
import mx.tec.avisos.data.local.Cifrador
import mx.tec.avisos.data.local.SesionStore
import mx.tec.avisos.data.remote.AuthInterceptor
import mx.tec.avisos.data.remote.AvisosApi
import mx.tec.avisos.data.remote.Network


class AppContainer(context: Context) {
    private val sesionStore = SesionStore(context, Cifrador())
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
