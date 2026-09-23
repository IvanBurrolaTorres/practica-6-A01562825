package mx.tec.avisos.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mx.tec.avisos.domain.Rol
import mx.tec.avisos.domain.Sesion

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion")

/** DataStore conserva la sesión; los tokens se cifran con una llave del Keystore. */
class SesionStore(private val context: Context, private val cifrador: Cifrador) {
    private object Llaves {
        val USUARIO = stringPreferencesKey("usuario")
        val ROL = stringPreferencesKey("rol")
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
        val EXPIRA_EN = longPreferencesKey("expira_en")
    }

    val sesion: Flow<Sesion?> = context.dataStore.data.map { prefs ->
        val usuario = prefs[Llaves.USUARIO] ?: return@map null
        val access = prefs[Llaves.ACCESS]?.let(cifrador::descifrar) ?: return@map null
        val refresh = prefs[Llaves.REFRESH]?.let(cifrador::descifrar) ?: return@map null
        Sesion(
            usuario = usuario,
            rol = Rol.de(prefs[Llaves.ROL] ?: "alumno"),
            accessToken = access,
            refreshToken = refresh,
            expiraEn = prefs[Llaves.EXPIRA_EN] ?: 0L
        )
    }

    suspend fun guardar(sesion: Sesion) {
        context.dataStore.edit { prefs ->
            prefs[Llaves.USUARIO] = sesion.usuario
            prefs[Llaves.ROL] = sesion.rol.name.lowercase()
            prefs[Llaves.ACCESS] = cifrador.cifrar(sesion.accessToken)
            prefs[Llaves.REFRESH] = cifrador.cifrar(sesion.refreshToken)
            prefs[Llaves.EXPIRA_EN] = sesion.expiraEn
        }
    }

    suspend fun borrar() {
        context.dataStore.edit { it.clear() }
    }
}
