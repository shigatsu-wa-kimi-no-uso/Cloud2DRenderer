package me.project.cloud2drenderer.renderer.entity.material;

import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;

public class BlinnPhong extends Material{
    private float[] ka;

    private float[] kd;

    private float[] ks;

    private float shininess;
    public BlinnPhong(){
        textures = new Texture[2];
    }


    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public void setAlbedo(Texture texture){
        textures[0] = texture;
    }

    public void setKa(float[] ka) {
        this.ka = ka;
    }
    public void setKd(float[] kd){
        this.kd = kd;
    }

    public void setKs(float[] ks) {
        this.ks = ks;
    }

    public void setAmbientFactor(float[] factor) {
        setKa(factor);
    }

    public void setDiffuseMap(Texture map){
         textures[0] = map;
    }

    public void setSpecularMap(Texture map) {
         textures[1] = map;
    }
    @ShaderUniform(uniformName = "shininess")
    public float getShininess() {
        return shininess;
    }

    @ShaderUniform(uniformName = "ka")
    public float[] getKa() {
        return ka;
    }

    @ShaderUniform(uniformName = "kd")
    public float[] getKd(){
        return kd;
    }

    @ShaderUniform(uniformName = "ks")
    public float[] getKs(){
        return ks;
    }


    @ShaderUniform(uniformName = "diffuseMap")
    public int getDiffuseMapUnit(){
        return textures[0].unit;
    }

    @ShaderUniform(uniformName = "specularMap")
    public int getSpecularMapUnit(){
        return textures[1].unit;
    }

}
