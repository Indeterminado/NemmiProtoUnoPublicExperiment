package com.feup.nemiprotouno;
// ----------------------------------------------------------------------------
//
//  NEMMI is developed by Alexandre Clément at the University of Porto's Faculty of Engineering
//  Released under GNU Affero General Public License version 3
//  https://opensource.org/licenses/AGPL-3.0
//
// ----------------------------------------------------------------------------
//
//  Interaction methods used in app
//
// ----------------------------------------------------------------------------

import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;
import java.util.Objects;

public abstract class InteractionActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        SensorEventListener
{
    // debug variables
    public static final String TAG = "NEMI_OUT";
    private final Boolean parentDebug = false;


    // touch variables
    protected GestureDetectorCompat mDetector;
    private Point screen;
    private final Boolean multiTouch = true;
    private final int maxTouches = 5;

    // variables for move smoothing
    protected int[] event_index = new int[maxTouches]; // move event array for each touch
    protected float[][][] events = new float[maxTouches][2][10]; //

    private final ArrayList<Integer> activeTouches = new ArrayList<>();

    // region motion sensors variables
    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetic;
    float[] accels=new float[3];
    float[] mags=new float[3];
    private static class coords {
        private float a=0;
        private float b=0;
        private float c=0;

        float get(int index) {
            switch(index) {
                case 0:
                    return this.a;
                case 1:
                    return this.b;
                default:
                    return this.c;
            }
        }

        void set(int index, float value) {
            switch(index) {
                case 0:
                    this.a = value;
                case 1:
                    this.b = value;
                default:
                    this.c = value;
            }
        }
    }
    private final ArrayList<coords> accelerometerValues = new ArrayList<>();

    // setup sensor behaviour
    Boolean physicalAvailable = true;
    float accelerometerThreshold = 0.3f;
    int shakeForceThreshold = 15;
    private long lastShake = 0;
    int shakeTimeThreshold = 200;
    private static final int smoothEvents = 10;

    // endregion

