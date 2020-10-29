package com.metaltorchlabs.rockpaperscissors

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ContextThemeWrapper
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val rock = 1
    private val paper = 2
    private val scissors = 3
    private val gameTimeout = 3
    private lateinit var playerRockImage: Drawable
    private lateinit var playerPaperImage: Drawable
    private lateinit var playerScissorsImage: Drawable
    private lateinit var aiRockImage: Drawable
    private lateinit var aiPaperImage: Drawable
    private lateinit var aiScissorsImage: Drawable
    private lateinit var clothImage: Drawable
    private var playerChosenHand = rock
    private var aiChosenHand = rock
    private var gameIsInSession = false
    private var games = 0
    private var won = 0
    private var lost = 0
    private var tied = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        new_game_button.setOnClickListener {
            startGame()
        }

    }

    override fun onResume() {

        super.onResume()
        preloadImages()
        initPlayerHandChooser()

    }

    private fun preloadImages() {

        val playerHandStyle = ContextThemeWrapper(baseContext, R.style.AppTheme_ImageView_PlayerHand).theme
        playerRockImage = resources.getDrawable(R.drawable.hand_rock, playerHandStyle)
        playerPaperImage = resources.getDrawable(R.drawable.hand_paper, playerHandStyle)
        playerScissorsImage = resources.getDrawable(R.drawable.hand_scissors, playerHandStyle)
        clothImage = resources.getDrawable(R.drawable.cloth, theme)

        val aiHandStyle = ContextThemeWrapper(baseContext, R.style.AppTheme_ImageView_AIHand).theme
        aiRockImage = resources.getDrawable(R.drawable.hand_rock, aiHandStyle)
        aiPaperImage = resources.getDrawable(R.drawable.hand_paper, aiHandStyle)
        aiScissorsImage = resources.getDrawable(R.drawable.hand_scissors, aiHandStyle)

    }

    private fun initPlayerHandChooser() {

        player_hand_image.setOnClickListener {

            if (gameIsInSession) {

                playerChosenHand++

                if (playerChosenHand > scissors) {
                    playerChosenHand = rock
                }

                when (playerChosenHand) {
                    rock -> player_hand_image.setImageDrawable(playerRockImage)
                    paper -> player_hand_image.setImageDrawable(playerPaperImage)
                    scissors -> player_hand_image.setImageDrawable(playerScissorsImage)
                }

            }

        }

    }

    private fun startGame() {

        gameIsInSession = true

        game_result.visibility = View.GONE
        new_game_button.visibility = View.GONE
        ai_hand_image.setImageDrawable(clothImage)

        object : CountDownTimer((gameTimeout * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timer.text = ((millisUntilFinished / 1000) + 1 ).toString()
            }

            override fun onFinish() {

                gameIsInSession = false

                timer.text = ""
                game_result.visibility = View.VISIBLE
                new_game_button.visibility = View.VISIBLE

                aiChosenHand = Random.nextInt(rock, scissors + 1)
                when (aiChosenHand) {
                    rock -> ai_hand_image.setImageDrawable(aiRockImage)
                    paper -> ai_hand_image.setImageDrawable(aiPaperImage)
                    scissors -> ai_hand_image.setImageDrawable(aiScissorsImage)
                }

                when (playerChosenHand) {
                    rock ->
                        when (aiChosenHand) {
                            rock -> showTie()
                            paper -> showLose()
                            scissors -> showWin()
                        }
                    paper ->
                        when (aiChosenHand) {
                            rock -> showWin()
                            paper -> showTie()
                            scissors -> showLose()
                        }
                    scissors ->
                        when (aiChosenHand) {
                            rock -> showLose()
                            paper -> showWin()
                            scissors -> showTie()
                        }
                }

                games++

                games_value.text = games.toString()
                wins_value.text = won.toString()
                lost_value.text = lost.toString()
                tied_value.text = tied.toString()

            }

        }.start()

    }

    private fun showWin() {
        won++
        game_result.setTextColor(resources.getColor(R.color.color_win, theme))
        game_result.text = resources.getString(R.string.result_won)
        game_result.visibility = View.VISIBLE
    }

    private fun showLose() {
        lost++
        game_result.setTextColor(resources.getColor(R.color.color_lose, theme))
        game_result.text = resources.getString(R.string.result_lost)
        game_result.visibility = View.VISIBLE
    }

    private fun showTie() {
        tied++
        game_result.setTextColor(resources.getColor(R.color.color_tie, theme))
        game_result.text = resources.getString(R.string.result_tie)
        game_result.visibility = View.VISIBLE
    }

}