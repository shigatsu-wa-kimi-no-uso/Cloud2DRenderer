#version 300 es
precision mediump float;

in vec3 aPosition;
in vec2 aTexCoords;

out vec2 vTexCoords;
out vec3 pos;

uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;

void main()
{
    vTexCoords = aTexCoords;
  //  gl_Position = vec4(aPosition,1.0);
    gl_Position = uProjection * uView * uModeling * vec4(aPosition, 1.0);
}