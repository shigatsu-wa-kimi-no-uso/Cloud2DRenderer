package me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.project.cloud2drenderer.global.GlobalConstants;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ElemType;


@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VertexAttribute {

    int order();
    String attributeName() default "";
    int elemCnt();
    ElemType elemType() default ElemType.AUTO;
    boolean normalized() default false;
    int strideInBytes() default -1;
    int offset() default -1;

}

