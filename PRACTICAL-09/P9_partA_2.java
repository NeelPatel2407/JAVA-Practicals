class P9_partA_2 {
    static int seatsLeft = 5;
    static int booked = 0;

    static void bookWithoutSync() {
        if(seatsLeft > 0) {
            try {
                Thread.sleep(10);
            } catch(InterruptedException e) {
            }

            seatsLeft--;
            booked++;
            System.out.println(Thread.currentThread().getName() + " booked a seat");
        }
    }

    static synchronized void book() {
        if(seatsLeft > 0) {
            seatsLeft--;
            booked++;
            System.out.println(Thread.currentThread().getName() + " booked a seat");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];

        System.out.println("Without synchronization:");

        seatsLeft = 5;
        booked = 0;

        for(int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> bookWithoutSync(), "User-" + (Thread.currentThread().getId()));
            threads[i].start();
        }

        for(int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("Seats left: " + seatsLeft);
        System.out.println("Total booked: " + booked);

        System.out.println("\nWith synchronization:");

        seatsLeft = 5;
        booked = 0;

        for(int i = 0; i < 10; i++) {
            final int user = i + 1;

            threads[i] = new Thread(() -> book(), "User-" + user);
            threads[i].start();
        }

        for(int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("Seats left: " + seatsLeft);
        System.out.println("Total booked: " + booked);
    }
}