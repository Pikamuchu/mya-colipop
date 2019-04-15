package com.mya.games.colipop

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView

class ColiPopView (context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    lateinit var thread: ColiPopThread
        private set

    lateinit var timerView: TextView

    lateinit var buttonRetry: Button

    lateinit var textView: TextView

    init {

        // register our interest in hearing about changes to our surface
        val holder = holder
        holder.addCallback(this)

        // create thread only; it's started in surfaceCreated()
        // except if used in the layout editor.
        if (isInEditMode == false) {
            thread = ColiPopThread(holder, context, object : Handler() {

                override fun handleMessage(m: Message) {

                    timerView.text = m.data.getString("text")

                    if (m.data.getString("STATE_GAME_END") != null) {
                        //buttonRestart.setVisibility(View.VISIBLE);
                        buttonRetry.visibility = View.VISIBLE

                        timerView.visibility = View.INVISIBLE

                        textView.visibility = View.VISIBLE

                        if (m.data.getString("PLAYER_WIN") != null) {
                            textView.setText(R.string.winText)
                        } else if (m.data.getString("CPU_WIN") != null) {
                            textView.setText(R.string.loseText)
                        } else {
                            textView.setText(R.string.gameOverText)
                        }

                        timerView.text = "0:00"
                        textView.height = 20

                    }
                }//end handle msg
            })
        }

        isFocusable = true // make sure we get key events

        //Log.d(TAG, "@@@ done creating view!");
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (!hasWindowFocus) {
            this.thread.pause()
        }
    }

    /* Callback invoked when the surface dimensions change. */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        this.thread.setSurfaceSize(width, height)
    }

    override fun surfaceCreated(arg0: SurfaceHolder) {
        // Si el thread esta finalizado creamos uno nuevo
        if (this.thread.state == Thread.State.TERMINATED) {
            this.thread = ColiPopThread.newColiPopThread(thread)
            //Log.d(TAG, "surfaceCreated: New thread created!");
        }
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        this.thread.setRunning(true)
        this.thread.start()
        //Log.d(TAG, "surfaceCreated: Thread started!");
    }

    override fun surfaceDestroyed(arg0: SurfaceHolder) {
        var retry = true
        this.thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
                //Log.d(TAG, "surfaceDestroyed: Thread killed!");
            } catch (e: InterruptedException) {
            }

        }
    }

    fun destroy() {
        thread.destroy()
    }

    companion object {
        private val TAG = "ColiPop"
    }
}
