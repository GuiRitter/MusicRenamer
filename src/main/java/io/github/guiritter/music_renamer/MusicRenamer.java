package io.github.guiritter.music_renamer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public final class MusicRenamer {

    public static final String getExtension(File file) {
        if (file == null) {
            return "";
        }
        String fieldArray[] = file.getName().split("\\.");
        if (fieldArray.length == 0) {
            return "";
        }
        return fieldArray[fieldArray.length - 1];
    }

    public static final void main(String args[]) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File folder;
        if ((folder = chooser.getSelectedFile()) == null) {
            return;
        }
        Arrays.stream(folder.listFiles()).forEach(file -> {
            Mp3File mp3File;
            try {
                mp3File = new Mp3File(file);
            } catch (UnsupportedTagException | InvalidDataException | IOException e) {
                JOptionPane.showMessageDialog(null, "Error opening file:\n" + e.getMessage());
                e.printStackTrace();
                return;
            }
            ID3v2 id3v2Tag = mp3File.getId3v2Tag();
            try {
                Files.move(file.toPath(), file.toPath().resolveSibling(String.format("%s · %s.%s", id3v2Tag.getArtist(), id3v2Tag.getTitle(), getExtension(file))));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error renaming file:\n" + e.getMessage());
                e.printStackTrace();
            }
        });
        // TODO inform when finished
    }
}
