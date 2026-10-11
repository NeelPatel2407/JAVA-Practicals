import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Account implements Comparable<Account> {

    private String accountNumber;
    private String name;
    private long balance;

    Account(String accountNumber, String name, long balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " - " + name + " - Rs." + balance;
    }
}

class AccountStore {

    private HashMap<String, Account> accounts;
    private ArrayList<String> transactionHistory;

    AccountStore() {
        accounts = new HashMap<>();
        transactionHistory = new ArrayList<>();
    }

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        transactionHistory.add(
                "Account added: " + account.getAccountNumber()
        );
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<Account> sortedByBalance() {

        ArrayList<Account> list =
                new ArrayList<>(accounts.values());

        list.sort(new Comparator<Account>() {
            @Override
            public int compare(Account a, Account b) {
                return Long.compare(
                        a.getBalance(),
                        b.getBalance()
                );
            }
        });

        return list;
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }
}

class TransactionProcessor {

    private ConcurrentHashMap<String, Account> accounts;

    TransactionProcessor() {
        accounts = new ConcurrentHashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(
                account.getAccountNumber(),
                account
        );
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void showAccounts() {
        for (Account account : accounts.values()) {
            System.out.println(account);
        }
    }
}

public class P12_partB {

    public static void main(String[] args) {

        AccountStore store = new AccountStore();

        store.add(new Account("A103", "Neel", 25000));
        store.add(new Account("A101", "Raj", 10000));
        store.add(new Account("A105", "Amit", 50000));
        store.add(new Account("A102", "Dev", 15000));

        System.out.println("Accounts sorted by balance:");

        ArrayList<Account> sorted =
                store.sortedByBalance();

        for (Account account : sorted) {
            System.out.println(account);
        }

        System.out.println("\nSearching for A103:");

        Account account = store.get("A103");

        if (account != null) {
            System.out.println(account);
        } else {
            System.out.println("Account not found");
        }

        TransactionProcessor processor =
                new TransactionProcessor();

        for (Account a : store.getAll()) {
            processor.addAccount(a);
        }

        System.out.println("\nAccounts in ConcurrentHashMap:");

        processor.showAccounts();

        System.out.println("\nTransaction History:");

        for (String transaction :
                store.getTransactionHistory()) {

            System.out.println(transaction);
        }
    }
}