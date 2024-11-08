package me.project.cloud2drenderer.util;

public class FileUtils {


    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
