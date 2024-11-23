#version 300 es
precision mediump float;

in vec2 vTexCoords;
out vec4 fragmentColor;

uniform sampler2D albedo;

void main()
{
   // fragmentColor = vec4(d_val,d_val,d_val,1.0);
    fragmentColor = texture(albedo, vTexCoords);
}