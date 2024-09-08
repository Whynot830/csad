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
                if (file.getType() == acceptableFileType) {
                    long delayMs = file.getSize() * 7L;
                    Thread.sleep(delayMs);
                    System.out.println(file.getType().getValue() + " file (" + file.getSize() + " bytes) has been processed, time elapsed: " + delayMs + "ms");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
