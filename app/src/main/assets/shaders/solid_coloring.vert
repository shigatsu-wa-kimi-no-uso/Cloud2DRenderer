#version 300 es
precision mediump float;

in vec3 aPosition;
in vec4 aColor;

out vec4 vColor;

uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;

void main()
{
    vColor = aColor;
    gl_Position = uProjection * uView * uModeling * vec4(aPosition, 1.0);
}