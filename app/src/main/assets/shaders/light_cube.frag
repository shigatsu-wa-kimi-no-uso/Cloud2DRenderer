#version 300 es
precision mediump float;

in vec3 vPosition;
out vec4 fragmentColor;

uniform vec3 uLightIntensity;
uniform vec3 uEyePosition;


float getIntensityAttenuation(vec3 lightPos,vec3 litPoint){
    float dis = distance(lightPos,litPoint);
    float disSqrInv = 3.0/dis;
    return disSqrInv;
}

void main()
{
    vec3 color = uLightIntensity * getIntensityAttenuation(vPosition, uEyePosition);
    fragmentColor = vec4(color, 1.0);
}