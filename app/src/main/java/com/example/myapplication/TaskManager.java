package com.example.myapplication;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase TaskManager
 * Permite manejar Tasks
 *
 * @author <a href=erick6aelgg@ciencias.unam.mx> Erick Gael García Gutiérrez - @erick6aelgg </>
 */
public class TaskManager {
    private List<Task> Tasks;

    // Método constructor
    public TaskManager(){
        Tasks = new ArrayList<Task>();
    }

    /**
     * Agrega una nueva tarea a la lista.
     * @param title       Título de la tarea
     * @param description Descripción de la tarea
     */
    public void addTask(String title, String description){
        Task newTarea = new Task(Tasks.size(), title, description, false); // Por omisión, no completada
        Tasks.add(newTarea);
    }

    /**
     * Agrega una tarea ya construida.
     * @param task Objeto Task a agregar.
     */
    public void addTask(Task task){
        if (task == null)
            return;

        Tasks.add(task);
    }

    /**
     * Devuelve una representación en String de todas las tareas.
     * @return Cadena con el listado de tareas, o un mensaje si no hay tareas.
     */
    public String viewTasks(){
        if (Tasks.isEmpty())
            return "No hay tareas registradas.";

        StringBuilder sb = new StringBuilder();
        for (Task t : Tasks)
            sb.append(("* " + t.toString() + "\n"));

        return sb.toString();
    }

    /**
     * Actualiza los datos de una tarea existente.
     * @param taskId Identificador de la tarea a modificar
     * @param title Nuevo título (si es null no se modifica)
     * @param description Nueva descripción (si es null no se modifica)
     * @param completed Nuevo estado de completado (si es null no se modifica)
     */
    public void updateTask(int taskId, String title, String description, Boolean completed) {
        for (Task t : Tasks) {
            if (t.getTaskId() == taskId) {
                if (title != null) t.setTitle(title);
                if (description != null) t.setDescription(description);
                if (completed != null) t.setCompleted(completed);
                return;
            }
        }
        System.out.println("Tarea con ID " + taskId + " no encontrada.");
    }

    /**
     * Elimina una tarea por su ID.
     * @param taskId Identificador de la tarea a eliminar
     */
    public void deleteTask(int taskId) {
        Task tareaAEliminar = null;
        for (Task t : Tasks) {
            if (t.getTaskId() == taskId) {
                tareaAEliminar = t;
                break;
            }
        }
        if (tareaAEliminar != null) {
            Tasks.remove(tareaAEliminar);
        } else {
            System.out.println("Tarea no encontrada.");
        }
    }

}
