package me.project.cloud2drenderer.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationUtils {

    public static List<Field> getAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationType){
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            List<Field> fieldsOfSuper = Arrays.asList(clazz.getSuperclass().getDeclaredFields());
            fields.addAll(fieldsOfSuper);
        }
        fields.removeIf(field -> !field.isAnnotationPresent(annotationType));
        return fields;
    }

    public static List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationType){
        List<Method> methods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
        if (clazz.getSuperclass() != null) {
            List<Method> methodsOfSuper = Arrays.asList(clazz.getSuperclass().getDeclaredMethods());
            methods.addAll(methodsOfSuper);
        }
        methods.removeIf(method -> !method.isAnnotationPresent(annotationType));
        return methods;
    }
}
