#version 300 es
precision mediump float;
precision highp int;

in vec2 vTexCoords;
out vec4 fragmentColor;

uniform sampler2D uTexture;

void main()
{
    vec4 texColor = texture(uTexture, vTexCoords);
    fragmentColor = texColor;// vec4(1.0,0.0,0.0,1.0);
}