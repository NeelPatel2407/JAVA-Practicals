import java.util.concurrent.*;

class Account {
    int accountNumber;
    int balance;

    Account(int accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    synchronized void deposit(int amount) {
        balance += amount;
    }

    synchronized void withdraw(int amount) {
        if(balance >= amount)
            balance -= amount;
    }
}

class TransactionProcessor {
    ExecutorService pool = Executors.newFixedThreadPool(4);

    void submit(Runnable task) {
        pool.execute(task);
    }

    void stop() {
        pool.shutdown();

        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Buffer {
    int[] data = new int[3];
    int count = 0;

    synchronized void add(int value) {
        while(count == 3) {
            try {
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        data[count++] = value;
        System.out.println("Produced: " + value);
        notify();
    }

    synchronized int remove() {
        while(count == 0) {
            try {
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int value = data[0];

        for(int i = 1; i < count; i++)
            data[i - 1] = data[i];

        count--;

        System.out.println("Consumed: " + value);
        notify();

        return value;
    }
}

public class P10_partB {

    static void transfer(Account a, Account b, int amount) {

        Account first;
        Account second;

        if(a.accountNumber < b.accountNumber) {
            first = a;
            second = b;
        } else {
            first = b;
            second = a;
        }

        synchronized(first) {
            synchronized(second) {
                a.withdraw(amount);
                b.deposit(amount);

                System.out.println(
                    Thread.currentThread().getName()
                    + " transferred " + amount
                );
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== THREAD POOL ===");

        Account account = new Account(1, 1000);
        TransactionProcessor processor = new TransactionProcessor();

        for(int i = 1; i <= 10; i++) {
            final int n = i;

            processor.submit(() -> {
                if(n % 2 == 0)
                    account.deposit(100);
                else
                    account.withdraw(50);

                System.out.println(
                    Thread.currentThread().getName()
                    + " completed transaction"
                );
            });
        }

        processor.stop();

        System.out.println("Final balance: " + account.balance);


        System.out.println("\n=== PRODUCER CONSUMER ===");

        Buffer buffer = new Buffer();

        Thread producer = new Thread(() -> {
            for(int i = 1; i <= 10; i++)
                buffer.add(i);
        });

        Thread consumer = new Thread(() -> {
            for(int i = 1; i <= 10; i++)
                buffer.remove();
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();


        System.out.println("\n=== TRANSFER ===");

        Account a = new Account(10, 1000);
        Account b = new Account(20, 1000);

        Thread t1 = new Thread(() -> {
            transfer(a, b, 100);
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            transfer(b, a, 200);
        }, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Account 10: " + a.balance);
        System.out.println("Account 20: " + b.balance);

        System.out.println("\nAll tasks completed.");
    }
}