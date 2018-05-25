#version 410 core

in vec4 vPos;
out vec4 frag_color;

void
main()
{
  frag_color = vPos;
  if (!gl_FrontFacing) {
    frag_color = vec4(1, 0, 0, 1);
  }

  frag_color = vec4(floor(mod((vPos.x)*5, 2))*floor(mod((vPos.y+vPos.x-sin(vPos.z*2))*5, 2)));
}