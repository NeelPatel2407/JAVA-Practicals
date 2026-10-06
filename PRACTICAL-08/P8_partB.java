class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends BankException {
    private long shortfall;

    public InsufficientFundsException(long shortfall) {
        super("Insufficient funds");
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}

class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

class InvalidAmountException extends BankException {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class Account {
    private String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (amount > balance) {
            throw new InsufficientFundsException(amount - balance);
        }

        balance -= amount;
        System.out.println("Withdrawal successful: " + amount);
    }

    public void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }

        balance += amount;
        System.out.println("Deposit successful: " + amount);
    }

    public void transfer(Account to, long amount) throws BankException {
        try {
            withdraw(amount);
            to.deposit(amount);
            System.out.println("Transfer successful.");
        }
        catch (BankException e) {
            throw e;
        }
        finally {
            System.out.println("Transfer operation completed.");
        }
    }

    public long getBalance() {
        return balance;
    }
}

class BankResource implements AutoCloseable {
    public void open() {
        System.out.println("Bank resource opened.");
    }

    public void close() {
        System.out.println("Bank resource closed.");
    }
}

public class P8_partB {
    public static void main(String[] args) {

        Account a1 = new Account("A101", 100000);
        Account a2 = new Account("A102", 3000);

        try {
            a1.withdraw(7000);
        }
        catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed.");
            System.out.println("Shortfall: " + e.getShortfall());
        }
        catch (InvalidAmountException e) {
            System.out.println("Invalid withdrawal: " + e.getMessage());
        }
        finally {
            System.out.println("Withdrawal attempt finished.");
        }

        try {
            a1.deposit(-500);
        }
        catch (InvalidAmountException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
        finally {
            System.out.println("Deposit attempt finished.");
        }

        try {
            a1.transfer(a2, 1000);
        }
        catch (BankException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        finally {
            System.out.println("Transfer attempt finished.");
        }

        try (BankResource resource = new BankResource()) {
            resource.open();
            System.out.println("Using bank resource...");
        }

        System.out.println("Final balance A101: " + a1.getBalance());
        System.out.println("Final balance A102: " + a2.getBalance());
    }
}