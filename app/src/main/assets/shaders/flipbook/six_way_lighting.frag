#version 310 es
#extension GL_EXT_shader_io_blocks : enable
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

in Varying{
    vec2 texCoords;
    vec3 position;
    mat3 TBNInversed;
    mat3 TBN;
}fs_in;


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
    vec3 lightingRLT = texture(uFlipBookLightingMap.mapRLT,fs_in.texCoords).rgb;
    vec3 lightingBBF = texture(uFlipBookLightingMap.mapBBF,fs_in.texCoords).rgb;
    vec3 lightingRTF = vec3(lightingRLT.rb,lightingBBF.b); //right top front
    vec3 lightingLBB = vec3(lightingRLT.g,lightingBBF.rg); //left bottom back
    float attenuation = getIntensityAttenuation(uPointLight.position,fs_in.position);
    vec3 lightDir = uPointLight.position - fs_in.position;
    vec3 lightDirTS = fs_in.TBNInversed * lightDir;

    //mat3 TBN = transpose(fs_in.TBNInversed);
   // fragmentColor = vec4(lightDirTS*0.5+0.5,1.0);
    vec3 lighting = attenuation * computeSixWayLighting(lightingRTF,lightingLBB,lightDirTS,uPointLight.intensity);
    vec4 albedo = texture(uFlipBookAlbedo,fs_in.texCoords);
   // fragmentColor = vec4( fs_in.TBN[2]*0.5+0.5,albedo.a);
    fragmentColor = vec4(albedo.rgb * lighting,albedo.a);
}