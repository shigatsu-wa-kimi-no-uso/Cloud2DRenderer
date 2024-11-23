package me.project.cloud2drenderer.renderer.scene.input;

import me.project.cloud2drenderer.renderer.scene.Camera;

public class CameraInputHandler {

    private float moveForwardSensitivity;

    private float moveSidewardsSensitivity;

    private float rotationSensitivityX;

    private float rotationSensitivityY;

    private float upDownSensitivity;

    private Camera camera;

    private final ScrollHandler[] scrollHandlers = new ScrollHandler[InputMode.values().length];

    public void setCameraMoveSensitivity(float forward,float sidewards) {
        this.moveForwardSensitivity = forward;
        this.moveSidewardsSensitivity = sidewards;
    }

    public void setCameraRotationSensitivity(float rotationSensitivityX, float rotationSensitivityY) {
        this.rotationSensitivityX = rotationSensitivityX;
        this.rotationSensitivityY = rotationSensitivityY;
    }

    public void setCameraUpDownSensitivity(float upDownSensitivity) {
        this.upDownSensitivity = upDownSensitivity;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @FunctionalInterface
    interface ScrollHandler{
        void handleScroll(float disX,float disY);
    }



    public CameraInputHandler(){
        scrollHandlers[InputMode.MOVE.ordinal()] = (disX, disY) -> {
            float[] orient = camera.getOrientation();
            float[] axisX = camera.getAxisX();
            float kForward = -moveForwardSensitivity *disY;
            float kSidewards = moveSidewardsSensitivity *disX;
            float[] forward = {kForward*orient[0],kForward*orient[1],kForward*orient[2]};
            float[] sidewards = {kSidewards*axisX[0],kSidewards*axisX[1],kSidewards*axisX[2]};
            camera.moveCamera(forward[0]+sidewards[0],forward[1]+sidewards[1],forward[2]+sidewards[2]);
        };
        scrollHandlers[InputMode.ROTATE.ordinal()] = (disX, disY) -> camera.rotate(rotationSensitivityX *disY, rotationSensitivityY *disX);
        scrollHandlers[InputMode.UP_DOWN.ordinal()] = (disX, disY) -> {
            float[] axisY = camera.getAxisY();
            float kVert = upDownSensitivity *disY;
            float[] delta = {kVert*axisY[0],kVert*axisY[1],kVert*axisY[2]};
            camera.moveCamera(delta[0],delta[1],delta[2]);
        };
    }


    void handleScroll(InputMode inputMode,float distanceX, float distanceY){
       scrollHandlers[inputMode.ordinal()].handleScroll(distanceX,distanceY);
       camera.updateView();
    }
}
