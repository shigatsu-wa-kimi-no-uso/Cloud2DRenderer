#version 300 es
precision mediump float;

in vec2 vTexCoords;
in vec3 pos;
out vec4 fragmentColor;


void main()
{
   // vec4 texColor = texture(texture1, vTexCoords);
    fragmentColor = vec4(vTexCoords.yyy,1.0);
}