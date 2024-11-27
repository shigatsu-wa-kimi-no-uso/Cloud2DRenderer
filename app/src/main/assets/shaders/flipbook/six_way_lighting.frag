#version 300 es
precision mediump float;
precision highp int;

struct Light{
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


uniform Light uPointLight;
uniform sampler2D uFlipBookAlbedo;
uniform SixWayLightingMap uFlipBookLightingMap;
uniform SeqFrameParams uSeqFrameParams;


float getIntensityAttenuation(vec3 lightPos,vec3 litPoint){
    float dis = distance(lightPos,litPoint);
    float disSqrInv = 1.0/(dis*dis);
    return 1.0;
}

vec3 computeSixWayLighting(vec3 RTF,vec3 LBB,vec3 lightDir,vec3 lightColor) {
    vec3 normalizedLightDir = normalize(lightDir);
    vec3 clampedDir = clamp(normalizedLightDir, vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0));
    vec3 clampedNegDir = clamp(-normalizedLightDir, vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0));
    vec3 factor = RTF * clampedDir + LBB * clampedNegDir;
    return lightColor*(factor.x+factor.y+factor.z);
}


void main()
{
    vec3 lightingRLT = texture(uFlipBookLightingMap.mapRLT, vTexCoords).rgb;
    vec3 lightingBBF = texture(uFlipBookLightingMap.mapBBF, vTexCoords).rgb;
    vec3 lightingRTF = vec3(lightingRLT.rb,lightingBBF.b); //right top front
    vec3 lightingLBB = vec3(lightingRLT.g,lightingBBF.rg); //left bottom back
    float attenuation = getIntensityAttenuation(uPointLight.position, vPosition);
    vec3 lightDir = uPointLight.position - vPosition;
    vec3 lightDirTS = vTBNInversed * lightDir;

    //mat3 TBN = transpose(TBNInversed);
   // fragmentColor = vec4(lightDirTS*0.5+0.5,1.0);
    vec3 lighting = attenuation * computeSixWayLighting(lightingRTF,lightingLBB,lightDirTS,uPointLight.intensity);
    vec4 albedo = texture(uFlipBookAlbedo, vTexCoords);
   // fragmentColor = vec4( TBN[2]*0.5+0.5,albedo.a);
    fragmentColor = vec4(albedo.rgb * lighting,albedo.a);
}