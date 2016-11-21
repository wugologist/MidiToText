import java.awt.FileDialog;
import java.io.File;
import javax.swing.JFrame;

/**
 * A couple of utility methods to request filenames from the user via the operating system
 * file picker GUI.
 */
class GuiFilePrompts {

  /**
   * Displays the file dialog provided and returns the user input.
   * This part is similar in both save and open dialogs, so it's abstracted out.
   * @param fd The file dialog to run.
   * @return a File object representing the filename the user requested, or null if the user
   *         pressed cancel.
   */
  private static File runDialog(FileDialog fd) {
    fd.setVisible(true);
    String filename = fd.getFile();
    if (filename != null) {
      String filterPath = fd.getDirectory();
      return new File(filterPath, filename);
    } else {
      return null;
    }
  }

  /**
   * A method to open a dialog to request a filename from the user.
   * The dialog is configured as an open dialog.
   * This method blocks until the user has closed the dialog.
   * @return a File object representing the filename the user requested, or null if the user
   *         pressed cancel.
   */
  static File requestOpenFile() {
    FileDialog fd = new FileDialog(new JFrame(), "Select a File", FileDialog.LOAD);
    fd.setFilenameFilter((dir, name) -> name.endsWith(".midi") || name.endsWith(".mid"));
    return runDialog(fd);
  }

  /**
   * A method to open a dialog to request a filename from the user.
   * The dialog is configured as an load dialog.
   * This method blocks until the user has closed the dialog.
   * @return a File object representing the filename the user requested, or null if the user
   *         pressed cancel.
   */
  static File requestSaveFile() {
    FileDialog fd = new FileDialog(new JFrame(), "Save File", FileDialog.SAVE);
    fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
    fd.setFile("*.txt");
    return runDialog(fd);
  }

}
