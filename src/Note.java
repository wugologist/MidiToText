import java.util.Objects;

/**
 * A single Midi Note.
 */
class Note {
  private final int instrument;
  private final int key;

  /**
   * Create a Midi Note.
   * @param instrument The instrument of the note. Will be stored mod 16 due to channel limit.
   * @param key The key of the pitch. Key 60 = C4.
   */
  Note(int instrument, int key){
    // Some midi instruments start at 0, some at 1. The example ones from the homework start at 1.
    // Better to be off by an instrument than to pass negative values.
    // Should probably think this through more...
    this.instrument = instrument + 1;
    this.key = key;
  }

  int getInstrument() {
    return instrument;
  }

  int getKey() {
    return key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Note note = (Note) o;

    return instrument == note.instrument && key == note.key;

  }

  @Override
  public int hashCode() {
    return Objects.hash(instrument, key);
  }
}
