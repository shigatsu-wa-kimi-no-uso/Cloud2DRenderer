package me.project.cloud2drenderer.util;

public class DebugUtils {

    public static int checkRange(int val, int lowerBound, int upperBound) {
        assert val >= lowerBound && val <= upperBound;
        return val;
    }

    public static float checkRange(float val, float lowerBound, float upperBound) {
        assert val >= lowerBound && val <= upperBound;
        return val;
    }

    public static short checkRange(short val, short lowerBound, short upperBound) {
        assert val >= lowerBound && val <= upperBound;
        return val;
    }

    public static <T> T checkNotNull(T val) {
        assert val != null;
        return val;
    }

    public static <T> T assertNotNull(T val){
        assert val!=null;
        return val;
    }


}
