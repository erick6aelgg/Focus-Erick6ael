# Práctica 2 - Pomodoro Timer

Se realizó una aplicación Android de basada en la técnica Pomodoro. Ayuda a gestionar
sesiones de concentración de 25 min, descansos cortos de 5 min y largos de 15 min de forma automática, con indicadores visuales, vibración y frases motivacionales.

---

## Preguntas
### 1. ¿Cuál fue el mayor reto al gestionar el CountDownTimer y cómo evitaste que se crearan múltiples instancias al presionar el botón repetidamente?

El mayor reto fue que cada vez que el usuario presionaba "Comenzar", se creaba un nuevo
`CountDownTimer` sin cancelar el anterior, provocando que varios contadores corrieran
en paralelo y el tiempo se comportara de forma inesperada.

Esto se resuelve con el método `cancelTimer()`, que cancela y anula la instancia actual
antes de crear una nueva.



### 2. ¿Por qué es preferible usar un LinearLayout con addView para los puntos de progreso en lugar de declarar 4 ImageViews estáticos en el XML?

Si en el futuro se quisiera cambiar el número de sesiones antes del descanso
largo, habría que modificar tanto el XML como el código Java ya que serian estáticos.



### 3. Si quisiéramos añadir una función para que el usuario personalice sus propios tiempos de enfoque, ¿qué parte de tu lógica actual tendría que cambiar y cómo lo abordarías?

Se definieron como constantes  `FOCUS_DURATION_MS`, `BREAK_DURATION_MS` y `REST_DURATION_MS`. Para permitir personalización habría que:

1. Reemplazarlas por variables que se lean desde `SharedPreferences`.
2. Agregar una pantalla de configuración donde el usuario pueda ingresar sus tiempos.
3. Guardar esos valores con `SharedPreferences` para que persistan entre sesiones.
4. Llamar a `resetModeTime()` después de guardar para que el cambio se refleje
   inmediatamente en el temporizador.

### 4. ¿Cómo harían para que el tiempo del temporizador se mantenga si el usuario minimiza la app?

Implementamos `onSaveInstanceState()` para guardar el estado del temporizador
(tiempo restante, modo actual, estado y contadores) en el `Bundle` del sistema antes
de que la Activity sea destruida. En `onCreate()` se detecta si existe ese `Bundle` y
se restauran los valores, reanudando el contador si estaba en ejecución.
