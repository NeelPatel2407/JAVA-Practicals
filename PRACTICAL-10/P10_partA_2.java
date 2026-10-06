class Buffer {
    private int item;
    private boolean available = false;

    synchronized void produce(int value) {
        while(available) {
            try {
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        item = value;
        available = true;

        System.out.println("Produced: " + value);

        notify();
    }

    synchronized int consume() {
        while(!available) {
            try {
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }

        int value = item;
        available = false;

        System.out.println("Consumed: " + value);

        notify();

        return value;
    }
}

class Producer implements Runnable {
    private Buffer buffer;
    private int count;

    Producer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public void run() {
        for(int i = 1; i <= count; i++) {
            buffer.produce(i);
        }

        System.out.println("Producer finished");
    }
}

class Consumer implements Runnable {
    private Buffer buffer;
    private int count;

    Consumer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public void run() {
        for(int i = 1; i <= count; i++) {
            buffer.consume();
        }

        System.out.println("Consumer finished");
    }
}

public class P10_partA_2 {
    public static void main(String[] args) throws InterruptedException {

        Buffer buffer = new Buffer();

        int items = 10;

        Thread producer = new Thread(
            new Producer(buffer, items),
            "Producer"
        );

        Thread consumer = new Thread(
            new Consumer(buffer, items),
            "Consumer"
        );

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("All items produced and consumed successfully.");
    }
}