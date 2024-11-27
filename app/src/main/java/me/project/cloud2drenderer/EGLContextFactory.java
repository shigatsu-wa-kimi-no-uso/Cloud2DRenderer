package me.project.cloud2drenderer;


import static android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;


public class EGLContextFactory implements GLSurfaceView.EGLContextFactory {

    private static final String TAG = EGLContextFactory.class.getSimpleName();
    private static double glVersion = 3.1;

    public EGLContext createContext(
            EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {

        Log.w(TAG, "creating OpenGL ES " + glVersion + " context");
        int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, (int) glVersion,
                EGL10.EGL_NONE };
        // attempt to create a OpenGL ES 3.0 context
        EGLContext context = egl.eglCreateContext(
                display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
        return context; // returns null if 3.0 is not supported;
    }

    @Override
    public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {

    }
}