package com.mya.games.colipop;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColiPopView extends SurfaceView implements SurfaceHolder.Callback {

    static final String TAG = "ColiPop";

    /**
     * The thread that actually draws the animation
     */
    ColiPopThread thread;

    TextView timerView;

    Button buttonRetry;

    // Button buttonRestart; 
    TextView textView;

    /**
     * The constructor called from the main ColiPop activity
     *
     * @param context
     * @param attrs
     */
    public ColiPopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        // except if used in the layout editor.
        if (isInEditMode() == false) {
            thread = new ColiPopThread(holder, context, new Handler() {

                public void handleMessage(Message m) {

                    timerView.setText(m.getData().getString("text"));

                    if (m.getData().getString("STATE_GAME_END") != null) {
                        //buttonRestart.setVisibility(View.VISIBLE);
                        buttonRetry.setVisibility(View.VISIBLE);

                        timerView.setVisibility(View.INVISIBLE);

                        textView.setVisibility(View.VISIBLE);

                        if (m.getData().getString("PLAYER_WIN") != null) {
                            textView.setText(R.string.winText);
                        } else if (m.getData().getString("CPU_WIN") != null) {
                            textView.setText(R.string.loseText);
                        } else {
                            textView.setText(R.string.gameOverText);
                        }

                        timerView.setText("0:00");
                        textView.setHeight(20);

                    }
                }//end handle msg
            });
        }

        setFocusable(true); // make sure we get key events

        //Log.d(TAG, "@@@ done creating view!");
    }


    /**
     * Pass in a reference to the timer view widget so we can update it from here.
     *
     * @param tv
     */
    public void setTimerView(TextView tv) {
        this.timerView = tv;
    }


    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            if (this.thread != null)
                this.thread.pause();

        }
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     *
     * @return the animation thread
     */
    public ColiPopThread getThread() {
        return this.thread;
    }


    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.thread.setSurfaceSize(width, height);
    }


    public void surfaceCreated(SurfaceHolder arg0) {
        // Si el thread esta finalizado creamos uno nuevo
        if (this.thread.getState() == Thread.State.TERMINATED) {
            this.thread = ColiPopThread.newColiPopThread(thread);
            //Log.d(TAG, "surfaceCreated: New thread created!");
        }
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        this.thread.setRunning(true);
        this.thread.start();
        //Log.d(TAG, "surfaceCreated: Thread started!");
    }


    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        this.thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
                //Log.d(TAG, "surfaceDestroyed: Thread killed!");
            } catch (InterruptedException e) {
            }
        }
    }

    public void SetButtonView(Button buttonRetry) {
        this.buttonRetry = buttonRetry;
    }


    //we reuse the help screen from the end game screen.
    public void SetTextView(TextView textView) {
        this.textView = textView;
    }

    public void destroy() {
        if (thread != null) {
            thread.destroy();
        }
    }

}

