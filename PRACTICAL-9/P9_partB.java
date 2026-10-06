class Account {
    private long balance;

    Account(long balance) {
        this.balance = balance;
    }

    void deposit(long amount) {
        long temp = balance;

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        balance = temp + amount;
    }

    synchronized void depositSafe(long amount) {
        long temp = balance;

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        balance = temp + amount;
    }

    synchronized void withdraw(long amount) {
        if(balance >= amount) {
            balance -= amount;
        }
    }

    long getBalance() {
        return balance;
    }
}

class AccountWorker implements Runnable {
    Account account;
    int times;
    long amount;
    boolean safe;

    AccountWorker(Account account, int times, long amount, boolean safe) {
        this.account = account;
        this.times = times;
        this.amount = amount;
        this.safe = safe;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");

        for(int i = 0; i < times; i++) {
            if(safe) {
                account.depositSafe(amount);
            } else {
                account.deposit(amount);
            }
        }

        System.out.println(Thread.currentThread().getName() + " finished");
    }
}

public class P9_partB {
    public static void main(String[] args) throws InterruptedException {

        int threadCount = 10;
        int times = 100;
        long amount = 100;

        long expected = threadCount * times * amount;

        System.out.println("===== WITHOUT SYNCHRONIZATION =====");

        Account account = new Account(0);
        Thread[] threads = new Thread[threadCount];

        for(int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(
                new AccountWorker(account, times, amount, false),
                "BankThread-" + (i + 1)
            );
            threads[i].start();
        }

        for(int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        System.out.println("Expected balance: " + expected);
        System.out.println("Actual balance: " + account.getBalance());

        System.out.println("\n===== WITH SYNCHRONIZATION =====");

        account = new Account(0);
        threads = new Thread[threadCount];

        for(int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(
                new AccountWorker(account, times, amount, true),
                "BankThread-" + (i + 1)
            );
            threads[i].start();
        }

        for(int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        System.out.println("Expected balance: " + expected);
        System.out.println("Actual balance: " + account.getBalance());
    }
}