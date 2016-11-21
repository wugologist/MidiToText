# MidiToText
CS3500-formatted text files from Midi files  
With apologies to Ben Lerner

## Usage
Compile to jar:  
`java -jar ./MidiToText /path/to/input.midi /path/to/output.txt` 

Running otherwise:  
`args[0] = /path/to/input.midi, args[1] = /path/to/output.txt`  

This can also be run without arguments for open & save dialogs. This lets the program be run without
a terminal.

## Output Format
First line:  
`tempo <number>`  
where `<number>` is the tempo, in microseconds per beat.
This number isn't always correct (for now), you may need to adjust it depending on your input.

Rest of lines:  
`note <start> <stop> <instrument> <key> <velocity>`  
where `<start>` and `<stop>` are the beginning and end beats, respectively, `<instrument>` is the
instrument, `<key>` is the key of the note (60 = C4), and `<velocity>` is the volume, in [0, 127].
