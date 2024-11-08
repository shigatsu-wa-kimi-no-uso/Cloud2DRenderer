#version 300 es
precision mediump float;

in vec2 vTexCoords;
out vec4 fragmentColor;

uniform float uRatio;
uniform sampler2D texture1;
uniform sampler2D texture2;

void main()
{
    fragmentColor = vec4(1.0,1.0,1.0,1.0);
   // fragmentColor = mix(texture(texture1, vTexCoords),texture(texture2, vTexCoords),uRatio);
}