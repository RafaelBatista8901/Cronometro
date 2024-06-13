package pt.ipg.cronometro

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var textViewTimer: TextView
    private lateinit var buttonStart: Button
    private lateinit var buttonSetTime: Button
    private lateinit var buttonCronometro: Button

    private var timeInMillis: Long = 0
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        
        val buttonTimer: Button = findViewById(R.id.buttonTimer)
        buttonTimer.isEnabled = false

        textViewTimer = findViewById(R.id.textViewTimer)
        buttonStart = findViewById(R.id.buttonStart)
        buttonSetTime = findViewById(R.id.buttonSetTime)
        buttonCronometro = findViewById(R.id.buttonCronometro)

        buttonSetTime.setOnClickListener {
            showSetTimeDialog()
        }

        buttonStart.setOnClickListener {
            startTimer(timeInMillis)
        }

        buttonCronometro.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSetTimeDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            showSecondsPickerDialog(hourOfDay, minute)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun showSecondsPickerDialog(hours: Int, minutes: Int) {
        val numberPicker = NumberPicker(this)
        numberPicker.maxValue = 59
        numberPicker.minValue = 0

        AlertDialog.Builder(this)
            .setTitle("Escolha os segundos")
            .setView(numberPicker)
            .setPositiveButton("OK") { _, _ ->
                val seconds = numberPicker.value
                timeInMillis = ((hours * 3600) + (minutes * 60) + seconds) * 1000L
                textViewTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun startTimer(timeInMillis: Long) {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000) / 3600
                val minutes = ((millisUntilFinished / 1000) % 3600) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                textViewTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                showTimeUpDialog()
            }
        }.start()
    }

    private fun showTimeUpDialog() {
        AlertDialog.Builder(this)
            .setTitle("Tempo Acabou")
            .setMessage("O tempo acabou!")
            .setPositiveButton("Fechar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}