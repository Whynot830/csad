package pw1.Task3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class FileGenerator implements Runnable {
    private final BlockingQueue<File> queue;

    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random rand = new Random();
        FileType[] types = FileType.values();
        while (true) {
            try {
                Thread.sleep(rand.nextInt(900) + 100);
                int size = rand.nextInt(90) + 10;
                int fileTypeIdx = rand.nextInt(types.length);
                File file = new File(types[fileTypeIdx], size);
                queue.put(file);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
