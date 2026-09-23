# Bitácora · Práctica 6

## Ejercicio 0 · Qué es secreto

| Dato | ¿Se guarda? | Protección |
| --- | --- | --- |
| Contraseña | No | Solo se usa para entrar o registrar; se borra del estado del login cuando la operación tiene éxito. |
| Token de acceso | Sí | Cifrado con una llave del Android Keystore y guardado en la carpeta privada mediante DataStore. |
| Token de refresco | Sí | Cifrado igual que el de acceso; nunca se muestra en la interfaz ni en logs. |
| Usuario y rol | Sí | En la carpeta privada de la app. El rol local solo controla la interfaz; el servidor decide permisos. |
| Código de profesor | No | Solo se envía durante el registro y se borra del estado del login cuando la operación tiene éxito. |
| Fecha de expiración | Sí | En DataStore; no es un secreto y permite saber cuándo vence el acceso. |

## Ejercicio B3 · Cuatro fugas fuera de DataStore

| Salida posible | Medida |
| --- | --- |
| Cabecera `Authorization` en Logcat | `HttpLoggingInterceptor` usa `BASIC` y redacta esa cabecera incluso si se cambia a `HEADERS`. |
| Respaldo de la carpeta privada | El manifiesto usa `allowBackup="false"`; además, la llave del Keystore no viaja con el archivo. |
| URL con token en parámetros | El token se envía solo en la cabecera `Authorization`, nunca en la ruta ni en el query string. |
| Pantalla, captura o portapapeles | La interfaz no muestra ni copia tokens; solo presenta el usuario y el rol. |

Estas respuestas son conceptuales. La inspección del archivo y del Logcat requiere una sesión real en un emulador o dispositivo.
