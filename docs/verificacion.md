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
