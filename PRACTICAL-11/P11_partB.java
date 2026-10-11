import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int accountNumber;
    protected String holderName;
    protected long balance;

    Account(int accountNumber, String holderName, long balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String toString() {
        return "Account No: " + accountNumber +
               ", Holder: " + holderName +
               ", Balance: " + balance;
    }
}

class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    SavingsAccount(int accountNumber, String holderName, long balance) {
        super(accountNumber, holderName, balance);
    }
}

class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;

    CurrentAccount(int accountNumber, String holderName, long balance) {
        super(accountNumber, holderName, balance);
    }
}

class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    int id;
    String name;

    Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class StatePersister {

    public static void save(Account[] accounts, Path file)
            throws IOException {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(file.toFile()))) {

            out.writeObject(accounts);
        }
    }

    public static Account[] load(Path file)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(file.toFile()))) {

            return (Account[]) in.readObject();
        }
    }
}

class TransactionLog {

    public static void append(Path file, String line)
            throws IOException {

        Files.write(
                file,
                (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}

class ReportGenerator {

    public static void generate(Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder report = new StringBuilder();

        report.append("END OF DAY REPORT\n");
        report.append("=================\n\n");

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(logsFolder)) {

            for (Path file : stream) {

                if (!Files.isRegularFile(file))
                    continue;

                BasicFileAttributes attr =
                        Files.readAttributes(
                                file,
                                BasicFileAttributes.class
                        );

                report.append("File: ")
                      .append(file.getFileName())
                      .append("\n");

                report.append("Size: ")
                      .append(attr.size())
                      .append(" bytes\n");

                report.append("Last Modified: ")
                      .append(attr.lastModifiedTime())
                      .append("\n");

                try (BufferedReader reader =
                             Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] parts = line.split(",");

                        if (parts[0].equals("DEPOSIT")) {
                            totalDeposits +=
                                    Long.parseLong(parts[1]);
                        }

                        if (parts[0].equals("WITHDRAW")) {
                            totalWithdrawals +=
                                    Long.parseLong(parts[1]);
                        }
                    }
                }

                report.append("\n");
            }
        }

        report.append("TOTAL DEPOSITS: ")
              .append(totalDeposits)
              .append("\n");

        report.append("TOTAL WITHDRAWALS: ")
              .append(totalWithdrawals)
              .append("\n");

        Files.write(
                reportFile,
                report.toString().getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}

public class P11_partB {

    public static void main(String[] args) throws Exception {

        Path stateFile = Paths.get("bank.dat");
        Path logsFolder = Paths.get("logs");
        Path reportFile = Paths.get("report.txt");

        Files.createDirectories(logsFolder);

        Account[] accounts = {
                new SavingsAccount(101, "Neel", 10000),
                new CurrentAccount(102, "Raj", 15000)
        };

        TransactionLog.append(
                logsFolder.resolve("transactions.txt"),
                "DEPOSIT,5000"
        );

        TransactionLog.append(
                logsFolder.resolve("transactions.txt"),
                "WITHDRAW,2000"
        );

        TransactionLog.append(
                logsFolder.resolve("transactions.txt"),
                "DEPOSIT,3000"
        );

        StatePersister.save(accounts, stateFile);

        Account[] loadedAccounts =
                StatePersister.load(stateFile);

        System.out.println("Accounts after loading:");

        for (Account account : loadedAccounts) {
            System.out.println(account);
        }

        ReportGenerator.generate(
                logsFolder,
                reportFile
        );

        System.out.println("\nEnd of Day Report:");
        System.out.println(
                Files.readString(reportFile)
        );
    }
}