package me.project.cloud2drenderer.renderer.controller;

import static android.opengl.GLES20.*;

import java.util.Stack;

import me.project.cloud2drenderer.opengl.GLErrorUtils;
import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;

public class CanvasController {


    public void clearColor(){
        GLCanvasManager.clearColor(0,0,0,0);
    }

    public void clearColor(float red,float green,float blue,float alpha){
        GLCanvasManager.clearColor(red,green,blue,alpha);
    }

    public void clearCanvas(float red,float green,float blue,float alpha){
        clearColor(red,green,blue,alpha);
        clearCanvas();
    }
    public void clearCanvas(){
        GLCanvasManager.clearCanvas(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT|GL_STENCIL_BUFFER_BIT);
    }
    public void enableDepthTest(){
        GLCanvasManager.enableGLFunction(GL_DEPTH_TEST);
    }

    public void enableBlend(){
        GLCanvasManager.enableGLFunction(GL_BLEND);
    }

    public void setCanvasSize(int width,int height){
        assert width > 0 && height > 0;
        GLCanvasManager.setCanvasSize(width,height);
    }

    public void setCanvasSize(int width,float aspect){
        assert aspect > 0;
        int height = (int)(width / aspect);
        setCanvasSize(width,height);
    }

    public void drawTriangleArray(int first,int count){
        assert first >= 0 && count > 0;
        GLCanvasManager.drawArrays(GL_TRIANGLES,first,count);
        GLErrorUtils.assertNoError();
    }

    public void drawTriangleByIndex(int count){
        GLCanvasManager.drawElements(GL_TRIANGLES,count,GL_UNSIGNED_INT,0);
        GLErrorUtils.assertNoError();
    }

    public void drawTriangleArray(int count){
        drawTriangleArray(0,count);
    }

}
