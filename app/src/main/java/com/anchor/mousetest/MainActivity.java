package com.anchor.mousetest;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MouseTest";
    private ImageView myMouse = null;
    private int[] viewCoords = null;
    private int buttonBits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMouse = (ImageView) findViewById(R.id.myPointer);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(viewCoords == null) {
            viewCoords = new int[2];
            myMouse.getLocationOnScreen(viewCoords);
        }
    }

    private void updateVisuals()
    {
        if(buttonBits == 0)
        {
            myMouse.setImageResource(R.drawable.m0);
        }
        else if(buttonBits == 1)
        {
            myMouse.setImageResource(R.drawable.m1);
        }
        else if(buttonBits == 2)
        {
            myMouse.setImageResource(R.drawable.m2);
        }
        else if(buttonBits == 3)
        {
            myMouse.setImageResource(R.drawable.m3);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Log.d(TAG, String.format("onTouchEvent() source:%d tooltype:%d action:%d", event.getSource(), event.getToolType(0), event.getAction()));

        myMouse.setX(event.getRawX() - viewCoords[0]);
        myMouse.setY(event.getRawY() - viewCoords[1]);

        if((event.getSource() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)
        {
            return true;
        }

        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                buttonBits |= 1;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                buttonBits &= (~1);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        updateVisuals();
        return true;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, String.format("onKeyDown() source:%d keycode:%d repeat:%d", event.getSource(), keyCode, event.getRepeatCount()));

        if ( (event.getSource() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE )
        {
            if ( event.getRepeatCount() == 0 && keyCode == 4 && ((buttonBits&2) == 0) )
            {
                buttonBits |= 2;
                updateVisuals();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        Log.d(TAG, String.format("onKeyUp() source:%d keycode:%d repeat:%d", event.getSource(), keyCode, event.getRepeatCount()));

        if ( (event.getSource() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE )
        {
            if ( event.getRepeatCount() == 0 && keyCode == 4 && ((buttonBits&2) == 2) )
            {
                buttonBits &= (~2);
                updateVisuals();
                return true;
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override public void onBackPressed() {
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        Log.d(TAG, String.format("onGenericMotionEvent() source:%d action:%d button:%d bs:%d x:%d y:%d", event.getSource(), event.getAction(), event.getActionButton(), event.getButtonState(), (int)event.getRawX(), (int)event.getRawY()));

        if ((event.getSource() & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)
        {
            myMouse.setX(event.getRawX() - viewCoords[0]);
            myMouse.setY(event.getRawY() - viewCoords[1]);

            if(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS)
            {
                if(event.getActionButton() == 1 && ((buttonBits&1) == 0))
                {
                    buttonBits |= 1;
                }
                else if(event.getActionButton() == 2 && ((buttonBits&2) == 0))
                {
                    buttonBits |= 2;
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE)
            {
                if(event.getActionButton() == 1 && ((buttonBits&1) == 1))
                {
                    buttonBits &= (~1);
                }
                else if(event.getActionButton() == 2 && ((buttonBits&2) == 2))
                {
                    buttonBits &= (~2);
                }
            }

            updateVisuals();
            return true;
        }

        return super.onGenericMotionEvent(event);
    }
}
