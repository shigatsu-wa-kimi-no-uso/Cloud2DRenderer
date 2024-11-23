#version 300 es
precision mediump float;

in vec3 aPosition;
in vec2 aTexCoords;

out vec2 vTexCoords;

uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;
uniform sampler2D heightMap;

void main()
{
    vTexCoords = aTexCoords;
    vec3 newPos = aPosition;
    newPos.y = texture(heightMap,aTexCoords).x;
    gl_Position = uProjection * uView * uModeling * vec4(newPos, 1.0);
}