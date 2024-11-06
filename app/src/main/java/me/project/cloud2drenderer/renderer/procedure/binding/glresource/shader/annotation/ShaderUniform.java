package me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformFlag;

@Target({ElementType.FIELD,ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShaderUniform {
    String uniformName();

    UniformFlag[] flags() default {UniformFlag.AUTO_ASSIGN};


}
