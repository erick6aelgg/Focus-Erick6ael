package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.view.View;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


/**
 * @author <a href=erick6aelgg@ciencias.unam.mx> Erick Gael García Gutiérrez - @erick6aelgg </>
 * @author <a href="mailto:miguel.mavt59@ciencias.unam.mx">Miguel Angel Valente Trinidad - @MiguelAngel59</a>
 */

public class MainActivity extends AppCompatActivity {

    // Estados del temporizador y sesion.
    enum TimerState { IDLE, RUNNING, PAUSED }
    enum SessionMode { FOCUS, BREAK, REST}

    // Constantes de tiempo en milisegundos.
    private static final long FOCUS_DURATION_MS   = 25 * 60 * 1000L;
    private static final long BREAK_DURATION_MS   =  5 * 60 * 1000L;
    private static final long REST_DURATION_MS    = 15 * 60 * 1000L;
    private static final int SESSIONS_BEFORE_REST = 4;

    // Elementos de la IU.
    private TextView tvAppTittle;
    private ImageButton btnStats, btnSettings;
    private ChipGroup chipGroupMode;
    private Chip chipFocus, chipBreak, chipRest;
    // TODO: delcarar el texto que indica el estado de la sesion.
    private TextView tvTimerDisplay;
    // TODO: delcarar el texto que indica cuantas sesiones han sido completadas.
    private MaterialButton btnStartStop;
    // TODO: delcarar los botones de reinicio y salto de una sesion.
    private LinearLayout sessionDotsContainer;
    // TODO: declarar el texto para la frase motivadora.
    // Elementos para el funcionamiento del temporizador.
    private CountDownTimer countDownTimer;
    private TimerState timerState = TimerState.IDLE;
    private SessionMode currentMode = SessionMode.FOCUS;
    private long timeLeftMillis = FOCUS_DURATION_MS;
    private int focusSessionsCompleted = 0;

