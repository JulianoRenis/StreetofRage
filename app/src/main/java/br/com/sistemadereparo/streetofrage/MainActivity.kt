package br.com.sistemadereparo.streetofrage

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
    private var deltaX: Int = 0

    private var moveHandler: Handler? = null

    private val moveRunnable = object : Runnable {
        override fun run() {
            // Move o personagem continuamente
            moveCharacter(deltaX)
            moveHandler?.postDelayed(this, 75) // Repete a cada 100 milissegundos
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        imageView = binding.imgAdam
        startAnimation()

        binding.btnLeft.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> moveCharacterContinuously(-5) // Quando o botão é pressionado
                MotionEvent.ACTION_UP -> stopMovingCharacter() // Quando o botão é solto
            }
            true
        }

        binding.btnRight.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> moveCharacterContinuously(5) // Quando o botão é pressionado
                MotionEvent.ACTION_UP -> stopMovingCharacter() // Quando o botão é solto
            }
            true
        }
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

    private fun moveCharacterContinuously(deltaX: Int) {
        // Inicia o movimento contínuo se ainda não estiver em andamento
        if (moveHandler == null) {
            moveHandler = Handler()
            this.deltaX = deltaX // Define a direção do movimento
            moveHandler?.postDelayed(moveRunnable, 0)
        }
    }

    private fun stopMovingCharacter() {
        // Para o movimento contínuo se estiver em andamento
        moveHandler?.removeCallbacks(moveRunnable)
        moveHandler = null
    }

    private fun moveCharacter(deltaX: Int) {
        val layoutParams = imageView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.leftMargin += deltaX // Altera a margem esquerda do ImageView
        imageView.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }
}