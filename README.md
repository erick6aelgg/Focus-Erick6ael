# Programación de dispositivos móviles 2026-2
### García Gutiérrez Erick Gael - 321148726
---

# Práctica 3 - Aplicación FocusBuddy

Se realizó una aplicación Android de basada en la técnica Pomodoro. Ayuda a gestionar
sesiones de concentración de 25 min, descansos cortos de 5 min y largos de 15 min de forma automática, con indicadores visuales, vibración y frases motivacionales.

Además, se guarda un registro de las sesiones completadas (de los 3 tipos) donde es posible filtrar entre el día, semana o simplemente mostrar todas.

Es posible cambiar el idioma entre español e inglés así como mostrar un modo claro y un modo oscuro.

---

## Tareas destacadas

### Reestructuración del proyecto
El proyecto ahora no solo consta de `MainActivity`, para comenzar este fue agregado al paquete `controller`, se agregaron más clases en paquetes nuevos como lo son `model`, `view` y `data`.

### Implementación de nuevas pantallas
Se ingresaron nuevas pantallas para mostrar historial y preferencias configurables por el usuario, siendo en el historial donde se muestran más elementos como botones o cartas con datos de las sesiones.

### Implementación de temas y preferencias de idioma
Es posible seleccionar entre temas claro y oscuro así como cambiar el idioma de los textos de la aplicación, esta última parte fue la más compleja ya que al principio al cambiar la configuración solo cambiaba el idioma de una pantalla, pero otra no cambiaba y la última solo cambiaba al reiniciar la aplicación.

### Integración de una base de datos
Se implementó una conexión a una base de datos local con el propósito de llevar el registro de las sesiones, se implementaron varias clases para esto, incluyendo una que funciona exlcusivamente para definir la estructura de la tabla en la base de datos.

---

## Mejoras para una segunda versión

Aprovechando que ya podemos tratar con temas claros y oscuros, estaría bien implementar una opción de accesibilidad para personas con daltonismo (incluyendo los distintos tipos).
