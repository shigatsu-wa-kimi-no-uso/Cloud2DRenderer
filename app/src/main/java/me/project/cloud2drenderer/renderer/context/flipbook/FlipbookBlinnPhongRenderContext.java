package me.project.cloud2drenderer.renderer.context.flipbook;

import me.project.cloud2drenderer.renderer.context.BlinnPhongRenderContext;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;


public class FlipbookBlinnPhongRenderContext extends BlinnPhongRenderContext {

    private SequenceFrameParams seqFrameParams;

    public void setSeqFrameParams(SequenceFrameParams seqFrameParams) {
        this.seqFrameParams = seqFrameParams;
    }

    @ShaderUniform(uniformName = "uSeqFrameParams",flags = {UniformFlag.IS_STRUCT})
    public SequenceFrameParams getSeqFrameParams() {
        return seqFrameParams;
    }


    @Override
    public void adjustContext() {
        seqFrameParams.increaseCurrentFrameIndex();
    }
}
