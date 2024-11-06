#version 300 es
precision mediump float;

in vec2 vTexCoords;
out vec4 fragmentColor;

uniform sampler2D texture1;

void main()
{
    vec4 texColor = texture(texture1, vTexCoords);
    fragmentColor = texColor;
}