package me.project.cloud2drenderer.renderer.context;

import androidx.annotation.NonNull;

import java.util.Vector;

import me.project.cloud2drenderer.GLRenderer;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.buffer.VertexBufferBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderVariableSetterWrapper;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.GLResourceBinder;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.drawing.DrawMethod;
import me.project.cloud2drenderer.renderer.scene.Camera;



public abstract class RenderContext {


    public String name;

    public static Camera camera;

    public int contextId;

    public LoadedModel loadedModel;

    private DrawMethod drawMethod;

    private GLResourceBinder resourceBinder;

    private GLResourceBinder vertexBufferBinder;

    private GLResourceBinder textureBinder;

    private GLResourceBinder shaderBinder;

    private final Vector<ShaderVariableSetterWrapper> autoAssignedUniforms;

    private final Vector<ShaderVariableSetterWrapper> uniformAssignments;

    private final Vector<ShaderVariableSetterWrapper> attributeSetters;


    public RenderContext(){
        autoAssignedUniforms = new Vector<>();
        uniformAssignments = new Vector<>();
        attributeSetters = new Vector<>();
    }


    public void addAttributeSetters(ShaderVariableSetterWrapper setterWrapper) {
        attributeSetters.add(setterWrapper);
    }

    public abstract float[] getTransform();


    public GLResourceBinder getVertexBufferBinder() {
        return vertexBufferBinder;
    }

    public GLResourceBinder getShaderBinder() {
        return shaderBinder;
    }

    public void setShaderBinder(ShaderBinder shaderBinder) {
        this.shaderBinder = shaderBinder;
    }

    public void setVertexBufferBinder(VertexBufferBinder vertexBufferBinder) {
        this.vertexBufferBinder = vertexBufferBinder;
    }

    public abstract void setTransform(float[] transform);

    public void setDrawMethod(DrawMethod drawMethod) {
       this.drawMethod=drawMethod;
    }

    public void setGLResourceBinder(GLResourceBinder bindingMethod){
        this.resourceBinder = bindingMethod;
    }

    public <T> void commitUniformAssignment(@NonNull UniformVar<T> uniformVar){
        if(uniformVar.getUniformSetterWrapper()!=null){
            uniformAssignments.add(uniformVar.getUniformSetterWrapper());
        }
    }

    public void addAutoAssignedUniforms(ShaderVariableSetterWrapper setterWrapper){
        autoAssignedUniforms.add(setterWrapper);
    }

    public GLResourceBinder getTextureBinder() {
        return textureBinder;
    }

    public void setTextureBinder(GLResourceBinder textureBinder) {
        this.textureBinder = textureBinder;
    }

    public void bindShaderAttributePointers(){
        for(ShaderVariableSetterWrapper wrapper: attributeSetters){
            wrapper.apply();
        }
    }

    public void applyUniformAssignments(){
        for(ShaderVariableSetterWrapper wrapper: autoAssignedUniforms){
            wrapper.apply();
        }
        for(ShaderVariableSetterWrapper wrapper: uniformAssignments){
            wrapper.apply();
        }
        uniformAssignments.clear();
    }

    public void draw(){
        drawMethod.draw(this);
    }

    public void bindGLResources(){
       resourceBinder.bind(this);
    }

    public void bindShader(){
        shaderBinder.bind(this);
    }

    public void bindTexture(){
        textureBinder.bind(this);
    }

    public void bindVertexBuffer(){
        vertexBufferBinder.bind(this);
    }


    public abstract Shader getShader();


    public abstract Texture[] getTextures();

    public abstract void setMaterial(Material material);

    public abstract Material getMaterial();

    public abstract void setMaterial(Material[] material);

    public abstract void adjustContext();

    public abstract void initContext();

}
