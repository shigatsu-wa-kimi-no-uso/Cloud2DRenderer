package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader;

public enum UniformFlag {
    AUTO_ASSIGN(0x1),
    AUTO_INITIALIZE(0x2),
    USING_RAW(0x4),
    IS_STRUCT(0x8);

    final int mask;

    UniformFlag(int mask){
        this.mask = mask;
    }
}
