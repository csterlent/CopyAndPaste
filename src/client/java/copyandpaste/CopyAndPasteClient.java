package copyandpaste;

import com.fox2code.foxloader.loader.ClientMod;
import com.fox2code.foxloader.registry.CommandCompat;

public class CopyAndPasteClient extends CopyAndPaste implements ClientMod {
  public void onInit() {
    CommandCompat.registerCommand(new CopyAndPasteCommand("a"));
    CommandCompat.registerCommand(new CopyAndPasteCommand("z"));
    CommandCompat.registerCommand(new CopyAndPasteCommand("d"));
    CommandCompat.registerCommand(new CopyAndPasteCommand("c"));
    CommandCompat.registerCommand(new CopyAndPasteCommand("v"));
  }
}
