package pw1.Task3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);
        Thread generatorThread = new Thread(new FileGenerator(queue));

        Thread jsonProcessorThread = new Thread(new FileProcessor(queue, FileType.JSON));
        Thread xmlProcessorThread = new Thread(new FileProcessor(queue, FileType.XML));
        Thread xlsProcessorThread = new Thread(new FileProcessor(queue, FileType.XLS));

        generatorThread.start();
        jsonProcessorThread.start();
        xmlProcessorThread.start();
        xlsProcessorThread.start();
    }
}
