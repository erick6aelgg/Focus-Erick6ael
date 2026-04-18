package com.example.myapplication.data;


import static com.example.myapplication.data.SessionContract.SessionEntry.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Helper para crear y administrar la base local de sesiones.
 * SQLiteOpenHelper se encarga de abrir/crear la base y llamar a onCreate/onUpgrade.
 */
public class SessionDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "focusBud.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Crea el helper de SQLite.
     *
     * @param context Contexto de la aplicación.
     */
    public SessionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Crea la tabla de sesiones cuando la base de datos se genera por primera vez.
     *
     * @param db Base de datos SQLite.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TYPE + " TEXT NOT NULL, "
                + COLUMN_DATE + " TEXT NOT NULL, "
                + COLUMN_START_TIME + " TEXT NOT NULL, "
                + COLUMN_DURATION + " INTEGER NOT NULL, "
                + COLUMN_COMPLETED + " INTEGER NOT NULL, "
                + COLUMN_CREATED_AT + " INTEGER NOT NULL"
                + ");";

        db.execSQL(createTableSql);
    }

    /**
     * Maneja la actualización de la estructura de la base de datos.
     * Para esta práctica se rehace la tabla.
     * @param db Base de datos SQLite.
     * @param oldVersion Versión anterior.
     * @param newVersion Nueva versión.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 1. Eliminamos la tabla si ya existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // 2. Volvemos a crearla llamando al metodo onCreate
        onCreate(db);

        Log.d("SQLite", "Base de datos actualizada de la versión " + oldVersion + " a la " + newVersion);
    }

    /**
     * Maneja un posible downgrade de versión.
     * @param db Base de datos SQLite.
     * @param oldVersion Versión anterior.
     * @param newVersion Nueva versión.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
