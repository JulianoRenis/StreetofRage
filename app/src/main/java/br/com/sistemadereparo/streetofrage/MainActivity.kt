package br.com.sistemadereparo.streetofrage

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.sistemadereparo.streetofrage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var pernsonaAdam: ImageView
    private lateinit var stageOne: ImageView
    private val images = intArrayOf(R.drawable.adam_zero, R.drawable.adam_one, R.drawable.adam_two)


    private var currentIndex = 0
    private val handler = Handler()
    private var deltaX: Int = 0
    private var andando = false

    private var leftMargin = 0 // Ajuste a margem esquerda conforme necessário

    private var moveHandler: Handler? = null

    private val moveRunnable = object : Runnable {
        override fun run() {
            // Move o personagem continuamente
            movePersonagem(deltaX)
            moveHandler?.postDelayed(this, 100) // Repete a cada 100 milissegundos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pernsonaAdam = binding.imgAdam
        stageOne = binding.imageBackgroundStageOne

        verificarSePersonagemEstaParado()




        val topMargin = 0 // Ajuste a margem superior conforme necessário


        val frame = binding.framelayoutStageOne

        val layoutParams = stageOne.layoutParams as FrameLayout.LayoutParams
        layoutParams.leftMargin = leftMargin
        layoutParams.topMargin = topMargin
        stageOne.layoutParams = layoutParams

        // Define a escala do ImageView para preencher todo o espaço sem distorcer a imagem
        stageOne.scaleType = ImageView.ScaleType.FIT_XY

// Define a escala do FrameLayout para evitar a compressão da imagem de fundo
        frame.scaleX = 6.0f
        frame.scaleY = 1.0f

        binding.btnLeft.setOnTouchListener { view, motionEvent ->
            handler.removeCallbacksAndMessages(null)


            layoutParams.leftMargin = leftMargin
            frame.layoutParams = layoutParams

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    movePersonagemContinuamente(-8) // Quando o botão é pressionado
                    leftMargin+=10
                     }
                MotionEvent.ACTION_UP -> paraMovimentoDoPersonagem() // Quando o botão é solto
            }
            andando
        }

        binding.btnRight.setOnTouchListener { view, motionEvent ->
            handler.removeCallbacksAndMessages(null)


            layoutParams.leftMargin = leftMargin
            frame.layoutParams = layoutParams

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    movePersonagemContinuamente(8)
                    leftMargin-=10
                } // Quando o botão é pressionado
                MotionEvent.ACTION_UP -> paraMovimentoDoPersonagem() // Quando o botão é solto
            }
            andando
        }

        binding.btnSoco.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            // Inicie a animação de soco
            animacaoSoco()
        }
    }





    private fun animacaoSoco() {


        binding.imgAdam.setImageResource(R.drawable.anim_soco)
        val animationDrawable = binding.imgAdam.drawable as AnimationDrawable
        animationDrawable.isOneShot = true
        animationDrawable.start()



    }

    private fun pararAnimSoco() {
        // Pare a animação de soco, se estiver em andamento
        (pernsonaAdam.drawable as? AnimationDrawable)?.stop()

        // Defina a imagem padrão do personagem após o soco
        pernsonaAdam.setImageResource(R.drawable.adam_zero)
    }

    private fun verificarSePersonagemEstaParado() {
        if (andando == false) {
            iniciaAnimacaoPersonagemGingando()
        }
    }


    private fun iniciaAnimacaoPersonagemGingando() {
        pernsonaAdam.setImageResource(images[currentIndex])

        // Incrementa o índice para a próxima imagem
        currentIndex = (currentIndex + 1) % images.size

        // Aguarda 500 milissegundos antes de exibir a próxima imagem
        handler.postDelayed({
            iniciaAnimacaoPersonagemGingando()
        }, 250)
    }


    private fun movePersonagemContinuamente(deltaX: Int) {
        if (moveHandler == null) {
            pernsonaAdam.setImageResource(R.drawable.anim_andando) // Inicia a nova animação
            val animationDrawable = pernsonaAdam.drawable as AnimationDrawable
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
        pernsonaAdam.setImageResource(R.drawable.adam_zero) // Define a imagem parada do personagem
        // Pare a animação de caminhada, se estiver em andamento
        (pernsonaAdam.drawable as? AnimationDrawable)?.stop()

        verificarSePersonagemEstaParado()
    }

    private fun movePersonagem(deltaX: Int) {
        val layoutParams = pernsonaAdam.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.leftMargin += deltaX // Altera a margem esquerda do ImageView
        pernsonaAdam.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }
}