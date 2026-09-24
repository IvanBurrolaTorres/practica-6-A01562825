package mx.tec.avisos.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mx.tec.avisos.data.local.SesionStore
import mx.tec.avisos.data.remote.AvisosApi
import mx.tec.avisos.data.remote.Credenciales
import mx.tec.avisos.data.remote.RefreshBody
import mx.tec.avisos.data.remote.toSesion
import mx.tec.avisos.domain.Sesion
import retrofit2.HttpException
import java.io.IOException

/** Única puerta a la sesión persistida. */
class SesionRepository(private val api: AvisosApi, private val store: SesionStore) {
    val sesion: Flow<Sesion?> = store.sesion

    suspend fun entrar(usuario: String, password: String) {
        store.guardar(api.login(Credenciales(usuario.trim().lowercase(), password)).toSesion())
    }

    suspend fun registrar(usuario: String, password: String, codigoProfesor: String) {
        val credenciales = Credenciales(
            usuario = usuario.trim().lowercase(),
            password = password,
            codigoProfesor = codigoProfesor.trim().ifEmpty { null }
        )
        store.guardar(api.register(credenciales).toSesion())
    }

    suspend fun salir() {
        val actual = store.sesion.first()
        store.borrar()
        if (actual != null) {
            try {
                api.logout(RefreshBody(actual.refreshToken))
            } catch (e: IOException) {
                // La sesión local ya está cerrada; la revocación remota se intentó.
            } catch (e: HttpException) {
                // El refresh podía estar revocado previamente.
            }
        }
    }

    fun tokenActual(): String? = runBlocking { store.sesion.first() }?.accessToken

    fun refrescarToken(): String? = runBlocking { refrescar() }

    /** Un refresh rechazado termina la sesión; un fallo de red la conserva. */
    private suspend fun refrescar(): String? {
        val actual = store.sesion.first() ?: return null
        return try {
            val nueva = api.refresh(RefreshBody(actual.refreshToken)).toSesion()
            store.guardar(nueva)
            nueva.accessToken
        } catch (e: HttpException) {
            if (e.code() == 401) store.borrar()
            null
        } catch (e: IOException) {
            null
        }
    }
}
