#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform vec2 mouse;
uniform vec3 u_color;
uniform float time;


void main(void){
    
    
    vec2 uvv = 1.5*(2.0*gl_FragCoord.xy - resolution.xy) / resolution.y;
    vec2 offset = (vec2(0,0)*2.0-1.0)*1.5;
    offset.x *= resolution.x/resolution.y;
    //float t = time*50.0;
    //vec2 offset = vec2(cos(t),+sin(t))/10.0;
    //how could I make an eclipse?
    
    vec3 light_color = vec3(0.4, 0.3, 0.0);
    float light = 0.0;
    
    light = 0.1 / distance(normalize(uvv), uvv);
    
    if(length(uvv) < 1.0){
        light *= 0.1 / distance(normalize(uvv-offset), uvv-offset);
    }
    
    gl_FragColor = vec4(light*light_color, 0.8);

}