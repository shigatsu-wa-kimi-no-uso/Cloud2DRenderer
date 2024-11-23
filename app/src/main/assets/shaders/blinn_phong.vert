#version 310 es
#extension GL_EXT_shader_io_blocks : enable
precision mediump float;

in vec3 aPosition;  // object space
in vec2 aTexCoords;
in vec3 aNormal;

out Varying{
    vec2 texCoords;
    vec3 position; // world space
    vec3 normal;
}vs_out;


uniform mat4 uModeling;
uniform mat4 uModelIT;
uniform mat4 uView;
uniform mat4 uProjection;


void main()
{
    vs_out.texCoords = aTexCoords;
    vs_out.position = vec3(uModeling * vec4(aPosition, 1.0));
    vs_out.normal = mat3(uModelIT) * aNormal;
    gl_Position = uProjection * uView * vec4(vs_out.position, 1.0);
}