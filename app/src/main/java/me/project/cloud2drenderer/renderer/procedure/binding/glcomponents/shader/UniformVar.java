package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader;

public class UniformVar<T> {
    private T value;
    private String name;

    private int count;

    private int offset;
    private boolean transpose;

    private int index;

    private ShaderUniformSetterWrapper uniformSetterWrapper;

    public ShaderUniformMeta meta;


    public UniformVar(){

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int getLocation(){
        return meta.location[index];
    }

    public ShaderUniformSetterWrapper getUniformSetterWrapper() {
        return uniformSetterWrapper;
    }

    public void setUniformSetterWrapper(ShaderUniformSetterWrapper uniformSetterWrapper) {
        this.uniformSetterWrapper = uniformSetterWrapper;
    }

    public ShaderUniformMeta getMeta() {
        return meta;
    }

    public void setMeta(ShaderUniformMeta meta) {
        this.meta = meta;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isTranspose() {
        return transpose;
    }

    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }
}
