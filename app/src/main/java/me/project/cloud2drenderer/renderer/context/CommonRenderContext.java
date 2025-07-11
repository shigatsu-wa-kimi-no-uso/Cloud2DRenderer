package me.project.cloud2drenderer.renderer.context;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformVar;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;


public class CommonRenderContext extends RenderContext{

    private Material material;

    @ShaderUniform(uniformName = "uColor")
    public UniformVar<int[]> color = null;

    @ShaderUniform(uniformName = "uColorVec")
    public UniformVar<float[]> colorVec = null;

    @ShaderUniform(uniformName = "uRatio")
    public UniformVar<float[]> ratio;

    private float[] transform;


    @Override
    @ShaderUniform(uniformName = "uModeling")
    public float[] getTransform(){
        return transform;
    }

    @Override
    public void setTransform(float[] transform) {
        this.transform = transform;
    }

    public CommonRenderContext(){
        ratio = new UniformVar<>();
    }


    @Override
    public Shader getShader() {
        return material.getShader();
    }


    @Override
    public void adjustContext() {
    //    commitUniformAssignment(ratio);
      //  colorVec.setValue(new float[]{(float) Math.random(),(float) Math.random()});
     //   commitUniformAssignment(colorVec);
       // counter++;
     //   color.setOffset(counter%2);
        // commitUniformAssignment(colorVec);
       // commitUniformAssignment(color);
  //      camera.setPosition(new float[]{(float) Math.random(),(float) Math.random(),(float) Math.random()});
    //    camera.update();
    }

    @Override
    public void initContext() {
       // colorVec.setValue(new float[]{(float) Math.random(),(float) Math.random()});
    //    color.setValue(new int[]{0,1});
     //   commitUniformAssignment(ratio);
       // commitUniformAssignment(colorVec);
//        commitUniformAssignment(color);
    }

    @Override
    public Texture[] getTextures(){
        return material.getTextures();
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material[] material) {

    }


    @ShaderUniform(uniformName = "uView")
    public float[] getView(){
        return camera.getView();
    }

    @ShaderUniform(uniformName = "uProjection")
    public float[] getProjection(){
        return camera.getProjection();
    }

    @ShaderUniform(uniformName = "texture1")
    public int getTexture1Unit(){
        return material.getTextures()[0].unit;
    }

    @ShaderUniform(uniformName = "texture2")
    public int getTexture2Unit(){
        return material.getTextures()[1].unit;
    }

}
