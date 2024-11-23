#version 300 es
precision mediump float;

in vec3 aPosition;

out vec3 vPosition;
uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;


void main()
{
    vPosition = vec3(uModeling * vec4(aPosition, 1.0));
    gl_Position = uProjection * uView * vec4(vPosition, 1.0);
}