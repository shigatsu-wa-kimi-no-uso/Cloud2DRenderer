package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader;

public class ShaderUniformMeta {

    public String uniformName;

    public int[] location;  //若该类型是个array,则对每个元素都会有一个location, 一般location是连续的, 但没有文档明确保证了这一点

    public int type;

    public int elemCnt;

    public boolean intBased;

}
