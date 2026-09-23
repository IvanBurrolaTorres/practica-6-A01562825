package mx.tec.avisos.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/** Agrega el token actual a las peticiones desde el hilo de OkHttp. */
class AuthInterceptor(private val token: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val actual = token() ?: return chain.proceed(chain.request())
        val firmada = chain.request().newBuilder()
            .header("Authorization", "Bearer $actual")
            .build()
        return chain.proceed(firmada)
    }
}
