package me.project.cloud2drenderer.util;

public class ColorUtils {

    public static float[] normalizeColor(int r,int g,int b){
        return new float[]{r/255.0f,g/255.0f,b/255.0f};
    }



    public static float[] getIntensity(float normR,float normG,float normB,float intensity){
        return new float[]{normR*intensity,normG*intensity,normB*intensity};
    }

    public static float[] getIntensity(float[] color,float intensity){
        return getIntensity(color[0],color[1],color[2],intensity);
    }
    public static float[] getIntensity(int r,int g,int b,float intensity){
        float[] color = normalizeColor(r,g,b);
        return getIntensity(color,intensity);
    }

}
