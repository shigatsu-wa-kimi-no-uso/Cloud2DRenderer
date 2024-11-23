package me.project.cloud2drenderer.opengl.statemanager;


import static android.opengl.GLES32.*;

import android.opengl.GLDebugHelper;

import java.util.Stack;

import me.project.cloud2drenderer.opengl.GLErrorUtils;

public class GLCanvasManager {

    static class GLCapabilityStatus {
        int capability;
        boolean status;
    }

    private static final Stack<GLCapabilityStatus> statusStack;

    static {
        statusStack = new Stack<>();
    }

    public static void storeCapabilityStatus(int cap){
        GLCapabilityStatus item = new GLCapabilityStatus();
        item.capability = cap;
        item.status = glIsEnabled(cap);
        statusStack.push(item);
        GLErrorUtils.assertNoError();
    }

    public static void restoreCapabilityStatus(){
        GLCapabilityStatus item = statusStack.pop();
        if(item.status){
            glEnable(item.capability);
        }else {
            glDisable(item.capability);
        }
        GLErrorUtils.assertNoError();
    }


    public static void clearColor(float red,float green,float blue,float alpha){
        glClearColor(red,green,blue,alpha);
        GLErrorUtils.assertNoError();
    }

    public static void clearCanvas(int mask){
        glClear(mask);
        GLErrorUtils.assertNoError();
    }

    public static void setCanvasSize(int width,int height){
        glViewport(0, 0, width,height);
        GLErrorUtils.assertNoError();
    }


    public static void setBlendFunction(int sfactor,int dfactor){
        glBlendFunc(sfactor,dfactor);

      //  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLErrorUtils.assertNoError();
    }

    public static void disableGLFunction(int cap){
        glDisable(cap);
        GLErrorUtils.assertNoError();
    }

    public static void enableGLFunction(int cap){
        glEnable(cap);
        GLErrorUtils.assertNoError();
    }


    public static void cullFaceMode(int mode){
        glCullFace(mode);
        GLErrorUtils.assertNoError();
    }

    public static void drawArrays(int mode,int first,int count){
        glDrawArrays(mode,first, count);
        GLErrorUtils.assertNoError();
    }

    public static void drawElements(int mode,int count,int type,int offset){
        glDrawElements(mode,count,type,offset);
        GLErrorUtils.assertNoError();
    }
}
