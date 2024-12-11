#version 300 es
precision mediump float;
precision highp int;

struct SeqFrameParams {
    int currFrameIndex; // 从0开始
    float frequency;
    vec2 flipBookShape;
};

in vec3 aPosition;  // object space
in vec2 aTexCoords;
in vec3 aNormal;
in vec3 aTangent;
in vec3 aBitangent;


out vec2 vTexCoords;
out vec3 vPosition; // world space
out vec3 vNormal;
out mat3 vTBNInversed; //world to tangent
out mat3 vTBN;   // tangent to world


uniform mat4 uModeling;
uniform mat4 uModelIT;
uniform mat4 uView;
uniform mat4 uProjection;
uniform SeqFrameParams uSeqFrameParams;


vec2 getTexCoords(vec2 originalTexCoords, vec2 flipBookShape, float currIndex){
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
    mat3 modelIT = mat3(uModelIT);
    vec3 T = normalize(modelIT * aTangent);
    vec3 B = normalize(modelIT * aBitangent);
    vec3 N = normalize(modelIT * aNormal);
    vTBN = mat3(T, B, N);
    vTBNInversed = transpose(mat3(T, B, N));
    vTexCoords = getTexCoords(aTexCoords, uSeqFrameParams.flipBookShape, currIndex);
    vPosition = vec3(uModeling * vec4(aPosition, 1.0));
    vNormal = mat3(uModelIT) * aNormal;
    gl_Position = uProjection * uView * vec4(vPosition, 1.0);
}