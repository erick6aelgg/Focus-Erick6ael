package com.example.myapplication.model;

import static com.example.myapplication.data.SessionContract.SessionEntry.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.myapplication.data.SessionContract.SessionEntry.*;
import com.example.myapplication.data.SessionDbHelper;

/**
 * Gestiona el ciclo de vida de las tareas sugeridas dentro de la aplicación.
 * Implementa las operaciones básicas de persistencia en memoria (CRUD).
 * @author <a href="mailto:monmm@ciencias.unam.mx" > Mónica Miranda Mijangos </a> - @monmm
 * @version 1.0, feb 2026
 */
public class SessionManager {
    private final SessionDbHelper dbHelper;
    private static final Locale APP_LOCALE = new Locale("es", "MX");
    private static final String DATE_PATTERN = "dd MMM yyyy";

    public SessionManager(Context context) {
        dbHelper = new SessionDbHelper(context);
    }

    /**
     * Agrega una sesión al historial.
     * Se inserta al inicio para que lo más reciente aparezca primero.
     * @param session Sesión a registrar.
     * @return ID insertado o -1 si falló.
     */
    public void addSession(Session session) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TYPE, session.getType());
            values.put(COLUMN_DATE, session.getDate());
            values.put(COLUMN_START_TIME, session.getStartTime());
            values.put(COLUMN_DURATION, session.getDuration());
            values.put(COLUMN_COMPLETED, session.isCompleted() ? 1 : 0);
            values.put(COLUMN_CREATED_AT, session.getCreatedAtMillis());


            long newId = db.insert(TABLE_NAME, null, values);
            if (newId != -1L) {
                session.setId(newId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * Devuelve una copia del historial completo de sesiones.
     * @return Lista nueva con todas las sesiones registradas.
     */
    public List<Session> getHistory() {
        return getAllSessions();
    }

    /**
     * Devuelve una copia del historial completo de sesiones.
     * @return Lista nueva con todas las sesiones registradas.
     */
    public List<Session> getAllSessions(){
        List<Session> sessionList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consultamos toda la tabla, ordenando por ID descendente (dejando la sesión más reciente primero)
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, _ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Session session = new Session();
                // Extraemos los datos usando el índice de la columna
                session.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
                session.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                session.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                session.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME)));
                session.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                // Convertimos el 1/0 de SQLite de vuelta a boolean
                int completedInt = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED));
                session.setCompleted(completedInt == 1);
                session.setCreatedAtMillis(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));

                sessionList.add(session);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sessionList;
    }

    /**
     * Obtiene las sesiones registradas en la fecha actual.
     * El filtro depende de que Session.getDate() use el formato "dd MMM yyyy".
     * @return Lista con las sesiones del día de hoy.
     */
    public List<Session> getTodaySessions() {
        List<Session> result = new ArrayList<>();
        Calendar today = Calendar.getInstance();

        for (Session session : getHistory()) {
            Calendar item = Calendar.getInstance();
            item.setTimeInMillis(session.getCreatedAtMillis());

            if (sameDay(today, item)) {
                result.add(session);
            }
        }
        return result;
    }

    /**
     * Comprueba si dos calendarios pertenecen al mismo día.
     * @param first Primer calendario.
     * @param second Segundo calendario.
     * @return true si son el mismo día.
     */
    private boolean sameDay(Calendar first, Calendar second) {
        return first.get(Calendar.YEAR) == second.get(Calendar.YEAR)
                && first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Obtiene las sesiones registradas en la semana actual.
     * La semana se calcula de lunes a domingo.
     * El filtro depende de que Session.getDate() use el formato "dd MMM yyyy".
     * @return Lista con las sesiones de la semana actual.
     */
    public List<Session> getThisWeekSessions() {
        List<Session> result = new ArrayList<>();

        Calendar now = Calendar.getInstance(Locale.getDefault());
        Calendar startOfWeek = (Calendar) now.clone();
        startOfWeek.setFirstDayOfWeek(Calendar.MONDAY);
        startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        zeroTime(startOfWeek);

        Calendar endOfWeek = (Calendar) startOfWeek.clone();
        endOfWeek.add(Calendar.DAY_OF_YEAR, 6);
        endOfDay(endOfWeek);

        for (Session session : getHistory()) {
            Calendar item = Calendar.getInstance();
            item.setTimeInMillis(session.getCreatedAtMillis());

            if (!item.before(startOfWeek) && !item.after(endOfWeek)) {
                result.add(session);
            }
        }
        return result;
    }

    /**
     * Lleva un Calendar al inicio del día.
     * @param calendar Calendar a ajustar.
     */
    private void zeroTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Lleva un Calendar al final del día.
     * @param calendar Calendar a ajustar.
     */
    private void endOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }


}

