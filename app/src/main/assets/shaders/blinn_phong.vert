#version 300 es
precision mediump float;

layout (location = 4) in vec3 aPosition;  // object space
layout (location = 5) in vec3 aNormal;
layout (location = 6) in vec2 aTexCoords;

out vec2 vTexCoords;
out vec3 vPosition; // world space
out vec3 vNormal;


uniform mat4 uModeling;
uniform mat4 uModelIT;
uniform mat4 uView;
uniform mat4 uProjection;


void main()
{
    vTexCoords = aTexCoords;
    vPosition = vec3(uModeling * vec4(aPosition, 1.0));
    vNormal = mat3(uModelIT) * aNormal;
    gl_Position = uProjection * uView * vec4(vPosition, 1.0);
}