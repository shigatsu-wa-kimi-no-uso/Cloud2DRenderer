package me.project.cloud2drenderer.renderer.entity.model;

import me.project.cloud2drenderer.opengl.glresource.buffer.GLVertexBuffer;

public class LoadedModel {
    public GLVertexBuffer vertexBuffer;

    public String name;

    public int vertexCount;

    public int drawnVertexCount;

    public boolean elemBased;


    public Class<? extends MeshModel> modelClass;

    @Deprecated
    public ModelMeta.ModelMetaGetter modelMetaGetter;


}
