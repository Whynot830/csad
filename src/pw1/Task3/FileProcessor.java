package pw1.Task3;

import java.util.concurrent.BlockingQueue;

public class FileProcessor implements Runnable {
    private final BlockingQueue<File> queue;
    private final FileType acceptableFileType;

    public FileProcessor(BlockingQueue<File> queue, FileType acceptableFileType) {
        this.queue = queue;
        this.acceptableFileType = acceptableFileType;
    }

    @Override
    public void run() {
        while (true) {
            try {
                File file = queue.take();
                if (file.type() == acceptableFileType) {
                    long delayMs = file.size() * 7L;
                    Thread.sleep(delayMs);
                    System.out.println(file.type().getValue() + " file (" + file.size() + " bytes) has been processed, time elapsed: " + delayMs + "ms");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
