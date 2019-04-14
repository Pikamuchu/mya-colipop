package com.mya.games.colipop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColiPop extends Activity implements View.OnClickListener {
    /**
     * A handle to the thread that's actually running the animation.
     */
    private ColiPopThread coliPopThread;

    /**
     * A handle to the View in which the game is running.
     */
    private ColiPopView coliPopView;

    // the play start button
    private Button button;

    // used to hit retry
    private Button buttonRetry;

    // the window for instructions and such
    private TextView textView;

    // game window timer
    private TextView timerView;

    /**
     * Required method from parent class
     *
     * @param savedInstanceState - The previous instance of this app
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        this.coliPopView = (ColiPopView) findViewById(R.id.ColiPopView);
        this.coliPopThread = this.coliPopView.getThread();

        // look up the happy shiny button
        this.button = (Button) findViewById(R.id.Button01);
        this.button.setOnClickListener(this);

        this.buttonRetry = (Button) findViewById(R.id.Button02);
        this.buttonRetry.setOnClickListener(this);

        // set up handles for instruction text and game timer text
        this.textView = (TextView) findViewById(R.id.text);
        this.timerView = (TextView) findViewById(R.id.timer);

        this.coliPopView.setTimerView(this.timerView);

        this.coliPopView.SetButtonView(this.buttonRetry);

        this.coliPopView.SetTextView(this.textView);
    }


    /**
     * Handles component interaction
     *
     * @param v The object which has been clicked
     */
    public void onClick(View v) {
        // this is the first screen
        if (this.coliPopThread.getGameState() == ColiPopThread.STATE_START) {
            this.button.setText(R.string.play);
            this.textView.setVisibility(View.VISIBLE);
            this.textView.setText(R.string.helpText);
            this.coliPopThread.setGameState(ColiPopThread.STATE_PLAY);

        // we have entered game play, now we about to start running
        } else if (this.coliPopThread.getGameState() == ColiPopThread.STATE_PLAY) {
            this.button.setVisibility(View.INVISIBLE);
            this.textView.setVisibility(View.INVISIBLE);
            this.timerView.setVisibility(View.VISIBLE);
            this.coliPopThread.setGameState(ColiPopThread.STATE_RUNNING);

        // this is a retry button
        } else if (this.buttonRetry.equals(v)) {
            this.buttonRetry.setVisibility(View.INVISIBLE);
            this.button.setText("PLAY!");
            this.button.setVisibility(View.VISIBLE);
            this.textView.setText(R.string.helpText);
            this.textView.setVisibility(View.VISIBLE);
            this.coliPopThread.setGameState(ColiPopThread.STATE_PLAY);

        } else {
            Log.d("COLIPOP VIEW", "unknown click " + v.getId());
        }
    }

    /**
     * Standard override Touch event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            return this.coliPopThread.doTouchMotion(MotionEvent.ACTION_DOWN, x, y);

        } else if (action == MotionEvent.ACTION_UP) {
            return this.coliPopThread.doTouchMotion(MotionEvent.ACTION_UP, x, y);

        } else if (action == MotionEvent.ACTION_MOVE) {
            return this.coliPopThread.doTouchMotion(MotionEvent.ACTION_MOVE, x, y);
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (coliPopView != null) {
            coliPopView.destroy();
        }
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

}
