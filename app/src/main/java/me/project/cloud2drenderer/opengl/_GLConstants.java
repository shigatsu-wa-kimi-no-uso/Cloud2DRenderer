package me.project.cloud2drenderer.opengl;


import static android.opengl.GLES32.*;


public class _GLConstants  {
    public static final int INTERLEAVED_ATTRIBS = GL_INTERLEAVED_ATTRIBS;
    public static final int SEPARATE_ATTRIBS = GL_SEPARATE_ATTRIBS;

    public static final int NULL_INDEX = GL_NONE;

    public static final int NONE = GL_NONE;
    public static final int NO_ERROR = GL_NO_ERROR;

    public static final int FALSE = GL_FALSE;
    public static final int TRUE = GL_TRUE;

    public static final int INVALID_INDEX = GL_INVALID_INDEX;

    public static class VertexBuffer {
        private VertexBuffer(){}
        public static final int STATIC_DRAW = GL_STATIC_DRAW;
        public static final int DYNAMIC_DRAW = GL_DYNAMIC_DRAW;

        public static final int ARRAY_BUFFER = GL_ARRAY_BUFFER;
        public static final int ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

    }

    public static class Shader {
        private Shader(){}
        public static final int VERTEX_SHADER = GL_VERTEX_SHADER;
        public static final int FRAGMENT_SHADER = GL_FRAGMENT_SHADER;

        public static final int COMPILE_STATUS = GL_COMPILE_STATUS;
        public static final int LINK_STATUS = GL_LINK_STATUS;
    }

    public static class Type {
        private Type(){}
        public static final int BYTE = GL_BYTE;
        public static final int BOOL = GL_BOOL;
        public static final int UNSIGNED_BYTE = GL_UNSIGNED_BYTE;
        public static final int SHORT = GL_SHORT;
        public static final int UNSIGNED_SHORT = GL_UNSIGNED_SHORT;
        public static final int FLOAT = GL_FLOAT;
        public static final int INT = GL_INT;
        public static final int UNSIGNED_INT = GL_UNSIGNED_INT;

        public static final int FIXED = GL_FIXED;

        public static final int HIGH_INT = GL_HIGH_INT;
        public static final int HIGH_FLOAT = GL_HIGH_FLOAT;
    }

    public static class Texture {

        private Texture(){}
        public static final int TEXTURE_2D = GL_TEXTURE_2D;

        public static final int TEXTURE_CUBE_MAP = GL_TEXTURE_CUBE_MAP;

        public static final int TEXTURE_WRAP_S = GL_TEXTURE_WRAP_S;
        public static final int TEXTURE_WRAP_T = GL_TEXTURE_WRAP_T;
        public static final int TEXTURE_CUBE_MAP_POSITIVE_X = GL_TEXTURE_CUBE_MAP_POSITIVE_X;

        public static final int TEXTURE_WRAP_R = GL_TEXTURE_WRAP_R;

        public static final int CLAMP_TO_EDGE = GL_CLAMP_TO_EDGE;

        public static final int TEXTURE_MAG_FILTER = GL_TEXTURE_MAG_FILTER;
        public static final int TEXTURE_MIN_FILTER = GL_TEXTURE_MIN_FILTER;
        public static final int NEAREST = GL_NEAREST;

        public static final int LINEAR = GL_LINEAR;

        public static final int TEXTURE0 = GL_TEXTURE0;
    }


    public static class Canvas {
        private Canvas(){}
        public static final int DEPTH_TEST = GL_DEPTH_TEST;
        public static final int COLOR_BUFFER_BIT = GL_COLOR_BUFFER_BIT;

        public static final int DEPTH_BUFFER_BIT = GL_DEPTH_BUFFER_BIT;

        public static final int STENCIL_BUFFER_BIT = GL_STENCIL_BUFFER_BIT;

        public static final int TRIANGLES = GL_TRIANGLES;
    }

}
