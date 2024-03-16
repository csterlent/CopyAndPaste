package copyandpaste;

import net.minecraft.client.Minecraft;
import net.minecraft.src.game.level.World;

// stores block ids and metadata. put=place blocks, get=read from world
class BlockBuffer {
  // the default values make a buffer that does nothing when put
  public boolean bx=false, by=false, bz=false; // which corner was target
  public int sx=0, sy=0, sz=0; // size of region
  public int[] ids;
  public int[] data;

  private void setSize(int tx, int ty, int tz, int x, int y, int z) {
    this.sx = x - tx;
    this.sy = y - ty;
    this.sz = z - tz;

    this.bx = this.sx < 0;
    this.by = this.sy < 0;
    this.bz = this.sz < 0;

    if (this.bx) this.sx *= -1;
    if (this.by) this.sy *= -1;
    if (this.bz) this.sz *= -1;
    this.sx++;
    this.sy++;
    this.sz++;
  }

  // populate new arrays based on s[xyz] size and b[xyz] which corner
  private void doGet(int x, int y, int z, boolean areTargetCoords) {
    this.ids = new int[this.sx*this.sy*this.sz];
    this.data = new int[this.sx*this.sy*this.sz];

    World world = Minecraft.theMinecraft.theWorld;
    if (this.bx == areTargetCoords) x += 1-this.sx;
    if (this.by == areTargetCoords) y += 1-this.sy;
    if (this.bz == areTargetCoords) z += 1-this.sz;
    for (int i = 0; i < this.sx*this.sy*this.sz; i++) {
      int ix = x + (i/this.sz)/this.sy;
      int iy = y + (i/this.sz)%this.sy;
      int iz = z + i%this.sz;
      ids[i] = world.getBlockId(ix, iy, iz);
      data[i] = world.getBlockMetadata(ix, iy, iz);
      // System.out.println("Get: " + ix + " " + iy + " " + iz);
      // System.out.println(this.ids[i]);
      System.out.println("GET " + this.ids[i] + ":" + this.data[i]);
    }
  }

  public void copySizeAndGet(int tx, int ty, int tz, BlockBuffer bb) {
    this.bx = bb.bx;
    this.by = bb.by;
    this.bz = bb.bz;

    this.sx = bb.sx;
    this.sy = bb.sy;
    this.sz = bb.sz;

    doGet(tx, ty, tz, true);
  }

  public void put(int x, int y, int z) {
    World world = Minecraft.theMinecraft.theWorld;
    world.multiplayerWorld = true;
    if (this.bx) x += 1-this.sx;
    if (this.by) y += 1-this.sy;
    if (this.bz) z += 1-this.sz;
    for (int i = 0; i < this.sx*this.sy*this.sz; i++) {
      int ix = x + (i/this.sz)/this.sy;
      int iy = y + (i/this.sz)%this.sy;
      int iz = z + i%this.sz;
      world.setBlockAndMetadata(ix, iy, iz, this.ids[i], this.data[i]);
      // System.out.println("Put: " + ix + " " + iy + " " + iz);
      System.out.println("PUT " + this.ids[i] + ":" + this.data[i]);
    }
    world.multiplayerWorld = false;
  }

  public void fill(int tx, int ty, int tz, int x, int y, int z, int bid, int metadata) {
    setSize(tx, ty, tz, x, y, z);
    World world = Minecraft.theMinecraft.theWorld;
    if (!this.bx) x += 1-this.sx;
    if (!this.by) y += 1-this.sy;
    if (!this.bz) z += 1-this.sz;
    for (int i = 0; i < this.sx*this.sy*this.sz; i++) {
      int ix = x + (i/this.sz)/this.sy;
      int iy = y + (i/this.sz)%this.sy;
      int iz = z + i%this.sz;
      world.setBlockAndMetadata(ix, iy, iz, bid, metadata);
    }
  }

  // this should be bufferV
  // t[xyz] should already be set from the /a command
  public void get(int tx, int ty, int tz, int x, int y, int z) {
    setSize(tx, ty, tz, x, y, z);
    doGet(x, y, z, false);
  }
}
