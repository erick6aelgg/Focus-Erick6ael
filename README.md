# Programación de dispositivos móviles 2026-2
### García Gutiérrez Erick Gael - 321148726
## Práctica 1
### Descripción
La práctica se divide en tres fases:
#### Parte 1: Emuladores AVD
Se deben crear dos emuladores (AVD)
-   Smartphone con API 34 (Android 14), 1080x1920 px, con Play Store, 2GB RAM: 
    Para este, me base en el hardware del Pixel 2 ya que cumple con las propiedades solicitadas y permite el uso de la Playstore (Si se crea desde cero, esta opción es inexistente). 
-   Tablet con API 26 (Android 8), densidad xhdpi, 2GB RAM:
    Para la tablet se puede diseñar una desde cero, la densidad depende de las dimensiones y   el tamaño de la pantalla.

#### Parte 2: Git (Manejo de conflictos)
Con ayuda de un colaborador, se debe generar un conflicto y resolverlo, para ello, seguí los siguientes pasos:
1. Se crea una rama `feature/[usuario]`. 

2.  El dueño del repositorio modifica el archivo MainActivity.java de manera local.

3. El colaborador modifica ese archivo en el remoto (desde la página de git o haciendo `commit` y `push`).

4.  El dueño del repositorio deberá de usar `git pull feature[usuario] --no-rebase` donde habrá un conflicto.

5. Android Studio detecta el conflicto y se puede resolver desde ahí.

6. Tras resolverlo, se hace el `commit` agregando los cambios en la rama (y el respectivo `push`).

7. Finalmente, se hace el `merge` a `main` y tras hacer `push` en esa rama se ejecuta el `tag` con `git tag -a v1.0.0 -m "[mensaje]"` y `git push origin v1.0.0`.

#### Parte 3: TaskManager.java
Se debe crear una clase `TaskManager` en java con la cual manejaremos un listado de `Task` (una clase que yo implemente, también estaba la opción de usar solo Strings) utilizando las operaciones Create (agregar tareas) , Read (ver las existentes), Update (modificar basado en un id o la tarea misma) y Delete (eliminar dado un id).

Utilicé un ArrayList ya que a comparación de una lista, se puede acceder a un indice de la lista más rapido. 

