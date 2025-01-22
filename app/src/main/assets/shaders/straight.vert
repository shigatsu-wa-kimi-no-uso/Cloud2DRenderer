#version 300 es
precision mediump float;
precision highp int;

in vec3 aPosition;
in vec2 aTexCoords;


out vec2 vTexCoords;

uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;

void main()
{
    vTexCoords = aTexCoords;
    gl_Position = vec4(uProjection*uView*uModeling*vec4(aPosition, 1.0));
}