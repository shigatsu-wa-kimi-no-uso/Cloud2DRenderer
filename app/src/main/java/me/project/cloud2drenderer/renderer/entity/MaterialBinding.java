package me.project.cloud2drenderer.renderer.entity;

import me.project.cloud2drenderer.renderer.entity.material.Material;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.material.TextureSetter;

public class MaterialBinding {
    public String shaderName;

    public String[] textureNames;

    public TextureSetter[] textureSetters;

    public Material material;

    public boolean deferredTextureLoading;

}
