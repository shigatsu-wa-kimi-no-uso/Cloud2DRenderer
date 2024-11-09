#version 300 es
precision mediump float;
precision highp int;
in vec2 vTexCoords;
out vec4 fragmentColor;

uniform sampler2D uFlipBookTexture;

struct SeqFrameParams {
    int currFrameIndex; // 从0开始
    float frequency;
    vec2 flipBookShape;
};

uniform SeqFrameParams uSeqFrameParams;
in float debug_val;

void main()
{
   // vec2  val = uSeqFrameParams.flipBookShape/vec2(16.0,16.0);
   // float v = debug_val;
    //vec4 color = vec4( v,v,v,1);
    vec4 color = texture(uFlipBookTexture,vTexCoords);
    fragmentColor = color;
}