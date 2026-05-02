package com.example.freelancebudget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.freelancebudget.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Datos del intent
        val projectName = intent.getStringExtra("PROJECT_NAME") ?: "No name"
        val multiplier  = intent.getDoubleExtra("MULTIPLIER", 1.0)
        val total       = intent.getDoubleExtra("TOTAL", 0.0)

        // Uso correcto de strings con formato
        binding.tvProjectNameResult.text =
            getString(R.string.label_project_result, projectName)

        binding.tvMultiplierResult.text =
            getString(R.string.label_multiplier_result, multiplier)

        binding.tvTotalResult.text =
            getString(R.string.label_total_result, total)

        // Botón volver
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
