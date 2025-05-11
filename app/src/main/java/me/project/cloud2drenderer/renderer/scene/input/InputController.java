package me.project.cloud2drenderer.renderer.scene.input;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InputController implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private ImageButton moveImageButton;
    private ImageButton rotateImageButton;
    private ImageButton upDownImageButton;
    private ImageButton resetImageButton;

    private GestureDetector gestureDetector;



    private CameraInputHandler cameraInputHandler;

    private InputMode currInputMode = InputMode.MOVE;

    private int[][] buttonColorTable;
    private ImageButton[] buttons;



    void initButtonColorTable(){
        int selectedColor = Color.argb(255, 255, 200, 0);
        int unselectedColor = Color.argb(255, 200, 200, 200);

        buttons = new ImageButton[3];
        buttons[InputMode.MOVE.ordinal()] = moveImageButton;
        buttons[InputMode.UP_DOWN.ordinal()] = upDownImageButton;
        buttons[InputMode.ROTATE.ordinal()] = rotateImageButton;

        buttonColorTable = new int[InputMode.values().length][buttons.length];
        for(InputMode mode : InputMode.values()){
            for(int j=0;j< buttons.length;j++){
                int i = mode.ordinal();
                if(i==j){
                    buttonColorTable[i][j] = selectedColor;
                }else{
                    buttonColorTable[i][j] = unselectedColor;
                }
            }
        }
    }

    public InputController(Context ctx,
                           ImageButton moveImageButton,
                           ImageButton rotateImageButton,
                           ImageButton upDownImageButton,
                           ImageButton resetImageButton){
        this.moveImageButton = moveImageButton;
        this.rotateImageButton = rotateImageButton;
        this.upDownImageButton = upDownImageButton;
        this.resetImageButton = resetImageButton;
        initButtonColorTable();
        this.moveImageButton.setOnClickListener(v -> {setCurrentInputMode(InputMode.MOVE);});
        this.rotateImageButton.setOnClickListener(v -> {setCurrentInputMode(InputMode.ROTATE);});
        this.upDownImageButton.setOnClickListener(v -> {setCurrentInputMode(InputMode.UP_DOWN);});
        this.resetImageButton.setOnClickListener(v -> {});

        gestureDetector = new GestureDetector(ctx, this);
        setCurrentInputMode(currInputMode);
    }



    public void setCameraInputHandler(CameraInputHandler cameraInputHandler){
        this.cameraInputHandler = cameraInputHandler;
    }

    private void setCurrentInputMode(InputMode inputMode) {
        currInputMode = inputMode;
        int modeId = inputMode.ordinal();
        moveImageButton.setBackgroundColor(buttonColorTable[modeId][InputMode.MOVE.ordinal()]);
        rotateImageButton.setBackgroundColor(buttonColorTable[modeId][InputMode.ROTATE.ordinal()]);
        upDownImageButton.setBackgroundColor(buttonColorTable[modeId][InputMode.UP_DOWN.ordinal()]);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        cameraInputHandler.handleScroll(currInputMode,distanceX, distanceY);
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        return true;
    }



}
