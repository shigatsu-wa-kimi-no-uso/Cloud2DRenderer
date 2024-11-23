package me.project.cloud2drenderer.renderer.context;

import me.project.cloud2drenderer.renderer.entity.material.CheckerBoard;
import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;

public class CheckerBoardRenderContext extends BlinnPhongRenderContext{

 //    private CheckerBoard material;
//
//    private float[] transform;
//
//
//
//    @Override
//    @ShaderUniform(uniformName = "uModeling")
//    public float[] getTransform() {
//        return transform;
//    }
//
//    @Override
//    public void setTransform(float[] transform) {
//        this.transform = transform;
//    }
//
//    @Override
//    public Shader getShader() {
//        return material.getShader();
//    }
//
//    @Override
//    public Texture[] getTextures() {
//        return material.getTextures();
//    }


//    @ShaderUniform(uniformName = "uView")
//    public float[] getView(){
//        return camera.getView();
//    }
//
//    @ShaderUniform(uniformName = "uProjection")
//    public float[] getProjection(){
//        return camera.getProjection();
//    }


    @Override
    public void setMaterial(Material material) {
        this.material = (CheckerBoard) material;
    }


    @ShaderUniform(uniformName = "colors")
    public float[] getColors(){
        return ((CheckerBoard) material).getColors();
    }

    @Override
    public void adjustContext() {

    }

    @Override
    public void initContext() {

    }
}
