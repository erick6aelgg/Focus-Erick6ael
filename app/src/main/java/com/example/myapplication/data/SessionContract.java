package com.example.myapplication.data;

import android.provider.BaseColumns;

/**
 * Se utiliza para definir la tabala y campos
 * en la base de datos para el modelo de Session.
 */
public class SessionContract {

    // No se hace una instancia de esta clase, solo será usada para definición de constantes.
    private SessionContract(){}
    public static final class SessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "sessions";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_CREATED_AT = "created_at_millis";
    }
}
