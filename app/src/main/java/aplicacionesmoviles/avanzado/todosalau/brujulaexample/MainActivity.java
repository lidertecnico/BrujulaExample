package aplicacionesmoviles.avanzado.todosalau.brujulaexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // Declaración de variables miembro
    private SensorManager sensorManager;
    private Sensor magnetometer;
    private TextView magneticFieldTextView;
    private Button startButton;
    private boolean isListening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de vistas y botón
        magneticFieldTextView = findViewById(R.id.magneticFieldTextView);
        startButton = findViewById(R.id.startButton);

        // Inicialización del SensorManager y el magnetómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        // Configuración del OnClickListener para el botón de inicio
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isListening) {
                    // Comienza a escuchar el sensor cuando se presiona el botón
                    sensorManager.registerListener(MainActivity.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                    startButton.setText("Stop"); // Cambia el texto del botón a "Stop"
                    isListening = true;
                } else {
                    // Detiene la escucha del sensor cuando se presiona el botón nuevamente
                    sensorManager.unregisterListener(MainActivity.this);
                    startButton.setText("Start"); // Cambia el texto del botón a "Start"
                    isListening = false;
                    onPause(); // Pausa la actividad
                    //finish(); // Cierra la actividad cuando se detiene la lectura del sensor
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Método llamado cuando cambia el valor del sensor
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float magneticFieldStrength = (float) Math.sqrt(
                    event.values[0] * event.values[0] +
                            event.values[1] * event.values[1] +
                            event.values[2] * event.values[2]);

            // Muestra el valor del campo magnético en el TextView
            magneticFieldTextView.setText("Magnetic Field: " + magneticFieldStrength + " μT");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Método llamado cuando cambia la precisión del sensor
        // No necesitas implementar nada aquí para este ejemplo
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausa la escucha del sensor cuando la actividad está en pausa
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reanuda la escucha del sensor cuando la actividad se reanuda
        if (isListening) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
