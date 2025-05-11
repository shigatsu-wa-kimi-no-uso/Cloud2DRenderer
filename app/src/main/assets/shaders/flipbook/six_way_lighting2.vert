#version 300 es
precision mediump float;
precision mediump int;

struct SeqFrameParams {
    float currFrameIndex; // 从0开始
    ivec2 flipBookShape;
};


in vec3 aPosition;
in vec2 aTexCoords;
in vec3 aNormal;
in vec3 aTangent;
in vec3 aBitangent;

out float vRatio;
out vec2 vNextTexCoords;
out vec2 vTexCoords;
out vec3 vPosition;
out mat3 vTBNInversed;
out mat3 vTBN;

out float debug_val1;
out float debug_val2;
uniform vec3 uCloudAlbedo;
uniform mat4 uModeling;
uniform mat4 uModelIT;
uniform mat4 uView;
uniform mat4 uProjection;
uniform SeqFrameParams uSeqFrameParams;


vec2 getTexCoords_old(vec2 originalTexCoords,vec2 flipBookShape,float currIndex){
    vec2 stridePerTile = vec2(1.0,1.0)/flipBookShape;
    vec2 relativeCoords = originalTexCoords*stridePerTile;
    float tileIndex = mod(currIndex, (flipBookShape.x*flipBookShape.y));
    float currTileV = flipBookShape.y - floor(tileIndex / flipBookShape.x) - 1.0;
    float currTileH = mod(tileIndex, flipBookShape.x);

    vec2 currTilePos = vec2(currTileH,currTileV);
    vec2 currTileCoordBottomLeft = currTilePos*stridePerTile;
    debug_val1 = currTileCoordBottomLeft.x;
    debug_val2 =  currTileCoordBottomLeft.y;
    vec2 texCoord = currTileCoordBottomLeft + relativeCoords;
    //  texCoord = relativeCoords + stridePerTile*vec2(7.0,0.0);
    return texCoord;
}

vec2 getTexCoords(vec2 originalTexCoords,ivec2 flipBookShape,int currIndex){
    vec2 stridePerTile = vec2(1.0,1.0)/vec2(flipBookShape);
    vec2 relativeCoords = originalTexCoords*stridePerTile;
    int tileIndex = currIndex % (flipBookShape.x*flipBookShape.y);
    int currTileV = flipBookShape.y - tileIndex / flipBookShape.x - 1;
    int currTileH = tileIndex%flipBookShape.x;
    vec2 currTilePos = vec2(currTileH,currTileV);
    vec2 currTileCoordBottomLeft = currTilePos*stridePerTile;
    //debug_val1 = currTileCoordBottomLeft.x;
    //debug_val2 =  currTileCoordBottomLeft.y;
    vec2 texCoord = currTileCoordBottomLeft + relativeCoords;
    //  texCoord = relativeCoords + stridePerTile*vec2(7.0,0.0);
    return texCoord;
}

void main()
{
    int currIndex = int(floor(uSeqFrameParams.currFrameIndex));
    int nextIndex = int(ceil(uSeqFrameParams.currFrameIndex));
    vRatio = fract(uSeqFrameParams.currFrameIndex);
    vTexCoords = getTexCoords(aTexCoords, uSeqFrameParams.flipBookShape, currIndex);
    vNextTexCoords = getTexCoords(aTexCoords, uSeqFrameParams.flipBookShape, nextIndex);
    vPosition = vec3(uModeling * vec4(aPosition,1.0));
    mat3 modelIT = mat3(uModelIT);
    mat3 modeling = mat3(uModeling);
    vec3 T = normalize(modeling * aTangent);
    vec3 B = normalize(modeling * aBitangent);
    vec3 N = normalize(modelIT * aNormal);
    vTBN = mat3(T, B, N);
    vTBNInversed = transpose(mat3(T, B, N));

    gl_Position = uProjection * uView * vec4(vPosition, 1.0);
}