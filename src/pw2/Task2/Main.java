package pw2.Task2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws IOException {
        String srcFilename = "src/pw2/Task2/input.txt";
        String outFilename = "src/pw2/Task2/output.txt";

        byte[] data = new byte[1048576];
        try (FileOutputStream fos = new FileOutputStream(srcFilename)) {
            for (int i = 0; i < 100; i++) {
                fos.write(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        runAndPrintResults(() -> {
            try {
                copyWithIOStreams(srcFilename, outFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, "Copying file " + srcFilename + " to " + outFilename + " using IO streams");
        runAndPrintResults(() -> {
            try {
                copyWithFileChannel(srcFilename, outFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, "Copying file " + srcFilename + " to " + outFilename + " using FileChannel");
        runAndPrintResults(() -> {
            try {
                copyWithApacheCommonsIO(srcFilename, outFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, "Copying file " + srcFilename + " to " + outFilename + " using Apache Commons IO");
        runAndPrintResults(() -> {
            try {
                copyWithFilesClass(srcFilename, outFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, "Copying file " + srcFilename + " to " + outFilename + " using Files Class");
    }

    public static void runAndPrintResults(Supplier<?> supplier, String label) {
        long startMemory = getMemoryUsage();
        long startTime = System.nanoTime();

        supplier.get();

        long endMemory = getMemoryUsage();
        long elapsedTime = System.nanoTime() - startTime;

        long memoryUsed = endMemory - startMemory;

        System.out.println(label);
        System.out.println("Elapsed time: " + elapsedTime / 1000000 + " ms");
        System.out.println("Memory usage: " + memoryUsed / 1024 + " KB");
    }

    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
//        runtime.gc();
        return runtime.totalMemory() - runtime.freeMemory();
    }


    private static void copyWithIOStreams(String srcFilename, String outFilename) throws IOException {
        FileInputStream fis = new FileInputStream(srcFilename);
        FileOutputStream fos = new FileOutputStream(outFilename);
        byte[] buffer = new byte[1024];
        int bytesRead = fis.read();
        while (bytesRead != -1) {
            fos.write(buffer, 0, bytesRead);
            bytesRead = fis.read(buffer);
        }
        fos.close();
        fis.close();
    }

    private static void copyWithFileChannel(String srcFilename, String outFilename) throws IOException {
        FileInputStream fis = new FileInputStream(srcFilename);
        FileOutputStream fos = new FileOutputStream(outFilename);
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        outChannel.close();
        fos.close();
        fis.close();
    }

    private static void copyWithApacheCommonsIO(String srcFilename, String outFilename) throws IOException {
        File srcFile = new File(srcFilename);
        File outFile = new File(outFilename);
        FileUtils.copyFile(srcFile, outFile);
    }

    private static void copyWithFilesClass(String srcFilename, String outFilename) throws IOException {
        Path srcPath = Path.of(srcFilename);
        Path outPath = Path.of(outFilename);

        Files.copy(srcPath, outPath, StandardCopyOption.REPLACE_EXISTING);
    }


}
