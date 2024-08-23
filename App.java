public class App {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered!");
        }));

        System.out.println("Press Ctrl+C to trigger the shutdown hook.");
        try {
            Thread.sleep(Long.MAX_VALUE);  // Simulate a long-running process
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
