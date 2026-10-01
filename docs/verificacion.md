# Verificación de la práctica 6

La compilación `:app:assembleDebug` terminó correctamente. Las comprobaciones
de ejecución se hicieron en el emulador `Medium_Phone` con una cuenta de
alumno de demostración y una sesión de profesor ya presente en el emulador.
No se incluyen contraseñas ni tokens en este repositorio.

| Comprobación | Resultado observado |
| --- | --- |
| Cerrar el proceso y abrir la app otra vez | El tablón reapareció con la misma sesión de alumno. |
| DataStore con sesión | El archivo no contenía la secuencia `eyJ`; usuario y nombres de claves sí eran legibles. |
| Logcat con `HEADERS` activado temporalmente | `Authorization` salió redactada; no apareció un JWT. El código final usa `BASIC`. |
| Cuenta de alumno en `main` | Se mostró el tablón sin botón de publicación. |
| Rama `experimento-c2` | El botón apareció, pero el servidor rechazó `POST /avisos` con 403 y la pantalla mostró el mensaje sin cerrarse. |
| Acceso vencido | `POST /auth/refresh` respondió 200 y el tablón siguió abierto. |
| Refresh revocado | `POST /auth/refresh` respondió 401, apareció el login y el archivo de DataStore quedó en 0 bytes. |
| Salir desde la app | Apareció el login, el archivo quedó en 0 bytes y `sesionesActivas` del usuario de prueba bajó de 2 a 1. |

Los videos de `entregables/` muestran los flujos de la app y los resultados
observados. La cuenta de demostración no sustituye el registro personal del
estudiante con su matrícula exacta.

## La misma app contra el servidor propio del anexo

Después de entregar la práctica, la app se volvió a correr contra el módulo de
identidad del anexo (FastAPI + PostgreSQL, todo en Docker), cambiando solo
`BASE_URL`. Sirve para mostrar que el lado del servidor de la Práctica 6 se
puede sustituir por el propio sin tocar el cliente.

| Comprobación | Resultado observado |
| --- | --- |
| `POST /auth/login` contra el contenedor `api` | `200 OK`, y el tablón cargó. |
| `GET /api/avisos` contra el contenedor `api` | `200 OK`, con los avisos que se habían publicado en el servidor propio. |
| `GET /api/health` | `accessTokenSegundos: 300`, `refreshTokenSegundos: 604800`. |
| Refresco con el acceso vencido | El servidor vio `GET /avisos` 401 → `POST /auth/refresh` 200 → `GET /avisos` 200. El usuario nunca vio el login. |
| Stream SSE (`/api/avisos/stream`) | `event: aviso` por cada aviso nuevo; con `Last-Event-ID` reanuda sin repetir; al vencer el token cierra con `event: fin`. |
| Túnel HTTPS de Cloudflare | La app entró por `https://…trycloudflare.com` **sin** la configuración de cleartext de `src/debug/`. |
| La base de datos | `password_hash` empieza con `$argon2id$` en todas las filas; `sesiones.refresh_hash` guarda el SHA-256, nunca el token. |
| Rol desde el cliente | Registrarse mandando `"rol":"profesor"` devuelve `alumno`: lo decide el servidor. |

Capturas en [`capturas/`](capturas/):

- [`app-contra-servidor-local.png`](capturas/app-contra-servidor-local.png) — el tablón cargado, con el banner del usuario.
- [`docker-sostiene-el-servidor.png`](capturas/docker-sostiene-el-servidor.png) — `docker compose ps`, los logs viendo las peticiones de la app y la tabla `usuarios`.
- [`docker-evidencia.txt`](capturas/docker-evidencia.txt) — la misma salida en texto plano.

Estas capturas se tomaron con una copia local del anexo, no contra la API del
curso: nodice nada sobre `https://startdroid.com/api`, que es contra quien se
entregó y verifico la práctica.
