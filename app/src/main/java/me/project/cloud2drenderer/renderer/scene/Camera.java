package me.project.cloud2drenderer.renderer.scene;


import android.opengl.Matrix;

import me.project.cloud2drenderer.util.MatUtils;

public class Camera {

    private final float[] view;

    private final float[] projection;
    private float[] cameraPos;

    private float[] orientation;
    private final float[] yup = {0.0F, 1.0F, 0.0F};

    private float[] axisX;

    private float[] axisY;

    private float aspect;
    private float fovy = 45;
    private float zNear = 0.1f;
    private float zFar = 100f;

    public Camera() {
        view = new float[16];
        Matrix.setIdentityM(view,0);
        projection = new float[16];
        Matrix.setIdentityM(projection,0);
    }


    public void setView(float[] pos, float[] orientation) {
        this.cameraPos = pos;
        this.orientation = orientation;
    }
    public void setPosition(float[] pos){
        this.cameraPos = pos;
    }
    public float[] getPosition(){
        return cameraPos;
    }

    public float[] getOrientation(){
        return orientation;
    }

    public float[] getYup(){
        return yup;
    }

    public float[] getAxisX(){
        return axisX;
    }

    public float[] getAxisY(){
        return axisY;
    }

    public void setOrientation(float[] orientation){
        this.orientation = orientation;
        MatUtils.normalize(this.orientation);
    }

    void updateAxis(){
        axisX = MatUtils.cross(orientation,yup);
        axisY = MatUtils.cross(axisX,orientation);
    }
    public void rotate(float camXAngle, float camYAngle){
        float[] cross = MatUtils.cross(orientation, yup);
        float[] newOrientation = MatUtils.rotateVec3(orientation, camXAngle, cross);
        float aa = MatUtils.angle(newOrientation, yup);
        if (Math.abs(aa) <= 85) {
            orientation = newOrientation;
        }
        orientation = MatUtils.rotateVec3(orientation, camYAngle, yup);
        MatUtils.normalize(this.orientation);
    }

    public void setFrustumFieldOfView(float fov){
        this.fovy = fov;
    }
    public void setFrustumAspect(float aspect) {
        this.aspect = aspect;
    }

    public void setFrustumNear(float near){
        this.zNear = near;
    }

    public void setFrustumFar(float far){
        this.zFar = far;
    }
    public void setFrustum(float fov, float aspect, float zNear, float zFar) {
        setFrustumFieldOfView(fov);
        setFrustumAspect(aspect);
        setFrustumNear(zNear);
        setFrustumFar(zFar);
    }

    public void updateProjection(){
        Matrix.perspectiveM(projection, 0, fovy, aspect, zNear, zFar);
    }

    public void update(){
        updateView();
        updateProjection();
    }

    public void updateView() {
        float[] cent = MatUtils.add(cameraPos, orientation);
        Matrix.setLookAtM(view, 0, cameraPos[0], cameraPos[1], cameraPos[2],
                cent[0], cent[1], cent[2], yup[0], yup[1], yup[2]);
        updateAxis();
    }

    public void moveCamera(float deltaX,float deltaY,float deltaZ){
        cameraPos[0] += deltaX;
        cameraPos[1] += deltaY;
        cameraPos[2] += deltaZ;
    }




    public float[] getView(){
        return view;
    }


    public float[] getProjection(){
        return projection;
    }

}