    // region application lifetime/flow methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initGUI();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register accelerometer listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // release accelerometer listener
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // release accelerometer listener
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }
    //endregion

    private void initGUI(){
        // hide status bar and set activity to fullscreen
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        screen = new Point();
        display.getSize(screen);

        mDetector = new GestureDetectorCompat(this, this);

        for(int i=0; i<maxTouches; i++) {
            event_index[i] = 0;
        }
    }

    protected void initListeners() {
        // setup gesture detector for touch events
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

        // setup motion sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            // check if phone has sensorManager
            assert sensorManager != null;
        }
        catch (AssertionError e) {
            Toast.makeText(this, "Your phone doesn't have sensor manager", Toast.LENGTH_LONG).show();
            physicalAvailable = false;
        }
        
        // variable is false, it has failed previous test
        if(physicalAvailable) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

                if (parentDebug)
                    Log.d(TAG, "Accelerometer max: " + accelerometer.getMaximumRange());
            } else {
                Toast.makeText(this, "Your phone doesn't have an accelerometer", Toast.LENGTH_LONG).show();
                physicalAvailable = false;
            }

            magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (magnetic != null) {
                sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);

                if (parentDebug)
                    Log.d(TAG, "Magnetic max: " + magnetic.getMaximumRange());
            } else {
                Toast.makeText(this, "Your phone doesn't have a gyroscope", Toast.LENGTH_LONG).show();
                physicalAvailable = false;
            }
        }
    }

    //region overrideable method declarations
    protected abstract void doDblTap(int index, int x, int y);

    protected abstract void doTouchUp(int index, int x, int y);
    protected abstract void doTouchDown(int index, int x, int y);
    protected abstract void doMove(int index, int x, int y);

    protected abstract void doShake();
    protected abstract void doAccel(Float X, Float Y, Float Z);

    //protected abstract void do2Finger(float angle, float scale, float centerX, float centerY);

    //endregion

    /***********************
     * INTERACTION METHODS *
     ***********************/

    // region touch methods

    private Boolean moveMethod(MotionEvent event) {

        int pointerCount = event.getPointerCount();
        int mTouches = multiTouch ? maxTouches : 1;

        for(int pIndex = 0; pIndex < pointerCount; pIndex++) {
            if (pIndex > mTouches - 1)
                break;

            int[] pos = getScreenPos(event.getX(pIndex), event.getY(pIndex));
            int posX = pos[0];
            int posY = pos[1];

            int pointId = event.getPointerId(pIndex);

            if (activeTouches.contains(pointId)) {
                int pointIndex = activeTouches.indexOf(pointId);


                if (event_index[pointIndex] == 9) {
                    float totalX = events[pointIndex][0][9] = posX;
                    float totalY = events[pointIndex][1][9] = posY;

                    for (int i = 0; i < 9; i++) {
                        totalX += events[pointIndex][0][i];
                        totalY += events[pointIndex][1][i];

                        events[pointIndex][0][i] = events[pointIndex][0][i + 1];
                        events[pointIndex][1][i] = events[pointIndex][1][i + 1];
                    }

                    totalX *= 0.1f;
                    totalY *= 0.1f;

                    doMove(pointIndex, Math.round(totalX), Math.round(totalY));
                } else {
                    events[pointIndex][0][event_index[pointIndex]] = posX;
                    events[pointIndex][1][event_index[pointIndex]] = posY;
                    event_index[pointIndex]++;
                }
            }

            if (parentDebug)
                Log.d(TAG, "Action was Move");
        }
        return true;
    }

    private Boolean downMethod(MotionEvent event) {
        int pointID = event.getPointerId(event.getActionIndex());
        int pointIndex;

        if(activeTouches.contains(pointID))
            pointIndex = activeTouches.indexOf(pointID);
        else {
            pointIndex = event.getActionIndex();

            if(activeTouches.size() < maxTouches)
                activeTouches.add(pointIndex, pointID);
            else
                activeTouches.set(pointIndex, pointID);
        }

        int[] pos = getScreenPos(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
        int posX = pos[0];
        int posY = pos[1];

        if(multiTouch && event.getPointerCount() > 1) {
            if(parentDebug)
                Log.d(TAG, "Action was Pointer DOWN");
        }
        else{
            if(parentDebug)
                Log.d(TAG, "Action was Touch DOWN");
        }

        doTouchDown(pointIndex, posX, posY);

        return true;
    }

    private Boolean upMethod(MotionEvent event) {
        int pointID = event.getPointerId(event.getActionIndex());

        if(activeTouches.contains(pointID)) {

            int pointIndex = activeTouches.indexOf(pointID);
            activeTouches.set(pointIndex, -1);

            int[] pos = getScreenPos(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
            int posX = pos[0];
            int posY = pos[1];

            //checkTap(event);

            if (multiTouch && event.getPointerCount() > 1) {
                event_index[event.getActionIndex()] = 0;

                if (parentDebug)
                    Log.d(TAG, "Action was Pointer UP");
            } else {
                event_index[0] = 0;
                if (parentDebug)
                    Log.d(TAG, "Action was Touch UP");
            }

            doTouchUp(pointIndex, posX, posY);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        int mTouches = multiTouch ? maxTouches : 1;

        switch(action) {
            case (MotionEvent.ACTION_DOWN):
            case (MotionEvent.ACTION_POINTER_DOWN):
                if(event.getActionIndex() > mTouches-1)
                    break;
                return downMethod(event);
            case (MotionEvent.ACTION_MOVE):
                return moveMethod(event);
            case (MotionEvent.ACTION_UP):
            case (MotionEvent.ACTION_POINTER_UP):
                if(event.getActionIndex() > mTouches-1)
                    break;
                return upMethod(event);
        }

        return super.onTouchEvent(event);
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return true;
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        int[] pos = getScreenPos(motionEvent.getX(), motionEvent.getY());
        int index = motionEvent.getActionIndex();

        doDblTap(index, pos[0], pos[1]);
        return true;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    public void onShowPress(MotionEvent motionEvent) {

    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {

    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return true;
    }

    //endregion

    //region motion sensor methods

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float[] getAveragedAccel(float... values) {
        float[] avgs = {0,0,0};
        float[] total = {0,0,0};
        int arraySize = accelerometerValues.size();

        if(arraySize > 1) {
            float xDiff = Math.abs(values[0] - accelerometerValues.get(arraySize-1).get(0));
            float yDiff = Math.abs(values[1] - accelerometerValues.get(arraySize-1).get(1));
            float zDiff = Math.abs(values[2] - accelerometerValues.get(arraySize-1).get(2));

            if(xDiff > accelerometerThreshold || yDiff > accelerometerThreshold || zDiff > accelerometerThreshold) {
                coords t = new coords();
                t.set(0, values[0]);
                t.set(1, values[1]);
                t.set(2, values[2]);

                accelerometerValues.add(t);
                arraySize++;
            }
        }
        else {
            coords t = new coords();
            t.set(0, values[0]);
            t.set(1, values[1]);
            t.set(2, values[2]);

            accelerometerValues.add(t);
            arraySize++;
        }

        if(arraySize >= smoothEvents) {
            accelerometerValues.remove(0);
            arraySize--;
        }

        for(int i=0; i < arraySize; i++) {
            total[0] += accelerometerValues.get(i).get(0);
            total[1] += accelerometerValues.get(i).get(1);
            total[2] += accelerometerValues.get(i).get(2);
        }

        avgs[0] = total[0] / arraySize;
        avgs[1] = total[1] / arraySize;
        avgs[2] = total[2] / arraySize;

        return avgs;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(physicalAvailable) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accels = event.values.clone();
                    break;
            }

            if(accels !=null) {
                float[] avgs;

                // handle shakes
                double gForce = Math.sqrt(Math.pow(accels[0], 2) + Math.pow(accels[1], 2) + Math.pow(accels[2], 2));

                if ((System.currentTimeMillis() - lastShake > shakeTimeThreshold) && (gForce > shakeForceThreshold)) {
                    doShake();
                    lastShake = System.currentTimeMillis();
                }
                accels[0] = Math.min(accels[0], 9.2f);
                accels[0] = Math.max(accels[0], -9.2f);
                accels[1] = Math.min(accels[1], 9.2f);
                accels[1] = Math.max(accels[1], -9.2f);
                accels[2] = Math.min(accels[2], 9.2f);
                accels[2] = Math.max(accels[2], -9.2f);

                avgs = getAveragedAccel(accels[0], accels[1], accels[2]);
                doAccel(avgs[0], avgs[1], avgs[2]);
            }
        }
    }

    //endregion

    //region accessory methods
    protected int[] getScreenPos(Float x, Float y) {
        int[] pos = {0,0};
        pos[0] = Math.round(x/screen.x * 100);
        pos[1] = 100-Math.round(y/screen.y * 100);
        return pos;
    }
    //endregion
}
