package br.com.sistemadereparo.streetofrage

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.sistemadereparo.streetofrage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var imageView: ImageView
    private val images = intArrayOf(R.drawable.adam_zero, R.drawable.adam_one, R.drawable.adam_two)
    private var currentIndex = 0
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        imageView = binding.imgAdam
        startAnimation()

    }

    private fun startAnimation() {
        imageView.setImageResource(images[currentIndex])

        // Incrementa o índice para a próxima imagem
        currentIndex = (currentIndex + 1) % images.size

        // Aguarda 500 milissegundos antes de exibir a próxima imagem
        handler.postDelayed({
            startAnimation()
        }, 250)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }
}