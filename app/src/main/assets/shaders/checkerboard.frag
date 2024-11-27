#version 300 es
precision mediump float;

struct PointLight{
    vec3 intensity;
    vec3 position;  //world space
};

struct Material{
    vec3 ka;   //ambient
    vec3 ks;    //specular factor
    vec3 kd;
    sampler2D specularMap;   //specular
    sampler2D diffuseMap; //diffuse
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


in vec3 vPosition; // world space
in vec3 vNormal;
   // vec3 normalOS;



out vec4 fragmentColor;

uniform vec3 uEyePosition;
uniform vec3 uAmbientIntensity;
uniform PointLight uPointLight;
uniform Material uMaterial;
uniform vec3 colors[2];



float getIntensityAttenuation(vec3 lightPos,vec3 litPoint){
    float dis = distance(lightPos,litPoint);
    float disSqrInv = 1.0/(dis*dis);
    return 1.0;
}

vec3 lambert(vec3 kd, vec3 normal, vec3 lightVec, vec3 lightIntensity){
    float cosNL = dot(normal,lightVec);
    vec3 diffColor = kd * max(0.0,cosNL) * lightIntensity;
   // fragmentColor = vec4(normal,1.0);
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
    float epsilon = 0.0001;
    int x = int(floor(vPosition.x + epsilon));
    int y = int(floor(vPosition.y + epsilon));
    int z = int(floor(vPosition.z + epsilon));
    int index = abs(x + y + z) % 2;

    BlinnPhongMaterial material;
    material.kd = colors[index]*uMaterial.kd;
    material.ks = uMaterial.ks;
    material.ka = uMaterial.ka;
    material.shininess = uMaterial.shininess;
    vec3 viewVec = normalize(uEyePosition - vPosition);
    BlinnPhongLight light;
    light.ambientIntensity = uAmbientIntensity;
    light.castIntensityArrived = uPointLight.intensity*getIntensityAttenuation(uPointLight.position, vPosition);
    light.direction = normalize(uPointLight.position - vPosition);

    vec3 color = blinnPhong(viewVec, normalize(vNormal), material, light);

   // fragmentColor = vec4(normalize(vNormal)*0.5+0.5,1.0);
    fragmentColor = vec4(color,1.0);
//    fragmentColor = vec4(colors[index], 1.0);
}