    /**
     * Punto de entrada de la Activity. Infla la vista, enlaza elementos
     * de UI, asigna escuchas y muestra el tiempo inicial en pantalla.
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Inflamos nuestra vista.
        setContentView(R.layout.activity_main);

        // Inicializamos los elementos de la IU.
        bindViews();
        // Asignamos los escuchas.
        setupClickListeners();
        // Actualizamos la IU.
        updateTimerDisplay(timeLeftMillis);
    }

    /**
     * Llamado al destruir la Activity.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }


    /**
     * Enlaza los elementos del layout con sus variables correspondientes.
     * TODO: inicializar el texto que indica el estado de la sesion.
     * TODO: inicializar el texto que indica cuantas sesiones han sido completadas.
     * TODO: inicializar los botones de reinicio y salto de una sesion.
     * TODO: inicializar el texto para la frase motivadora.
     */
    private void bindViews() {
        btnStats = findViewById(R.id.btnStats);
        btnSettings = findViewById(R.id.btnSettings);
        chipGroupMode = findViewById(R.id.chipGroupMode);
        chipFocus = findViewById(R.id.chipFocus);
        chipBreak = findViewById(R.id.chipBreak);
        chipRest = findViewById(R.id.chipRest);
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);
        btnStartStop = findViewById(R.id.btnStartStop);
        sessionDotsContainer = findViewById(R.id.sessionDotsContainer);
    }

    /**
     * Asigna los escuchas de click a los elementos interactivos de la UI.
     */
    private void setupClickListeners() {
        // Asignamos un escucha al boton que controla nuestro temporizador.
        btnStartStop.setOnClickListener(v -> {
            // Se ha seleccionado la opcion para comenzar/pausar el temporizador.
            // Llamamos a los metodos correspondientes segun el estado del temporizador.
            if (timerState == TimerState.RUNNING) pauseTimer();
            else startTimer();
        });

        btnStats.setOnClickListener(null);
        btnSettings.setOnClickListener(null);
    }

    /**
     * TODO
     */
    private void startTimer() {
        // Actualizamos el estado del temporizador.
        timerState = TimerState.RUNNING;
        // Asignamos una texto mas adecuado al boton que controla nuestro temporizador.
        btnStartStop.setText("Pausar");

        // PRUEBA
        // addDot();

        // Creamos e inicializamos un contador.
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            /**
             * Se ejecuta cada segundo. Guarda el tiempo restante y
             * actualiza el display en pantalla.
             *
             * @param millisUntilFinished Tiempo restante en milisegundos.
             */
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimerDisplay(millisUntilFinished);
            }

            /**
             * Se ejecuta al llegar a cero. Notifica que la sesión terminó.
             */
            @Override
            public void onFinish() {
                onSessionFinished();
            }
        }.start();
    }

    /**
     * Pausa el temporizador cancelando el CountDownTimer activo
     * (el tiempo restante ya fue guardado en timeLeftMillis en el último tick).
     * Actualiza el estado a PAUSED y el texto del botón.
     */
    private void pauseTimer() {
        // Detenemos nuestro contador.
        if (countDownTimer != null) countDownTimer.cancel();
        // Actualizamos el estado de nuestro temporizador.
        timerState = TimerState.PAUSED;
        // Actualizamos el texto del boton que controla el temporizador.
        btnStartStop.setText("Reanudar");
    }

    /**
     * TODO: Documentar.
     * TODO: reiniciar el contenedor de puntos o agregar un nuevo punto en el layout.
     * TODO: actualizar el texto que indica el numero de sesiones de enfoque completadas.
     */
    private void onSessionFinished() {
        // Actualizamos el estado de nuestro temporizador.
        timerState = TimerState.IDLE;

        // Actualizamos el estado de la sesion por su sucesora.
        if (currentMode == SessionMode.FOCUS) {
            focusSessionsCompleted++;
            if (focusSessionsCompleted >= SESSIONS_BEFORE_REST) {
                focusSessionsCompleted = 0;
                currentMode = SessionMode.REST;
            } else {
                currentMode = SessionMode.BREAK;
            }
        } else {
            currentMode = SessionMode.FOCUS;
        }

        resetModeTime();
        btnStartStop.setText("Comenzar");
    }

    /**
     * TODO
     */
    private void addDot() { }

    /**
     * Asigna a timeLeftMillis la duración correspondiente al modo actual
     * y actualiza el display del temporizador.
     */
    private void resetModeTime() {
        // Reasignamos la duracion de la sesion segun el estado actual.
        if (currentMode == SessionMode.FOCUS) timeLeftMillis = FOCUS_DURATION_MS;
        else if (currentMode == SessionMode.BREAK) timeLeftMillis = BREAK_DURATION_MS;
        else timeLeftMillis = REST_DURATION_MS;
        // Actualizamos la IU.
        updateTimerDisplay(timeLeftMillis);
    }

    /**
     * Cancela el CountDownTimer si está activo y lo anula para
     * permitir que el GC lo limpie y evitar instancias múltiples.
     */
    private void cancelTimer() {
        // Si el temporizador esta activo:
        if (countDownTimer != null) {
            // Detemos el tiempo.
            countDownTimer.cancel();
            // Anulamos el temporizador.
            countDownTimer = null;
        }
    }

    /**
     * Actualiza el TextView del temporizador con el tiempo restante formateado
     * como MM:SS y resalta el chip correspondiente al modo actual.
     *
     * @param millis Tiempo restante en milisegundos.
     */
    private void updateTimerDisplay(long millis) {
        // Resaltamos el chip correspondiente al estado actual del temporizador.
        selectChipForMode(currentMode);
        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;
        // Actualizamos el texto del temporizador.
        tvTimerDisplay.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * Selecciona el chip del ChipGroup que corresponde al modo de sesión
     * activo y le aplica el resaltado visual.
     *
     * @param mode Modo de sesión actual (FOCUS, BREAK, REST).
     */
    private void selectChipForMode(SessionMode mode) {
        // El identificador del chip a seleccionar.
        int chipId;
        switch (mode) {
            case BREAK:
                // Asignamos el elemento en el layout (el chip).
                chipId = R.id.chipBreak;
                // Resaltamos el chip seleccionado.
                highlightChip(chipBreak);
                break;
            case REST:
                chipId = R.id.chipRest;
                highlightChip(chipRest);
                break;
            default:
                chipId = R.id.chipFocus;
                highlightChip(chipFocus);
                break;
        }
        chipGroupMode.check(chipId);
        // La agrupacion sabe que chip hemos seleccionado.
    }

    /**
     * Aplica un borde de 2dp con el color de acento al chip activo
     * y elimina el borde del resto de chips.
     *
     * @param activeChip Chip que debe resaltarse.
     */
    private void highlightChip(Chip activeChip) {
        // Obtenemos la densidad de pantalla necesaria para construir el borde de nuestros chips.
        float density = getResources().getDisplayMetrics().density;
        // Enlistamos los chips disponibles para manipularlos facilmente.
        Chip[] allChips = {chipFocus, chipBreak, chipRest};

        // Quitamos el borde de todos los chips.
        for (Chip chip : allChips) {
            chip.setChipStrokeWidth(0);
        }

        // Resaltamos el chip activo modificando el grosor del borde.
        activeChip.setChipStrokeWidth(2 * density);
        // Recuperamos el color para resaltar el borde del chip de los recursos de nuestra app.
        int colorAccent = ContextCompat.getColor(this, R.color.color_border_accent);
        // Asignamos el color del borde para resaltar al chip activo.
        activeChip.setChipStrokeColor(ColorStateList.valueOf(colorAccent));
    }
}