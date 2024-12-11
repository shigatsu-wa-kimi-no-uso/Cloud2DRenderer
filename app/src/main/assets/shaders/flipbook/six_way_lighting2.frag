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
    float currFrameIndex; // 从0开始
    vec2 flipBookShape;
};


in float vRatio;
in vec2 vNextTexCoords;
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


vec4 texSampleAndMix(sampler2D tex,vec2 texCoords1,vec2 texCoords2,float factor){
    return mix(texture(tex,texCoords1),texture(tex,texCoords2),factor);
 //   return texture(tex,texCoords1);
}

void main()
{
    vec3 lightMapRTB = texSampleAndMix(uFlipBookLightMap.mapRTB, vTexCoords, vNextTexCoords, vRatio).rgb;
   // vec3 lightMapRTB = texture(uFlipBookLightMap.mapRTB, vTexCoords).rgb;
    vec3 lightMapLBF = texSampleAndMix(uFlipBookLightMap.mapLBF, vTexCoords, vNextTexCoords, vRatio).rgb;
    //vec3 lightMapLBF = texture(uFlipBookLightMap.mapLBF, vTexCoords).rgb;
    vec3 lightMapRTF = vec3(lightMapRTB.rg, lightMapLBF.b); //right top front
    vec3 lightMapLBB = vec3(lightMapLBF.rg, lightMapRTB.b); //left bottom back
    float attenuation = getIntensityAttenuation(uPointLight.position, vPosition);
    vec3 lightDir = normalize(uPointLight.position - vPosition);

    vec3 pointLighting = attenuation * getLighting(vTBNInversed,lightDir,lightMapRTF,lightMapLBB,uPointLight.intensity);
    vec3 distantLighting = getLighting(vTBNInversed,normalize(-uDistantLight.direction),lightMapRTF,lightMapLBB,uDistantLight.intensity);
   // vec4 albedo = texture(uFlipBookAlbedo, vTexCoords);

    vec4 albedo = texSampleAndMix(uFlipBookAlbedo, vTexCoords,vNextTexCoords,vRatio);

    vec3 ambient = vec3(1,1,1);
   // fragmentColor = vec4( TBN[2]*0.5+0.5,albedo.a);
    float alpha = albedo.a;

   // alpha = pow(alpha,5.0);
   // vec3 color = mix(vec3(1,1,1),distantLighting,alpha);
    vec3 color = distantLighting;
    fragmentColor = vec4(clamp(color,0.0,1.0),alpha);
    //fragmentColor = vec4(debug_val,debug_val,debug_val,1);
}