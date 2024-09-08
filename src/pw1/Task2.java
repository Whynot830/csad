package pw1;

import java.util.Scanner;
import java.util.concurrent.*;

public class Task2 {
    public static void main(String[] args) {
        try (ExecutorService service = Executors.newFixedThreadPool(8)) {
            while (true) {
                System.out.print("Enter the number or 'quit' to exit the program: ");
                Scanner sc = new Scanner(System.in);
                String input = sc.nextLine();

                if (input.equalsIgnoreCase("quit")) {
                    break;
                }
                try {
                    int number = Integer.parseInt(input);

                    Future<Integer> future = service.submit(() -> {
                        int delaySeconds = ThreadLocalRandom.current().nextInt(1, 6);
                        try {
                            Thread.sleep(delaySeconds * 1000L);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return number * number;
                    });
                    try {
                        int squaredNumber = future.get();
                        System.out.println((number < 0 ? "(" + number + ")" : number) + "^2" + " = " + squaredNumber);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            service.shutdown();
        }
    }
}
