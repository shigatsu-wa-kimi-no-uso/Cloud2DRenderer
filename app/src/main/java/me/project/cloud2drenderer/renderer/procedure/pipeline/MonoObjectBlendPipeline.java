package me.project.cloud2drenderer.renderer.procedure.pipeline;

import me.project.cloud2drenderer.renderer.context.RenderContext;

public class MonoObjectBlendPipeline extends BlendPipeline{

    //mono object 多个单一物体渲染，具有相同的vertex buffer和shader，
    // 但具有不同的texture和shader uniform变量

    private void bindPipelineGlobalResources(RenderContext context){
        context.bindShader();
        context.bindVertexBuffer();
        context.bindShaderAttributePointers();
    }

    private void bindContextLocalResources(RenderContext context){
        context.bindTexture();
    }

    @Override
    public void beforeTask() {
        super.beforeTask();
        bindPipelineGlobalResources(sortedContexts.get(0));
    }

    @Override
    public void run(){
        for(RenderContext context : sortedContexts){
            bindContextLocalResources(context);
            context.applyUniformAssignments();
            context.draw();
        }
    }

}
