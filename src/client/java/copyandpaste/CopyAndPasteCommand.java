package copyandpaste;

import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;

import java.lang.Math;

import net.minecraft.client.Minecraft;
import net.minecraft.src.client.physics.MovingObjectPosition;

class CopyAndPasteCommand extends CommandCompat {
  public static BlockBuffer bufferZ = new BlockBuffer();
  public static BlockBuffer bufferV = new BlockBuffer();

  public static int tx, ty, tz; // target coord
  public static boolean targetted = false;

  public CopyAndPasteCommand(String cmd) {
    super(cmd);
  }

  @Override
  public void onExecute(String[] args, NetworkPlayer player) {
    if (player.getConnectionType().toString() != "SINGLE_PLAYER") {
      player.displayChatMessage("Mod incompatible with multiplayer!");
    }
    if (args.length > 1 && !args[1].equals("me") || args.length > 2) {
      player.displayChatMessage("Usage: " + args[0] + " [me]");
      return;
    }
    boolean atPlayer = args.length > 1;

    // get the coordinates user is referring to
    Integer x, y, z;
    if (atPlayer) {
      x = (int)Math.floor(player.getRegisteredX());
      y = (int)Math.floor(Minecraft.theMinecraft.thePlayer.boundingBox.minY);
      z = (int)Math.floor(player.getRegisteredZ());
    }
    else {
      MovingObjectPosition mop = Minecraft.theMinecraft.thePlayer.rayTrace(20, 1); // (maxDistance, deltaTicks)
      if (mop == null) {
        player.displayChatMessage("First look at a block (range 20m)");
        return;
      }
      x = mop.blockX;
      y = mop.blockY;
      z = mop.blockZ;
    }

    int tx = CopyAndPasteCommand.tx;
    int ty = CopyAndPasteCommand.ty;
    int tz = CopyAndPasteCommand.tz;
    boolean targetted = CopyAndPasteCommand.targetted;
    if (args[0].equals("/a")) {
      CopyAndPasteCommand.tx = x;
      CopyAndPasteCommand.ty = y;
      CopyAndPasteCommand.tz = z;
      CopyAndPasteCommand.targetted = true;
    }
    else if (args[0].equals("/z")) {
      CopyAndPasteCommand.bufferZ.put(x, y, z);
    }
    else if (args[0].equals("/d")) {
      if (targetted) {
        CopyAndPasteCommand.bufferZ.get(tx, ty, tz, x, y, z);
        CopyAndPasteCommand.bufferV.fill(tx, ty, tz, x, y, z, 0, 0);
      }
      else {
        player.displayChatMessage("First choose a corner to target with /a");
      }
    }
    else if (args[0].equals("/c")) {
      if (targetted) {
        CopyAndPasteCommand.bufferV.get(tx, ty, tz, x, y, z);
      }
      else {
        player.displayChatMessage("First choose a corner to target with /a");
      }
    }
    else if (args[0].equals("/v")) {
      CopyAndPasteCommand.bufferZ.copySizeAndGet(x, y, z, CopyAndPasteCommand.bufferV);
      CopyAndPasteCommand.bufferV.put(x, y, z);
    }
  }
}
