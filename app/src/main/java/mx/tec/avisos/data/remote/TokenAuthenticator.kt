package mx.tec.avisos.data.remote

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/** Reintenta una sola vez una petición con un token renovado tras un 401. */
class TokenAuthenticator(
    private val tokenActual: () -> String?,
    private val refrescar: () -> String?
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val enviado = response.request.header("Authorization")
            ?.removePrefix("Bearer ") ?: return null
        if (response.request.url.encodedPath.endsWith("/auth/refresh")) return null
        if (response.priorResponse != null) return null

        // Si otra petición ya renovó el token, se reutiliza el resultado.
        val nuevo = synchronized(this) {
            val vigente = tokenActual()
            if (vigente != null && vigente != enviado) vigente else refrescar()
        } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $nuevo")
            .build()
    }
}
