package com.mya.games.colipop

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView

class ColiPop : Activity(), View.OnClickListener {

    private val TAG = "ColiPop"

    /**
     * A handle to the thread that's actually running the animation.
     */
    private lateinit var coliPopThread: ColiPopThread

    /**
     * A handle to the View in which the game is running.
     */
    private lateinit var coliPopView: ColiPopView

    // the play start button
    private lateinit var button: Button

    // used to hit retry
    private lateinit var buttonRetry: Button

    // the window for instructions and such
    private lateinit var textView: TextView

    // game window timer
    private lateinit var timerView: TextView

    /**
     * Required method from parent class
     *
     * @param savedInstanceState - The previous instance of this app
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        this.coliPopView = findViewById<View>(R.id.ColiPopView) as ColiPopView
        this.coliPopThread = this.coliPopView.thread

        // look up the happy shiny button
        this.button = findViewById<View>(R.id.Button01) as Button
        this.button.setOnClickListener(this)

        this.buttonRetry = findViewById<View>(R.id.Button02) as Button
        this.buttonRetry.setOnClickListener(this)

        // set up handles for instruction text and game timer text
        this.textView = findViewById<View>(R.id.text) as TextView
        this.timerView = findViewById<View>(R.id.timer) as TextView

        this.coliPopView.timerView = this.timerView

        this.coliPopView.buttonRetry = this.buttonRetry

        this.coliPopView.textView = this.textView
    }

    /**
     * Handles component interaction
     *
     * @param v The object which has been clicked
     */
    override fun onClick(v: View) {
        // this is the first screen
        if (this.coliPopThread.getGameState() == ColiPopThread.STATE_START) {
            this.button.setText(R.string.play)
            this.textView.visibility = View.VISIBLE
            this.textView.setText(R.string.helpText)
            this.coliPopThread.setGameState(ColiPopThread.STATE_PLAY)

        // we have entered game play, now we about to start running
        } else if (this.coliPopThread.getGameState() == ColiPopThread.STATE_PLAY) {
            this.button.visibility = View.INVISIBLE
            this.textView.visibility = View.INVISIBLE
            this.timerView.visibility = View.VISIBLE
            this.coliPopThread.setGameState(ColiPopThread.STATE_RUNNING)

        // this is a retry button
        } else if (this.buttonRetry == v) {
            this.buttonRetry.visibility = View.INVISIBLE
            this.button.text = "PLAY!"
            this.button.visibility = View.VISIBLE
            this.textView.setText(R.string.helpText)
            this.textView.visibility = View.VISIBLE
            this.coliPopThread.setGameState(ColiPopThread.STATE_PLAY)

        } else {
            Log.d(TAG,"unknown click " + v.id)
        }
    }

    /**
     * Standard override Touch event
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action

        val x = event.x.toInt()
        val y = event.y.toInt()

        if (action == MotionEvent.ACTION_DOWN) {
            return this.coliPopThread.doTouchMotion(MotionEvent.ACTION_DOWN, x, y)

        } else if (action == MotionEvent.ACTION_UP) {
            return this.coliPopThread.doTouchMotion(MotionEvent.ACTION_UP, x, y)

        } else if (action == MotionEvent.ACTION_MOVE) {
            return this.coliPopThread.doTouchMotion(MotionEvent.ACTION_MOVE, x, y)
        }

        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        coliPopView.destroy()
        System.gc()
    }

    override fun onPause() {
        super.onPause()
        System.gc()
    }
}
