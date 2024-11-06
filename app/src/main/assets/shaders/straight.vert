#version 300 es
precision mediump float;
precision highp int;

in vec3 aPosition;
in vec2 aTexCoords;

out vec2 vTexCoords;

uniform vec2 uColorVec;

void main()
{

  //  vTexCoords = aTexCoords;
    gl_Position = vec4(aPosition, 1.0);
}