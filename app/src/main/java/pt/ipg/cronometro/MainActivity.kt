package pt.ipg.cronometro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.inputmethod.InputBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.ipg.cronometro.databinding.ActivityMainBinding
import kotlin.math.roundToInt
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.StartButton.setOnClickListener { startStopTimer() }
        binding.RestartButton.setOnClickListener { restartTimer() }

        serviceIntent= Intent(applicationContext, TimerServices::class.java)
        registerReceiver(updateTime, IntentFilter(TimerServices.TIMER_UPDATED))
    }

    private fun restartTimer() {
        stopTimer()
        time = 0.0
        binding.cronometroTimer.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerServices.TIMER_EXTRA, time)
        startService(serviceIntent)
        binding.StartButton.text = "Stop"
        val color1 = ColorStateList.valueOf(Color.parseColor("#B60F0F"))
        binding.StartButton.backgroundTintList = color1
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.StartButton.text = "Start"
        val color2 = ColorStateList.valueOf(Color.parseColor("#33AF38"))
        binding.StartButton.backgroundTintList = color2
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver(){

        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerServices.TIMER_EXTRA, 0.0)
            binding.cronometroTimer.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String{
        val resultInt = time.roundToInt()
        val hora = resultInt % 86400 / 3600
        val min = resultInt % 86400 % 3600 / 60
        val sec = resultInt % 86400 % 3600 % 60

        return makeTimeString(hora, min, sec)
    }

    private fun makeTimeString(hora: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hora, min, sec)
}
