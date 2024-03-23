package br.com.sistemadereparo.streetofrage

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
    private  var andando = false

    private var moveHandler: Handler? = null

    private val moveRunnable = object : Runnable {
        override fun run() {
            // Move o personagem continuamente
            movePersonagem(deltaX)
            moveHandler?.postDelayed(this, 75) // Repete a cada 100 milissegundos
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        imageView = binding.imgAdam

       verificarSePersonagemEstaParado()
        binding.btnLeft.setOnTouchListener { view, motionEvent ->
            handler.removeCallbacksAndMessages(null)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> movePersonagemContinuamente(-8) // Quando o botão é pressionado
                MotionEvent.ACTION_UP -> paraMovimentoDoPersonagem() // Quando o botão é solto
            }
            andando
        }

        binding.btnRight.setOnTouchListener { view, motionEvent ->
            handler.removeCallbacksAndMessages(null)

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> movePersonagemContinuamente(8) // Quando o botão é pressionado
                MotionEvent.ACTION_UP -> paraMovimentoDoPersonagem() // Quando o botão é solto
            }
            andando
        }
    }

    private fun verificarSePersonagemEstaParado() {
        if (andando==false){
            iniciaAnimacaoPersonagemGingando()
        }    }


    private fun iniciaAnimacaoPersonagemGingando() {
        imageView.setImageResource(images[currentIndex])

        // Incrementa o índice para a próxima imagem
        currentIndex = (currentIndex + 1) % images.size

        // Aguarda 500 milissegundos antes de exibir a próxima imagem
        handler.postDelayed({
            iniciaAnimacaoPersonagemGingando()
        }, 250)
    }

    private fun movePersonagemContinuamente(deltaX: Int) {
        if (moveHandler == null) {
            imageView.setImageResource(R.drawable.anim_andando) // Inicia a nova animação
            val animationDrawable = imageView.drawable as AnimationDrawable
            animationDrawable.start()
            this.deltaX = deltaX // Define a direção do movimento
            moveHandler = Handler()
            moveHandler?.postDelayed(moveRunnable, 0)
        }
    }

    private fun paraMovimentoDoPersonagem() {
        // Para o movimento contínuo se estiver em andamento
        moveHandler?.removeCallbacks(moveRunnable)
        moveHandler = null
        imageView.setImageResource(R.drawable.adam_zero) // Define a imagem parada do personagem
        // Pare a animação de caminhada, se estiver em andamento
        (imageView.drawable as? AnimationDrawable)?.stop()

        verificarSePersonagemEstaParado()
    }

    private fun movePersonagem(deltaX: Int) {
        val layoutParams = imageView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.leftMargin += deltaX // Altera a margem esquerda do ImageView
        imageView.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }
}