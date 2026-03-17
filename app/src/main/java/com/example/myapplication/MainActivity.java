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
import android.widget.Toast;

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
    private TextView tvTimerDisplay;

    private TextView tvSessionState;

    private TextView tvSessionsCompleted;
    private MaterialButton btnStartStop;

    private MaterialButton btnReset;

    private MaterialButton btnSkip;
    private LinearLayout sessionDotsContainer;
    // TODO: declarar el texto para la frase motivadora.
    // Elementos para el funcionamiento del temporizador.
    private CountDownTimer countDownTimer;
    private TimerState timerState = TimerState.IDLE;
    private SessionMode currentMode = SessionMode.FOCUS;
    private long timeLeftMillis = FOCUS_DURATION_MS;
    private int focusSessionsCompleted = 0;
    private int totalSessionsCompleted = 0;

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
        tvSessionState = findViewById(R.id.tvSessionState);
        tvSessionsCompleted = findViewById(R.id.tvSessionsCompleted);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnReset = findViewById(R.id.btnReset);
        btnSkip = findViewById(R.id.btnSkip);
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

        btnReset.setOnClickListener(v -> resetTimer());
        btnSkip.setOnClickListener(v -> skipToNextSession());
    }

    /**
     * Inicia o reanuda el temporizador desde el tiempo restante actual.
     * Actualiza el estado a RUNNING y cambia el texto del botón.
     * Al llamarse cada segundo, actualiza el display. Al terminar,
     * delega en onSessionFinished().
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
     * Lógica de transición entre sesiones al finalizar el tiempo.
     * En sesiones de FOCUS: agrega un punto al contenedor. Cuando se
     * alcanzan 4 sesiones de FOCUS se limpia el contenedor y pasa a REST.
     * En descanso (BREAK o REST): vuelve a FOCUS.
     */
    private void onSessionFinished() {
        timerState = TimerState.IDLE;

        if (currentMode == SessionMode.FOCUS) {
            focusSessionsCompleted++;
            totalSessionsCompleted++;
            addDot(); // Registra visualmente la sesión completada
            if (focusSessionsCompleted >= SESSIONS_BEFORE_REST) {
                focusSessionsCompleted = 0;
                sessionDotsContainer.removeAllViews(); // Reinicia el indicador visual del ciclo
                currentMode = SessionMode.REST;
            } else {
                currentMode = SessionMode.BREAK;
            }
        } else {
            currentMode = SessionMode.FOCUS;
        }

        // Mostramos un mensaje sencillo al finalizar cada sesion.
        Toast.makeText(this, "¡Sesión terminada!", Toast.LENGTH_SHORT).show();

        // solicitamos al servicio del sistema que genere una vibracion simple
        // para notificar al usuario que la sesion a terminado.
        // PERMISOS NECESARIOS EN EL MANIFIESTO:
        // <uses-permission android:name="android.permission.VIBRATE" />
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            // Vibra por 500 milisegundos
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        tvSessionsCompleted.setText("Sesiones completadas: " + totalSessionsCompleted);
        resetModeTime();
        btnStartStop.setText("Comenzar");
    }

    /**
     * Crea un punto visual (View circular con drawable) y lo agrega
     * dinámicamente al LinearLayout sessionDotsContainer.
     * El tamaño es 10dp y el margen derecho entre puntos es 8dp.
     */
    private void addDot() {
        // Creamos la vista del punto.
        View dot = new View(this);
        // Definimos su tamano (10dp convertido a pixeles).
        int dotSize = (int) (10 * getResources().getDisplayMetrics().density);
        // Creamos un contenedor para el punto.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
        // Agregamos un margen de separacion a la derecha (8dp).
        params.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
        // Aplicamos el layout a la vista.
        dot.setLayoutParams(params);
        // Asignamos la figura de nuestro punto (drawable).
        dot.setBackground(getDrawable(R.drawable.dot_session_completed));
        // Agregamos el punto creado al contenedor.
        sessionDotsContainer.addView(dot);
    }

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
     * permitir que el GC   lo limpie y evitar instancias múltiples.
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
     * Detiene el temporizador y reinicia el tiempo al valor inicial
     * del modo actual, sin avanzar al siguiente estado.
     */
    private void resetTimer() {
        cancelTimer();
        timerState = TimerState.IDLE;
        btnStartStop.setText("Comenzar");
        resetModeTime();
    }

    /**
     * Cancela el tiempo restante y fuerza el fin de la sesión actual,
     * avanzando inmediatamente al siguiente modo sin esperar que el
     * contador llegue a cero.
     */
    private void skipToNextSession() {
        cancelTimer();
        onSessionFinished();
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

        switch (currentMode) {
            case FOCUS:
                tvSessionState.setText("Modo Enfoque"); break;
            case BREAK:
                tvSessionState.setText("Modo Descanso");
                break;
            case REST:
                tvSessionState.setText("Descanso Largo");
                break;
                default: tvSessionState.setText("Desconocido");
        }
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