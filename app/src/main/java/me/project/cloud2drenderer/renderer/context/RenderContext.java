package me.project.cloud2drenderer.renderer.context;

import androidx.annotation.NonNull;

import java.util.Vector;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.ShaderVariableSetterWrapper;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.ResBindingMethod;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.drawing.DrawMethod;
import me.project.cloud2drenderer.renderer.scene.Camera;



public abstract class RenderContext {


    public String name;

    public static Camera camera;

    public int contextId;

    public LoadedModel loadedModel;

    private DrawMethod drawMethod;

    private ResBindingMethod resourseBinder;

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



    public abstract void setTransform(float[] transform);

    public void setDrawMethod(DrawMethod drawMethod) {
       this.drawMethod=drawMethod;
    }

    public void setGLResourceBinder(ResBindingMethod bindingMethod){
        this.resourseBinder = bindingMethod;
    }

    public <T> void commitUniformAssignment(@NonNull UniformVar<T> uniformVar){
        uniformAssignments.add(uniformVar.getUniformSetterWrapper());
    }

    public void addAutoAssignedUniforms(ShaderVariableSetterWrapper setterWrapper){
        autoAssignedUniforms.add(setterWrapper);
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
       resourseBinder.bind(this);
    }

    public abstract Shader getShader();


    public abstract Texture[] getTextures();

    public abstract void setMaterial(Material material);

    public abstract Material getMaterial();

    public abstract void setMaterial(Material[] material);

    public abstract void adjustContext();

    public abstract void initContext();

}
