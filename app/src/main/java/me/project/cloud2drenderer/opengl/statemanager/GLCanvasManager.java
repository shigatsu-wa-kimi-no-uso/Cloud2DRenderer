package me.project.cloud2drenderer.opengl.statemanager;


import static android.opengl.GLES30.*;

import me.project.cloud2drenderer.opengl.GLErrorUtils;

public class GLCanvasManager {


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

    public static void enableGLFunction(int cap){
        glEnable(cap);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
