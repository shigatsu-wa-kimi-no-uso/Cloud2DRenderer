#version 300 es
precision mediump float;

in vec3 aPosition;

out vec4 vPositionColor;

uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;

void main()
{
    vPositionColor = vec4(aPosition,1.0);
    gl_Position = uProjection * uView * uModeling * vec4(aPosition, 1.0);
}