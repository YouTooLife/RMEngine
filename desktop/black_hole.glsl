#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

#define M_Pi 3.14159265358979

mat2 calcRotationMat3(float ang)
{
    return mat2(cos(ang), -sin(ang), sin(ang), cos(ang));
}


void main(void)
{
    mat2 rotMatrix = calcRotationMat3(M_Pi*0.25*time*0.1);
    
    vec2 screen_pos = gl_FragCoord.xy;
    vec2 mouse_pos = mouse*resolution;
    
    vec2 p = rotMatrix*screen_pos * 0.2;
    float value = clamp((cos(p.x) + cos(p.y)) * 10., .1, 1.);
    float light = 0.08 + clamp(1. - distance(screen_pos, mouse_pos) / 150., 0., 1.);
    
    gl_FragColor = vec4(vec3(value*light), 1.);
}