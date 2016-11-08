import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Process Midi files.
 * Based on http://stackoverflow.com/questions/3850688/reading-midi-files-in-java
 */
class MidiReader {
  private final Appendable output;
  /**
   * Map of Notes which have yet ended to their initial tick and velocity
   */
  private Map<Note, NoteInfo> currentNotes = new HashMap<>();

  /**
   * Make a MidiReader to the given output.
   * @param output Where to write the output to
   */
  MidiReader(Appendable output) {
    this.output = output;
  }

  /**
   * Read a Midi Sequence into the output.
   * @param input The Midi Sequence to read.
   * @throws IOException If something goes wrong appending.
   */
  void read(Sequence input) throws IOException {
    output.append("tempo ")
            .append(String.valueOf(input.getMicrosecondLength() / input.getTickLength()))
            .append("\r\n");

    for (Track track :  input.getTracks()) {
      for (int i = 0; i < track.size(); i++) {
        MidiEvent event = track.get(i);
        MidiMessage message = event.getMessage();

        // We only care about Note On/Off messages, which are short messages
        if (message instanceof ShortMessage) {
          ShortMessage sm = (ShortMessage) message; // Necessary evil

          int key = sm.getData1();
          int velocity = sm.getData2();
          long tick = event.getTick();
          Note n = new Note(sm.getChannel(), key);

          if (sm.getCommand() == ShortMessage.NOTE_ON) {
            // Note Off events can also be sent as Note On events with 0 velocity
            if (velocity == 0) {
              processNoteOff(n, tick);
            } else {
              currentNotes.put(n, new NoteInfo(velocity, tick));
            }
          } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
            processNoteOff(n, tick);
          }

          // ignore other messages
        }
      }
    }
  }

  /**
   * Process a note ending.
   * @param n The note that has ended.
   * @param tickOff The tick when the note ended.
   * @throws IOException If appending goes wrong.
   */
  private void processNoteOff(Note n, long tickOff) throws IOException {
    NoteInfo info = currentNotes.remove(n);
    // Sometimes messages seem to be in the wrong order, maybe from bad input?
    // We ignore whenever we try to remove notes before they begin, i.e. aren't in the map.
    if (info != null) {
      output.append("note ").append(String.valueOf(info.getStartTone())).append(" ")
              .append(String.valueOf(tickOff)).append(" ")
              .append(String.valueOf(n.getInstrument())).append(" ")
              .append(String.valueOf(n.getKey())).append(" ")
              .append(String.valueOf(info.getVelocity()))
              .append("\r\n");
    }
  }

  /**
   * Stores some info about notes not in the Note class.
   * This class is necessary because of the fact that Midi identifies note equality for the purposes
   * of Note Off events using only the instrument and key of the note, meaning velocity and start
   * beat information cannot be stored in the same way as them, since the information would be
   * lost when removing the note.
   */
  private class NoteInfo {
    private final int velocity;
    private final long startTick;

    /**
     * Create a NoteInfo.
     * @param velocity The Note's velocity.
     * @param startTick The starting tick of the note.
     */
    NoteInfo(int velocity, long startTick) {
      this.velocity = velocity;
      this.startTick = startTick;
    }

    int getVelocity() {
      return velocity;
    }

    long getStartTone() {
      return startTick;
    }
  }
}
