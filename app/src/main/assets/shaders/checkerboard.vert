#version 300 es
precision mediump float;

in vec3 aPosition;
in vec3 aNormal;


out vec3 vPosition; // world space
out vec3 vNormal;

uniform mat4 uModeling;
uniform mat4 uModelIT;
uniform mat4 uView;
uniform mat4 uProjection;


void main()
{
    vPosition = vec3(uModeling * vec4(aPosition, 1.0));
    vNormal = mat3(uModelIT) * aNormal;
    //vs_out.normalOS = aNormal;
    gl_Position = uProjection * uView * vec4(vPosition,1.0);
}