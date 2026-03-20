# Reflexion sobre el uso de la IA en el desarrollo del proyecto

## Juego: Simon Dice (Opcion 2)
**Aplicacion:** QGame - Desarrollada con Jetpack Compose + Kotlin

---

## 1. Que prompts utilizo para solicitar ayuda a la IA?

A lo largo del desarrollo utilice la IA principalmente como herramienta de consulta e investigacion. Algunos de los prompts que use fueron:

- "Como configuro Jetpack Compose en un proyecto Android que solo tiene las dependencias basicas de AndroidX?"
- "Cual es la forma recomendada de manejar el estado de un juego por turnos usando ViewModel y StateFlow en Compose?"
- "Como puedo implementar navegacion entre pantallas pasando parametros como el nombre del jugador con Navigation Compose?"
- "Que API de animacion de Compose me conviene usar para animar el cambio de color de un boton, animateColorAsState o Animatable?"
- "Como hago para que un boton no sea clickeable mientras se esta mostrando una secuencia animada?"
- "Como estructuro un proyecto de Compose separando modelos, viewmodel, pantallas y componentes de forma limpia?"

En general, mis prompts iban orientados a resolver dudas puntuales sobre APIs de Compose y Kotlin que no habia usado antes, y a consultar buenas practicas de arquitectura.

---

## 2. Que sugerencias o respuestas de la IA fueron utiles para el desarrollo del proyecto?

- **Estructura del proyecto:** Me sugirio separar el codigo en paquetes claros (model, viewmodel, navigation, ui/screens, ui/components, ui/theme), lo cual me ayudo a mantener el proyecto organizado desde el inicio.

- **StateFlow en vez de mutableStateOf:** Me explico que para la logica del juego era mejor usar `StateFlow` en el ViewModel en lugar de `mutableStateOf`, porque asi la logica queda desacoplada de Compose y es mas testeable.

- **Fases del juego como enum:** Me sugirio modelar las fases del juego (`SHOWING_SEQUENCE`, `PLAYER_INPUT`, `GAME_OVER`) como un enum, lo cual simplifico mucho la logica de validacion y el control del flujo.

- **Animaciones con animateColorAsState:** Me recomendo usar `animateColorAsState` con `tween` para las transiciones de color de los botones Simon, y `animateFloatAsState` para un efecto sutil de escala. El resultado visual quedo muy bien.

- **Navegacion con popUpTo:** Me aclaro como usar `popUpTo` con `inclusive = true` para evitar que el usuario pudiera regresar a una partida ya terminada con el boton de atras.

---

## 3. Que aspectos del codigo tuvo que corregir o modificar manualmente?

- **Tiempos de la secuencia:** Probe varios valores para la duracion de iluminacion de cada boton y la pausa entre ellos hasta encontrar un ritmo que se sintiera natural (600ms encendido, 350ms entre colores).

- **Manejo del backstack de navegacion:** Tuve que ajustar la logica de `popUpTo` en varias transiciones para que la navegacion entre Game y Result no dejara pantallas huerfanas en el stack.

- **Codificacion del nombre del jugador:** Al pasar el nombre como argumento en la ruta de navegacion, tuve que agregar `Uri.encode()` y `Uri.decode()` para que nombres con espacios o caracteres especiales no causaran errores.

- **Padding con edge-to-edge:** Al activar `enableEdgeToEdge()`, el contenido se iba detras de la barra de estado. Ajuste los paddings de cada pantalla para que todo quedara visible correctamente.

- **Deshabilitacion de botones durante la animacion:** Inicialmente los botones respondian a taps mientras se mostraba la secuencia. Agregue la condicion `enabled = (phase == PLAYER_INPUT)` tanto en los composables como en el ViewModel para asegurar que no se pudiera interactuar fuera de turno.

---

## 4. Que aprendio del proceso de trabajar con IA?

- **Es una excelente herramienta de consulta:** Funciona como una documentacion interactiva. En vez de buscar en multiples paginas de la documentacion oficial, pude hacer preguntas directas y obtener respuestas con ejemplos aplicados a mi caso.

- **No se debe copiar sin entender:** Cada sugerencia que recibi la revise y la adapte a mi proyecto. Hubo casos donde la respuesta era correcta conceptualmente pero no encajaba exactamente con mi implementacion, asi que tuve que modificarla.

- **Ayuda a explorar opciones:** Me permitio comparar rapidamente diferentes enfoques (por ejemplo, `StateFlow` vs `mutableStateOf`, o `NavHost` vs navegacion manual) y elegir el que mejor se adaptaba a los requisitos del juego.

- **Acelera el aprendizaje de APIs nuevas:** Al no haber trabajado mucho con Compose antes, la IA me ayudo a entender conceptos como la recomposicion, el manejo de estado reactivo y las animaciones declarativas de forma mas rapida que leyendo solo la documentacion.

- **El criterio propio es fundamental:** La IA da sugerencias, pero las decisiones de diseno, la experiencia de usuario y la validacion final siempre dependen de uno. Es una herramienta, no un reemplazo del proceso de desarrollo.

---

## 5. Que errores aparecieron y como los soluciono?

- **Proyecto sin dependencias de Compose:** El proyecto base venia solo con AndroidX y Material. Tuve que agregar manualmente todas las dependencias de Compose (BOM, UI, Material3, Animation), el plugin `kotlin.compose` y habilitar `buildFeatures { compose = true }` en el `build.gradle.kts`.

- **Manifest sin Activity:** El `AndroidManifest.xml` no tenia ninguna Activity declarada. Agregue la declaracion de `MainActivity` con el intent-filter `MAIN/LAUNCHER` para que la app pudiera iniciar.

- **Interaccion durante la animacion:** Los botones recibian toques mientras el sistema mostraba la secuencia, lo que generaba comportamientos inesperados. Lo solucione con una doble validacion: el boton se deshabilita a nivel de UI cuando no es turno del jugador, y el ViewModel tambien ignora los taps si la fase no es `PLAYER_INPUT`.

- **Coroutines sin cancelar:** Cuando navegaba fuera de la pantalla de juego mientras se reproducia la secuencia, la coroutine seguia corriendo en segundo plano. Lo resolvi guardando el `Job` de la secuencia y cancelandolo tanto en `onCleared()` como al reiniciar el juego.

- **Limite de rondas:** Al llegar a la ronda 10, el juego intentaba agregar un color numero 11 a la secuencia. Agregue la validacion `if (sequence.size >= MAX_SEQUENCE_LENGTH)` para que al completar la ronda 10 el juego terminara con puntaje perfecto.
