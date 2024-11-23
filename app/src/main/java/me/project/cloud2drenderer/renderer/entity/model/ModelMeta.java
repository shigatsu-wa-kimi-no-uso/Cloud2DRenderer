package me.project.cloud2drenderer.renderer.entity.model;

import java.util.Map;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.VertexAttributeMeta;

public class ModelMeta {

    public int vertexCount;

    public Map<String, VertexAttributeMeta> attributeMetas;

    public interface ModelMetaGetter{
        ModelMeta getModelMeta();
    }

}
