package me.project.cloud2drenderer.renderer.context;

import androidx.annotation.NonNull;

import java.util.Vector;
import java.util.function.Consumer;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderUniformSetterWrapper;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.ResBindingMethod;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;
import me.project.cloud2drenderer.renderer.procedure.drawing.DrawMethod;
import me.project.cloud2drenderer.renderer.scene.Camera;



public abstract class RenderContext {


    public static Camera camera;
    public int contextId;

    public LoadedModel loadedModel;

    private DrawMethod drawMethod;

    private ResBindingMethod resourseBinder;

    private final Vector<ShaderUniformSetterWrapper> autoAssignedUniforms;

    private final Vector<ShaderUniformSetterWrapper> assignments;


    public RenderContext(){
        autoAssignedUniforms = new Vector<>();
        assignments = new Vector<>();
    }

    public void setDrawMethod(DrawMethod drawMethod) {
       this.drawMethod=drawMethod;
    }

    public void setGLResourceBinder(ResBindingMethod bindingMethod){
        this.resourseBinder = bindingMethod;
    }

    public <T> void commitUniformAssignment(@NonNull UniformVar<T> uniformVar){
        assignments.add(uniformVar.getUniformSetterWrapper());
    }

    public void addAutoAssignedUniforms(ShaderUniformSetterWrapper setterWrapper){
        autoAssignedUniforms.add(setterWrapper);
    }


    public void applyUniformAssignments(){
        autoAssignedUniforms.forEach(ShaderUniformSetterWrapper::apply);
        assignments.forEach(ShaderUniformSetterWrapper::apply);
        assignments.clear();
    }

    public void draw(){
        drawMethod.draw(this);
    }

    public void bindGLResources(){
       resourseBinder.bind(this);
    }

    public abstract Shader getShader();


    public Texture[] getTextures(){
        return new Texture[]{Texture.nullTexture()};
    }

    public abstract void setMaterial(Material material);

    public abstract void adjustContext();

    public abstract void initContext();

}
