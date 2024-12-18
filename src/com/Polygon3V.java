package com;

public final class Polygon3V extends RenderObject {

   public final Vertex a;
   public final Vertex b;
   public final Vertex c;
   
   public final byte au;
   public final byte av;
   
   public final byte bu;
   public final byte bv;
   
   public final byte cu;
   public final byte cv;


   public Polygon3V(Vertex a, Vertex b, Vertex c, byte au, byte av, byte bu, byte bv, byte cu, byte cv) {
      super(a, b, c);
      this.a = a;
      this.b = b;
      this.c = c;
      this.au = au;
      this.av = av;
      this.bu = bu;
      this.bv = bv;
      this.cu = cu;
      this.cv = cv;
   }

   private static int size(int a, int b, int c) {
      int var3;
      if(b < a) {
         var3 = a;
         a = b;
         b = var3;
      }

      if(c < a) {
         var3 = c;
         c = a;
         a = var3;
      }

      if(c < b) {
         c = b;
      }

      return c - a;
   }

   public final void render(Graphics3D g3d, Texture texture) {
      if(texture.perspectiveCorrection && (size(this.a.sx, this.b.sx, this.c.sx) > 45 || size(this.a.sy, this.b.sy, this.c.sy) > 45) && size(this.a.rz, this.b.rz, this.c.rz) > 200) {
         Texture.paintPers(g3d, texture, this.a, this.au & 255, this.av & 255, this.b, this.bu & 255, this.bv & 255, this.c, this.cu & 255, this.cv & 255);
      } else {
         Texture.paintAffine(g3d, texture, this.a, this.au & 255, this.av & 255, this.b, this.bu & 255, this.bv & 255, this.c, this.cu & 255, this.cv & 255);
      }
   }

   public final boolean isVisible(int x1, int y1, int x2, int y2) {
      if((this.a.sx - this.b.sx) * (this.b.sy - this.c.sy) <= (this.a.sy - this.b.sy) * (this.b.sx - this.c.sx)) {
         return false;
      } else {
         this.sz = (this.a.rz + this.b.rz + this.c.rz) / 3;
         return this.sz > 0?false:(this.a.sx <= x1 && this.b.sx <= x1 && this.c.sx <= x1?false:(this.a.sy <= y1 && this.b.sy <= y1 && this.c.sy <= y1?false:(this.a.sx >= x2 && this.b.sx >= x2 && this.c.sx >= x2?false:this.a.sy < y2 || this.b.sy < y2 || this.c.sy < y2)));
      }
   }
}
