#version 310 es
#extension GL_EXT_shader_io_blocks : enable
precision mediump float;
precision highp int;

in vec3 aPosition;
in vec2 aTexCoords;
in vec3 aNormal;
in vec3 aTangent;
in vec3 aBitangent;

out Varying{
    vec2 texCoords;
    vec3 position;
    mat3 TBNInversed;
    mat3 TBN;
}vs_out;


uniform mat4 uModeling;
uniform mat4 uModelIT;
uniform mat4 uView;
uniform mat4 uProjection;

struct SeqFrameParams {
    int currFrameIndex; // 从0开始
    float frequency;
    vec2 flipBookShape;
};

uniform SeqFrameParams uSeqFrameParams;


vec2 getTexCoords(vec2 originalTexCoords,vec2 flipBookShape,float currIndex){
    vec2 stridePerTile = vec2(1.0,1.0)/flipBookShape;
    vec2 relativeCoords = originalTexCoords*stridePerTile;
    float tileIndex = mod(currIndex, (flipBookShape.x*flipBookShape.y));
    float currTileV = flipBookShape.y - floor(tileIndex / flipBookShape.y) - 1.0;
    float currTileH = mod(tileIndex, flipBookShape.x);
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
    vs_out.texCoords = getTexCoords(aTexCoords,uSeqFrameParams.flipBookShape,currIndex);
    vs_out.position = vec3(uModeling * vec4(aPosition,1.0));
    mat3 modelIT = mat3(uModelIT);
    vec3 T = normalize(modelIT * aTangent);
    vec3 B = normalize(modelIT * aBitangent);
    vec3 N = normalize(modelIT * aNormal);
    vs_out.TBN = mat3(T, B, N);
    vs_out.TBNInversed = transpose(mat3(T, B, N));

    gl_Position = uProjection * uView * vec4(vs_out.position, 1.0);
}