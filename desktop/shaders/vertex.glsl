attribute vec4 a_position;
uniform mat4 u_projTrans;

varying vec4 v_position;

void main()
{
    
    gl_Position = u_projTrans * a_position;
}