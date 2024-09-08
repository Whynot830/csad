package pw2.Task1;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filename = "src/pw2/Task1/input.txt";
        List<String> textLines = List.of("test", "abcd", "qwerty", "xyz");
        Path path = Paths.get(filename);

        try {
            Files.write(path, textLines);
        } catch (IOException e) {
            System.err.println("Failed to write to file " + filename + ": " + e);
        }
        System.out.println(filename + " was created and populated\n");

        try {
            textLines = Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Failed to read the file " + filename + ": " + e);
        }
        System.out.println("Contents of " + filename + ":");
        textLines.forEach(System.out::println);
    }
}
