package com.example.freelancebudget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.freelancebudget.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    // View Binding: nos permite acceder a los componentes del XML sin usar findViewById()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Inflamos el layout usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchTheme.isChecked = isDarkMode

        // Seleccionamos "Low" por defecto para que siempre haya una opción marcada
        binding.rbLow.isChecked = true


        // Le asignamos una acción al botón "Calculate"
        binding.btnCalculate.setOnClickListener {
            handleCalculate()
        }

        val editor = prefs.edit()

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("dark_mode", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("dark_mode", false)
            }

            editor.apply()
        }

    }

    // Función que maneja toda la lógica cuando el usuario toca "Calculate"
    private fun handleCalculate() {

        // Leemos el texto ingresado en el campo de valor hora
        val hourlyRateText = binding.etHourlyRate.text.toString()

        // VALIDACIÓN: si el campo está vacío, mostramos error y cortamos
        if (hourlyRateText.isEmpty()) {
            binding.etHourlyRate.error = "Please enter an hourly rate"
            return
        }

        // Convertimos el texto a número decimal
        val hourlyRate = hourlyRateText.toDouble()

        // VALIDACIÓN: si el valor es 0 o negativo, mostramos error y cortamos
        if (hourlyRate <= 0) {
            binding.etHourlyRate.error = "Hourly rate must be greater than 0"
            return
        }

        // Determinamos el multiplicador según el RadioButton seleccionado
        val multiplier = when (binding.rgComplexity.checkedRadioButtonId) {
            R.id.rbLow    -> 1.0
            R.id.rbMedium -> 1.5
            R.id.rbHigh   -> 2.0
            else          -> 1.0  // valor por defecto (no debería llegar acá)
        }

        // Leemos el nombre del proyecto
        val projectName = binding.etProjectName.text.toString()

        // Verificamos si el CheckBox de urgente está marcado
        val isUrgent = binding.cbUrgent.isChecked

        // Calculamos el recargo por urgencia (30% extra si está marcado)
        val urgencyMultiplier = if (isUrgent) 1.3 else 1.0

        // Cálculo final: valor hora × complejidad × urgencia
        val total = hourlyRate * multiplier * urgencyMultiplier

        // Creamos el Intent para ir a la siguiente pantalla (ResultActivity)
        val intent = Intent(this, ResultActivity::class.java)

        // Empaquetamos los datos para enviarlos a ResultActivity
        intent.putExtra("PROJECT_NAME", projectName)
        intent.putExtra("MULTIPLIER", multiplier)
        intent.putExtra("TOTAL", total)

        // Lanzamos la siguiente Activity
        startActivity(intent)
    }
}