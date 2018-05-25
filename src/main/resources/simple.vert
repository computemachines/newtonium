#version 410 core

in vec4 vPosition;
out vec4 vPos;

uniform mat4 pvm_matrix;


void
main()
{
  vPos = vPosition;
  gl_Position = pvm_matrix * vPosition;
}