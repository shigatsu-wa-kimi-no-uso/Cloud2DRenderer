package me.project.cloud2drenderer.renderer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.project.cloud2drenderer.opengl.glresource.buffer.GLVertexBuffer;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.util.DebugUtils;

public class ModelController {

    private final Map<String, LoadedModel> loadedModels;

    public ModelController(){
        loadedModels = new HashMap<>();
    }

    public LoadedModel loadModel(MeshModel model, String name){
        if(model.getVertexIndices() == null){
            return loadModel(model,name,false);
        }else {
            return loadModel(model,name,true);
        }
    }

    public void put(String name,LoadedModel loadedModel){
        loadedModels.put(name,loadedModel);
    }


    public LoadedModel loadModel(MeshModel model, String name, boolean elemBased) {
        return loadedModels.compute(name, (k, v) ->
                Objects.requireNonNullElseGet(v, () -> {
                            float[] vertices = model.getVertexData();
                            int[] indices = model.getVertexIndices();
                            GLVertexBuffer vertexBuffer = GLVertexBufferManager.genVertexBuffer(elemBased);
                            GLVertexBufferManager.bind(vertexBuffer);
                            GLVertexBufferManager.loadVertexAttributeData(vertices, model.getDrawMethod());
                            LoadedModel loadedModel = new LoadedModel();
                            if(elemBased){
                                GLVertexBufferManager.loadElementIndices(indices,model.getDrawMethod());
                                loadedModel.drawnVertexCount = indices.length;
                            }else {
                                loadedModel.drawnVertexCount = vertices.length;
                            }
                            loadedModel.elemBased = elemBased;
                            loadedModel.vertexBuffer = vertexBuffer;
                            loadedModel.vertexCount = model.getVertexCount();

                            loadedModel.modelMetaGetter = model::getModelMeta;
                            GLVertexBufferManager.unbind();
                            return loadedModel;
                        }
                )
        );
    }

    public void bindVertexBuffer(GLVertexBuffer vertexBuffer){
        GLVertexBufferManager.bind(vertexBuffer);
    }

    public LoadedModel getLoadedModel(String name){
        return DebugUtils.checkNotNull(loadedModels.get(name));
    }


}
