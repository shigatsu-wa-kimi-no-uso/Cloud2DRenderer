#version 300 es
precision mediump float;
precision highp int;
in vec3 aPosition;
in vec2 aTexCoords;

out vec2 vTexCoords;

uniform mat4 uModeling;
uniform mat4 uView;
uniform mat4 uProjection;

struct SeqFrameParams {
    int currFrameIndex; // 从0开始
    float frequency;
    vec2 flipBookShape;
};

uniform SeqFrameParams uSeqFrameParams;
out float debug_val;

vec2 getTexCoords(vec2 originalTexCoords,vec2 flipBookShape,float currIndex){
    vec2 stridePerTile = vec2(1.0,1.0)/flipBookShape;
    vec2 relativeCoords = originalTexCoords*stridePerTile;
    float tileIndex = mod(currIndex, (flipBookShape.x*flipBookShape.y));
    float currTileV = floor(tileIndex / flipBookShape.x);
    float currTileH = mod(tileIndex,flipBookShape.x);
    vec2 currTilePos = vec2(currTileH,currTileV);
    vec2 currTileCoordTopLeft = currTilePos*stridePerTile;
  //  debug_val = currTileCoordTopLeft.x;
    vec2 texCoord = currTileCoordTopLeft  + relativeCoords;
  //  texCoord = relativeCoords + stridePerTile*vec2(7.0,0.0);
    return texCoord;
}


void main()
{
    float currIndex = floor(float(uSeqFrameParams.currFrameIndex) * uSeqFrameParams.frequency);
    vTexCoords = getTexCoords(aTexCoords,uSeqFrameParams.flipBookShape,currIndex);
    gl_Position = uProjection * uView * uModeling * vec4(aPosition, 1.0);
}