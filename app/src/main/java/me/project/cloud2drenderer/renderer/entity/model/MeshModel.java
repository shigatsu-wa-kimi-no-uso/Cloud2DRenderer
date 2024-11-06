package me.project.cloud2drenderer.renderer.entity.model;


import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.AttributeBindingProcessor;

public abstract class MeshModel {

    protected float[] vertexData;

    protected int[] vertexIndices;

    protected int vertexCount;

    protected int drawMethod;


    public void setVertexData(float[] vertexData) {
        this.vertexData = vertexData;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public void setDrawMethod(int drawMethod) {
        this.drawMethod = drawMethod;
    }

    public float[] getVertexData(){
        return vertexData;
    }

    public int getVertexCount(){
        return vertexCount;
    }

    public int getDrawMethod(){
        return drawMethod;
    }

    public int[] getVertexIndices() {
        return vertexIndices;
    }

    public void setVertexIndices(int[] vertexIndices) {
        this.vertexIndices = vertexIndices;
    }

    public ModelMeta getModelMeta() {
        return AttributeBindingProcessor.getModelMeta(this);
    }

/*
    class VertexAttributeMeta{
        public int strideInByte;

        public int count;

        public class SingleMeta {
            public String attributeName;
            public int elemCnt;
            public int elemType;
            public boolean normalized;
            public int offset;
        }

        public Map<String, SingleMeta> attributeMetas;
    }
*/

}
