import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

/**
 * Read a Midi file into a format usable by the OOD Music Editor.
 */
public class MidiToText {
  public static void main(String[] args) {
    if (args.length != 2 && args.length != 0) {
      System.err.print("Usage: args[0] = path/to/midi, args[1] = path/to/output");
      return;
    }

    File input;
    File output;

    if (args.length == 0) {
      input = GuiFilePrompts.requestOpenFile();
      if (input == null) {
        System.err.print("Fatal: File open dialog canceled.");
        // Required because the AWT thread doesn't close when the main thread finishes
        System.exit(1);
      }
      output = GuiFilePrompts.requestSaveFile();
      if (output == null) {
        System.err.print("Fatal: File save dialog canceled.");
        System.exit(1);
      }
    } else {
      input = new File(args[0]);
      output = new File(args[1]);
    }

    Sequence in;
    PrintWriter out;

    try {
      in = MidiSystem.getSequence(input);
      out = new PrintWriter(new BufferedWriter(new FileWriter(output)));
      MidiReader reader = new MidiReader(out);
      reader.read(in);
      out.flush();
    } catch (InvalidMidiDataException | IOException e) {
      e.printStackTrace();
      System.exit(2);
    }

    // Required because the AWT thread doesn't close when the main thread finishes
    System.exit(0);
  }
}
