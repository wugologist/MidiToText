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
    if (args.length != 2) {
      System.err.print("Usage: args[0] = path/to/midi, args[1] = path/to/output");
      return;
    }

    Sequence in;
    PrintWriter out;

    try {
      in = MidiSystem.getSequence(new File(args[0]));
      out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
      MidiReader reader = new MidiReader(out);
      reader.read(in);
      out.flush();
    } catch (InvalidMidiDataException | IOException e) {
      e.printStackTrace();
    }
  }
}
