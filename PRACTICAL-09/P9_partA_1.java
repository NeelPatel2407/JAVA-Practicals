class P9_partA_1 {
    static int counter = 0;

    static void increment() {
        counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        int threads = 10;
        int increments = 100000;

        Thread[] t = new Thread[threads];

        for(int i = 0; i < threads; i++) {
            t[i] = new Thread(() -> {
                for(int j = 0; j < increments; j++) {
                    increment();
                }
            });
            t[i].start();
        }

        for(int i = 0; i < threads; i++) {
            t[i].join();
        }

        System.out.println("Without synchronization:");
        System.out.println("Expected: " + (threads * increments));
        System.out.println("Actual: " + counter);

        counter = 0;

        for(int i = 0; i < threads; i++) {
            t[i] = new Thread(() -> {
                for(int j = 0; j < increments; j++) {
                    synchronized(P9_partA_1.class) {
                        counter++;
                    }
                }
            });
            t[i].start();
        }

        for(int i = 0; i < threads; i++) {
            t[i].join();
        }

        System.out.println("\nWith synchronization:");
        System.out.println("Expected: " + (threads * increments));
        System.out.println("Actual: " + counter);
    }
}