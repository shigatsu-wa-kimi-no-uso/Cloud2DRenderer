#version 300 es
precision mediump float;
precision highp int;

struct PointLight {
    vec3 intensity;
    vec3 position;
};

struct SixWayLightingMap{
    sampler2D mapRLT; //right left top
    sampler2D mapBBF; //bottom back front
};

struct SeqFrameParams {
    int currFrameIndex; // 从0开始
    float frequency;
    vec2 flipBookShape;
};


in vec2 vTexCoords;
in vec3 vPosition;
in mat3 vTBNInversed;
in mat3 vTBN;



out vec4 fragmentColor;


uniform PointLight uPointLight;
uniform sampler2D uFlipBookAlbedo;
uniform SixWayLightingMap uFlipBookLightMap;
uniform SeqFrameParams uSeqFrameParams;


float getIntensityAttenuation(vec3 lightPos,vec3 litPoint){
    float dis = distance(lightPos,litPoint);
    float disSqrInv = 1.0/(dis*dis);
    return 1.0;
}

vec3 computeSixWayLighting(vec3 sampledRTF, vec3 sampledLBB, vec3 lightDir, vec3 lightColor) {
    vec3 clampedDir = clamp(lightDir, vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0));
    vec3 clampedNegDir = clamp(-lightDir, vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0));
    vec3 factor = sampledRTF * clampedDir + sampledLBB * clampedNegDir;
    return lightColor*(factor.x+factor.y+factor.z);
}


void main()
{
    vec3 lightMapRLT = texture(uFlipBookLightMap.mapRLT, vTexCoords).rgb;
    vec3 lightMapBBF = texture(uFlipBookLightMap.mapBBF, vTexCoords).rgb;
    vec3 lightingRTF = vec3(lightMapRLT.rb, lightMapBBF.b); //right top front
    vec3 lightingLBB = vec3(lightMapRLT.g, lightMapBBF.rg); //left bottom back
    float attenuation = getIntensityAttenuation(uPointLight.position, vPosition);
    vec3 lightDir = normalize(uPointLight.position - vPosition);
    vec3 lightDirTS = normalize(vTBNInversed * lightDir);

    //mat3 TBN = transpose(TBNInversed);
   // fragmentColor = vec4(lightDirTS*0.5+0.5,1.0);
    vec3 lighting = attenuation * computeSixWayLighting(lightingRTF,lightingLBB,lightDirTS,uPointLight.intensity);
    vec4 albedo = texture(uFlipBookAlbedo, vTexCoords);
   // fragmentColor = vec4( TBN[2]*0.5+0.5,albedo.a);
    fragmentColor = vec4(albedo.rgb * lighting,albedo.a);
}