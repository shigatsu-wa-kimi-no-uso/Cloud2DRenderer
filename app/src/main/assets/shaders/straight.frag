#version 300 es
precision mediump float;
precision highp int;
in vec2 vTexCoords;

out vec4 fragmentColor;

uniform sampler2D texture1;
uniform vec2 uColorVec;
uniform int uColor;

void main()
{
   // vec4 texColor = texture(texture1, vTexCoords);
    fragmentColor = vec4(uColorVec,uColor,1);
}