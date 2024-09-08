package pw1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Task1 {
    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
//        runtime.gc();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static List<Callable<Integer>> getCallables(List<Integer> list, int availableThreads) {
        List<Callable<Integer>> callables = new ArrayList<>();
        int batchSize = list.size() / availableThreads;
        for (int i = 0; i < availableThreads; i++) {
            int startIdx = i * batchSize;
            int endIdx = i != availableThreads - 1 ? (i + 1) * batchSize : list.size();
            callables.add(() -> subSumCalc(list, startIdx, endIdx));
        }
        return callables;
    }

    private static int subSumCalc(List<Integer> list, int startIdx, int endIdx) throws InterruptedException {
        int subSum = 0;
        for (int integer : list.subList(startIdx, endIdx)) {
            subSum += integer;
            Thread.sleep(1);
        }
        return subSum;
    }

    static class SumCalcTask extends RecursiveTask<Integer> {
        private final List<Integer> list;
        private final int startIdx;
        private final int endIdx;

        public SumCalcTask(List<Integer> list, int startIdx, int endIdx) {
            this.list = list;
            this.startIdx = startIdx;
            this.endIdx = endIdx;
        }

        @Override
        protected Integer compute() {
            if (endIdx - startIdx < list.size()) {
                try {
                    return subSumCalc(list, startIdx, endIdx);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            int midIdx = startIdx + (endIdx - startIdx) / 2;

            SumCalcTask left = new SumCalcTask(list, startIdx, midIdx);
            SumCalcTask right = new SumCalcTask(list, midIdx, endIdx);

            left.fork();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return right.compute() + left.join();
        }
    }

    public static void runAndPrintResults(Supplier<Integer> supplier, String label) {
        long startMemory = getMemoryUsage();
        long startTime = System.nanoTime();

        int result = supplier.get();

        long endMemory = getMemoryUsage();
        long elapsedTime = System.nanoTime() - startTime;

        long memoryUsed = endMemory - startMemory;

        System.out.println(label);
        System.out.println("Elapsed time: " + elapsedTime / 1000000 + " ms");
        System.out.println("Memory usage: " + memoryUsed / 1024 + " KB");
        System.out.println("Result: " + result + "\n");
    }

    public static int calcSumInSeq(List<Integer> list) throws InterruptedException {
        int sum = 0;
        for (Integer integer : list) {
            sum += integer;
            Thread.sleep(1);
        }
        return sum;
    }

    public static int calcSumMultiThreaded(List<Integer> list) throws InterruptedException, ExecutionException {
        int availableThreads = Runtime.getRuntime().availableProcessors();
        int sum = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(availableThreads);
        List<Callable<Integer>> callables = getCallables(list, availableThreads);
        List<Future<Integer>> futures = executorService.invokeAll(callables);

        for (Future<Integer> future : futures) {
            int subSum = future.get();
            sum += subSum;
            Thread.sleep(1);
        }
        executorService.shutdown();
        return sum;
    }

    public static int calcSumFork(List<Integer> list) throws InterruptedException, ExecutionException {
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            SumCalcTask task = new SumCalcTask(list, 0, list.size());
            return forkJoinPool.invoke(task);
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            Integer r = rand.nextInt() % 256;
            list.add(r);
        }

        runAndPrintResults(() -> {
            try {
                return calcSumInSeq(list);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Calculating sum (single-threaded):");
        runAndPrintResults(() -> {
            try {
                return calcSumMultiThreaded(list);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, "Calculating sum (multi-threaded):");
        runAndPrintResults(() -> {
            try {
                return calcSumFork(list);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, "Calculating sum (fork join):");
    }
}