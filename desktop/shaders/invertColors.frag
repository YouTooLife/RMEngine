#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif


varying LOWP vec4 v_color;
varying vec2 v_texCoords;


varying vec2 v_lightPos;
varying vec4 v_position;

uniform sampler2D u_texture;


void main(){
    
    int dropoffDist = 300;
    float dist = 0;
    
    dist = pow(abs(v_lightPos.x-v_position.x), 2);
    dist += pow(abs(v_lightPos.y-v_position.y), 2);
    dist = sqrt(dist);
    
       //как и в стандартном шейдере получаем итоговый цвет пикселя
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords) * min(.75,(dropoffDist / max(1,dist)));
    //после получения итогового цвета, меняем его на противоположный
    //gl_FragColor.rgb=1.0-gl_FragColor.rgb;
}