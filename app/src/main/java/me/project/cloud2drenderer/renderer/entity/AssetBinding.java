package me.project.cloud2drenderer.renderer.entity;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.procedure.pipeline.RenderPipeline;

public class AssetBinding {

    public String modelName;

    public MaterialBinding materialBinding;


    public RenderContext context;

    public String pipelineName;

    public AssetBinding(){
    }

}
