package pw2.Task3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "src/pw2/Task3/input.txt";
        short checksum = calculateChecksum(filename);
        System.out.println("16-bit checksum of " + filename + ": " + checksum);
    }

    public static short calculateChecksum(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            FileChannel channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(2);
            short checksum = 0;

            while (channel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get();
                }
                buffer.clear();
            }
            return checksum;
        }
    }
}
