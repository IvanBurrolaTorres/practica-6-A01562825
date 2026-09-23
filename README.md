# Práctica 6 — Avisos

Proyecto de la Práctica 6 de TC2007B, completado hasta el checkpoint del Bloque B.

Es un tablón de avisos con login contra la API del curso. El login ya crea una
sesión persistente en DataStore. Los tokens se cifran con Android Keystore y
la capa de red agrega el token de acceso a cada petición. La autorización y
la renovación automática del token corresponden a los Bloques C y D.

## Cómo empezar

1. Clona el repositorio y ábrelo en Android Studio.
2. Espera a que Gradle sincronice y corre la app: debes ver la pantalla de login.
3. Abre https://startdroid.com/consola.html y regístrate desde el navegador.
   Es la Parte 0 de la guía, y va antes de escribir código.
4. Continúa con los Bloques C y D de la guía: https://startdroid.com/practicas/avisos.html

## Cómo trabajar

Haz un commit en cada checkpoint de la guía:

    git add -A ; git commit -m "checkpoint a2"

Si algo se rompe sin remedio, `git restore .` te regresa al último checkpoint bueno.

Los experimentos que rompen el código a propósito van en una rama:

    git switch -c experimento-c2     # antes de romper nada
    git switch main                  # el código bueno vuelve solo

## Lo que nunca va en este repositorio

Tu contraseña, tus tokens, y el código de profesor. Ninguno de los tres se
escribe en el código: la contraseña se teclea, los tokens los guarda la app
cifrados en el teléfono, y el código se da en clase.

## Uso de IA

Todo commit con código generado por IA debe declararlo con un trailer
`Co-Authored-By`. Ver la política completa en la guía.

## Entrega

Ver la rúbrica en la guía.
