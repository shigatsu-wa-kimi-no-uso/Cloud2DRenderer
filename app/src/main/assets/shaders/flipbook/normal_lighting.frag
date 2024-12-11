#version 300 es
precision mediump float;

struct PointLight{
    vec3 intensity;
    vec3 position;  //world space
};

struct Material{
    vec3 ka;   //ambient
    vec3 ks;    //specular factor
    vec3 kd;    // diffuse factor
    sampler2D specularMap;   //specular
    sampler2D diffuseMap; //diffuse
    sampler2D normalMap;
    float shininess;
};

struct BlinnPhongMaterial{
    vec3 ka;   //ambient
    vec3 ks;
    vec3 kd;
    float shininess;
};

struct BlinnPhongLight{
    vec3 ambientIntensity;
    vec3 castIntensityArrived;
    vec3 direction;
};


in vec2 vTexCoords;
in vec3 vPosition; // world space
in vec3 vNormal;
in mat3 vTBNInversed; //world to tangent
in mat3 vTBN;   // tangent to world



out vec4 fragmentColor;

uniform vec3 uEyePosition;
uniform vec3 uAmbientIntensity;
uniform PointLight uPointLight;
uniform Material uMaterial;


float getIntensityAttenuation(vec3 lightPos,vec3 litPoint){
    float dis = distance(lightPos,litPoint);
    float disSqrInv = 1.0/(dis*dis);
    return 1.0;
}

vec3 lambert(vec3 kd, vec3 normal, vec3 lightVec, vec3 lightIntensity){
    float cosNL = dot(normal,lightVec);
    vec3 diffColor = kd * max(0.0,cosNL) * lightIntensity;
   // fragmentColor = vec4(cosNL ,cosNL ,cosNL , 1.0);
    return diffColor;
}

vec3 phong(vec3 ks, float cosAlpha, float specularFact, vec3 lightIntensity){
    vec3 specColor = ks * pow(max(0.0,cosAlpha),specularFact) * lightIntensity;
    return specColor;
}


float getSpecularCosAlpha(vec3 viewVec,vec3 lightVec,vec3 normal){
    vec3 halfVec = normalize(viewVec + lightVec);
    return dot(normal,halfVec);
}



vec3 getDiffuseSpecular(vec3 viewVec, vec3 normal, BlinnPhongMaterial material,
                        BlinnPhongLight light){
    float cosAlpha = getSpecularCosAlpha(viewVec,light.direction,normal);
    vec3 diffuseColor = lambert(material.kd, normal, light.direction, light.castIntensityArrived);
    vec3 specularColor = phong(material.ks, cosAlpha, material.shininess, light.castIntensityArrived);

    return diffuseColor + specularColor;
}

vec3 blinnPhong(vec3 viewVec, vec3 normal, BlinnPhongMaterial material, BlinnPhongLight light){
    vec3 ambient = light.ambientIntensity * material.ka;
    vec3 diffSpec = getDiffuseSpecular(viewVec, normal,
                                       material,
                                       light);
    vec3 color = ambient + diffSpec;

    return color;
}

void main()
{
    BlinnPhongMaterial material;
    vec4 sampledKd = texture(uMaterial.diffuseMap, vTexCoords);
    material.kd = vec3(sampledKd) * uMaterial.kd;
    material.ks = uMaterial.ks;
    material.ka = uMaterial.ka;
    material.shininess = uMaterial.shininess;
    vec3 eyePositionTS = vTBNInversed * uEyePosition;
    vec3 reflectPointTS =  vTBNInversed * vPosition;
    vec3 lightPositionTS = vTBNInversed *  uPointLight.position;
    vec3 viewVecWS = normalize(uEyePosition - vPosition);
    vec3 lightDirWS = normalize(uPointLight.position - vPosition);
    vec3 viewVecTS = normalize(eyePositionTS - reflectPointTS);
    vec3 lightDirTS = normalize(lightPositionTS - reflectPointTS);
    //vec3 lightDirTS = vTBNInversed*lightDirWS;
    //vec3 viewVecTS = vTBNInversed * viewVecWS;
    BlinnPhongLight light;
    light.ambientIntensity = uAmbientIntensity;
    light.castIntensityArrived = uPointLight.intensity * getIntensityAttenuation(uPointLight.position, vPosition);
    light.direction = lightDirTS;
    vec3 normalMapVal = texture(uMaterial.normalMap, vTexCoords).rgb;
    vec3 normalTS = normalize(normalMapVal * 2.0 - 1.0);
    vec3 color = blinnPhong(viewVecTS, normalTS, material, light);
   // fragmentColor = vec4(sam);
    fragmentColor = vec4(color, sampledKd.a);
}