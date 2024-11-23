package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.BufferMode;

@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VertexDataClass {

    BufferMode bufferMode() default BufferMode.INTERLEAVED;
    boolean autoLocate() default true;
}
