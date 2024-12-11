#version 300 es
precision mediump float;
precision highp int;

struct PointLight {
    vec3 intensity;
    vec3 position;
};

struct DistantLight{
    vec3 intensity;
    vec3 direction;
};

struct SixWayLightingMap{
    sampler2D mapRTB; //right top back
    sampler2D mapLBF; //left bottom front
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
in float debug_val;


out vec4 fragmentColor;


uniform PointLight uPointLight;
uniform DistantLight uDistantLight;
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


vec3 getLighting(mat3 inversedTBN,vec3 lightDir,vec3 lightMapRTF,vec3 lightMapLBB,vec3 intensity){
    vec3 lightDirTS = normalize(inversedTBN * lightDir);
    vec3 lighting = computeSixWayLighting(lightMapRTF,lightMapLBB,lightDirTS,intensity);
    return lighting;
}

void main()
{
    vec3 lightMapRTB = texture(uFlipBookLightMap.mapRTB, vTexCoords).rgb;
    vec3 lightMapLBF = texture(uFlipBookLightMap.mapLBF, vTexCoords).rgb;
    vec3 lightMapRTF = vec3(lightMapRTB.rg, lightMapLBF.b); //right top front
    vec3 lightMapLBB = vec3(lightMapLBF.rg, lightMapRTB.b); //left bottom back
    float attenuation = getIntensityAttenuation(uPointLight.position, vPosition);
    vec3 lightDir = normalize(uPointLight.position - vPosition);

    vec3 pointLighting = attenuation * getLighting(vTBNInversed,lightDir,lightMapRTF,lightMapLBB,uPointLight.intensity);
    vec3 distantLighting = getLighting(vTBNInversed,normalize(-uDistantLight.direction),lightMapRTF,lightMapLBB,uDistantLight.intensity);
    vec4 albedo = texture(uFlipBookAlbedo, vTexCoords);
    //float alpha = texture(uFlipBookLightMap.mapRTB, vTexCoords).a;
   // vec3 albedo = vec3(lightingLBB.b);

   // fragmentColor = vec4( TBN[2]*0.5+0.5,albedo.a);
    fragmentColor = vec4(albedo.rgb*distantLighting,albedo.a);
    //fragmentColor = vec4(debug_val,debug_val,debug_val,1);
}