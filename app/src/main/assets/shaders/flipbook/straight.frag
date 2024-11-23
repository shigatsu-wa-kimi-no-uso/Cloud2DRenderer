#version 300 es
precision mediump float;
precision highp int;

struct SeqFrameParams {
    int currFrameIndex; // 从0开始
    float frequency;
    vec2 flipBookShape;
};

in vec2 vTexCoords;

out vec4 fragmentColor;

uniform sampler2D uFlipBookTexture;
uniform SeqFrameParams uSeqFrameParams;


void main()
{
    vec4 color = texture(uFlipBookTexture,vTexCoords);
    fragmentColor = color;
}