#version 300 es
precision mediump float;

in vec4 vPositionColor;

out vec4 fragmentColor;


void main()
{
    fragmentColor = vPositionColor;
}