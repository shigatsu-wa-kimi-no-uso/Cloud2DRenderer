package me.project.cloud2drenderer.renderer.context.flipbook;

import android.opengl.Matrix;

import me.project.cloud2drenderer.renderer.context.BlinnPhongRenderContext;
import me.project.cloud2drenderer.renderer.entity.model.shape.Rectangle;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.UniformFlag;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.ShaderUniform;
import me.project.cloud2drenderer.util.MatUtils;


public class FlipbookBlinnPhongRenderContext extends BlinnPhongRenderContext {

    private SequenceFrameParams seqFrameParams;

    private float[] position;

    private float[] scale;


    public void setSeqFrameParams(SequenceFrameParams seqFrameParams) {
        this.seqFrameParams = seqFrameParams;
    }

    @ShaderUniform(uniformName = "uSeqFrameParams",flags = {UniformFlag.IS_STRUCT})
    public SequenceFrameParams getSeqFrameParams() {
        return seqFrameParams;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }

    @Override
    public void adjustContext() {
        seqFrameParams.increaseCurrentFrameIndex();
        float[] eyePos = camera.getPosition();
        float[] eyePosOS = MatUtils.sub(eyePos,position);
        float[] transform = Rectangle.getBillboardTransform(eyePosOS,position);
        Matrix.scaleM(transform,0,scale[0],scale[1],scale[2]);
        setTransform(transform);
    }
}
