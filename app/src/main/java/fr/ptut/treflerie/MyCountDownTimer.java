package fr.ptut.treflerie;

import android.os.CountDownTimer;

/**
 * Created by benja on 25/01/2018.
 */

public class MyCountDownTimer extends CountDownTimer {

    // This variable refer to the source activity which use this CountDownTimer object.
    private MainActivity sourceActivity;

    public void setSourceActivity(MainActivity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (this.sourceActivity != null) {
            // Invoke source activity's tick event method.
            this.sourceActivity.onCountDownTimerTickEvent(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (this.sourceActivity != null) {
            // Invoke source activity's tick event method.
            //this.sourceActivity.onCountDownTimerFinishEvent();
        }
    }
}