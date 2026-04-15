package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Gestiona el ciclo de vida de las tareas sugeridas dentro de la aplicación.
 * Implementa las operaciones básicas de persistencia en memoria (CRUD).
 * @author <a href="mailto:monmm@ciencias.unam.mx" > Mónica Miranda Mijangos </a> - @monmm
 * @version 1.0, feb 2026
 */
public class SessionManager {
    private List<Session> sessionHistory;
    private static final Locale APP_LOCALE = new Locale("es", "MX");
    private static final String DATE_PATTERN = "dd MMM yyyy";

    public SessionManager() {
        this.sessionHistory = new ArrayList<>();
    }

    /**
     * Agrega una sesión al historial.
     * Se inserta al inicio para que lo más reciente aparezca primero.
     * @param session Sesión a registrar.
     */
    public void addSession(Session session) {
        if (session != null) {
            sessionHistory.add(0, session); // Insertamos al inicio para ver lo más reciente
        }
    }

    /**
     * Devuelve una copia del historial completo de sesiones.
     * @return Lista nueva con todas las sesiones registradas.
     */
    public List<Session> getHistory() {
        return new ArrayList<>(sessionHistory);
    }

    /**
     * Obtiene una sesión por índice.
     * @param index Posición dentro del historial.
     * @return La sesión encontrada, o null si el índice es inválido.
     */
    public Session getSessionAt(int index) {
        if (index < 0 || index >= sessionHistory.size()) {
            return null;
        }
        return sessionHistory.get(index);
    }

    /**
     * Reemplaza una sesión existente en una posición específica.
     * @param index Índice de la sesión a reemplazar.
     * @param updatedSession Nueva sesión.
     * @return true si se realizó el cambio; false si el índice es inválido
     *         o la nueva sesión es null.
     */
    public boolean updateSession(int index, Session updatedSession) {
        if (updatedSession == null || index < 0 || index >= sessionHistory.size()) {
            return false;
        }
        sessionHistory.set(index, updatedSession);
        return true;
    }

    /**
     * Elimina una sesión por índice.
     * @param index Índice de la sesión a eliminar.
     * @return true si la sesión fue eliminada; false si el índice es inválido.
     */
    public boolean removeSessionAt(int index) {
        if (index < 0 || index >= sessionHistory.size()) {
            return false;
        }
        sessionHistory.remove(index);
        return true;
    }

    /**
     * Elimina una sesión específica del historial.
     * @param session Sesión a eliminar.
     * @return true si la sesión fue encontrada y eliminada.
     */
    public boolean removeSession(Session session) {
        return session != null && sessionHistory.remove(session);
    }

    /**
     * Borra por completo el historial de sesiones.
     */
    public void clearHistory() {
        sessionHistory.clear();
    }

    /**
     * Obtiene las sesiones registradas en la fecha actual.
     * El filtro depende de que Session.getDate() use el formato "dd MMM yyyy".
     *
     * @return Lista con las sesiones del día de hoy.
     */
    public List<Session> getTodaySessions() {
        List<Session> result = new ArrayList<>();
        Date today = new Date();

        for (Session session : sessionHistory) {
            Date sessionDate = parseSessionDate(session != null ? session.getDate() : null);
            if (sessionDate != null && isSameDay(sessionDate, today)) {
                result.add(session);
            }
        }
        return result;
    }

    /**
     * Obtiene las sesiones registradas en la semana actual.
     * La semana se calcula de lunes a domingo.
     * El filtro depende de que Session.getDate() use el formato "dd MMM yyyy".
     *
     * @return Lista con las sesiones de la semana actual.
     */
    public List<Session> getThisWeekSessions() {
        List<Session> result = new ArrayList<>();

        Calendar now = Calendar.getInstance(APP_LOCALE);
        Calendar startOfWeek = (Calendar) now.clone();
        startOfWeek.setFirstDayOfWeek(Calendar.MONDAY);
        startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        resetTime(startOfWeek);

        Calendar endOfWeek = (Calendar) startOfWeek.clone();
        endOfWeek.add(Calendar.DAY_OF_YEAR, 6);
        setEndOfDay(endOfWeek);

        for (Session session : sessionHistory) {
            Date sessionDate = parseSessionDate(session != null ? session.getDate() : null);
            if (sessionDate == null) {
                continue;
            }

            Calendar sessionCalendar = Calendar.getInstance(APP_LOCALE);
            sessionCalendar.setTime(sessionDate);

            if (!sessionCalendar.before(startOfWeek) && !sessionCalendar.after(endOfWeek)) {
                result.add(session);
            }
        }

        return result;
    }

    /**
     * Convierte el texto de fecha guardado en Session a un objeto Date.
     *
     * @param dateText Fecha en formato "dd MMM yyyy".
     * @return Fecha parseada o null si el texto no es válido.
     */
    private Date parseSessionDate(String dateText) {
        if (dateText == null || dateText.trim().isEmpty()) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, APP_LOCALE);
        sdf.setLenient(false);

        try {
            return sdf.parse(dateText.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Compara si dos fechas corresponden al mismo día calendario.
     *
     * @param first Primera fecha.
     * @param second Segunda fecha.
     * @return true si son el mismo día.
     */
    private boolean isSameDay(Date first, Date second) {
        Calendar c1 = Calendar.getInstance(APP_LOCALE);
        Calendar c2 = Calendar.getInstance(APP_LOCALE);

        c1.setTime(first);
        c2.setTime(second);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Limpia la hora de un Calendar para dejarlo en 00:00:00.000.
     *
     * @param calendar Calendar a ajustar.
     */
    private void resetTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Ajusta un Calendar al final del día 23:59:59.999.
     *
     * @param calendar Calendar a ajustar.
     */
    private void setEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

 }

