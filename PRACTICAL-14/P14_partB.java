import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    String no,name;
    long balance;

    Account(String no,String name,long balance) {
        this.no=no;
        this.name=name;
        this.balance=balance;
    }

    synchronized void deposit(long n) {
        balance+=n;
    }

    synchronized void withdraw(long n) {
        if(n>balance) throw new BankException("Insufficient balance");
        balance-=n;
    }

    public String toString() {
        return no+" - "+name+" - Rs."+balance;
    }
}

class BankException extends RuntimeException {
    BankException(String msg) {
        super(msg);
    }
}

class Validator {
    static void amount(long n) {
        if(n<=0) throw new BankException("Invalid amount");
    }
}

class Repository<T> {
    HashMap<String,T> data=new HashMap<>();

    T save(String id,T obj) {
        data.put(id,obj);
        return obj;
    }

    T findById(String id) {
        return data.get(id);
    }

    ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }
}

class BankService {
    Repository<Account> repo;
    Path log;

    BankService(Repository<Account> repo,Path log) {
        this.repo=repo;
        this.log=log;
    }

    void openAccount(String no,String name,long balance) {
        Validator.amount(balance);
        if(repo.findById(no)!=null)
            throw new BankException("Account exists");

        repo.save(no,new Account(no,name,balance));
        write("DEPOSIT,"+balance);
    }

    void deposit(String no,long amount) {
        Validator.amount(amount);
        Account a=get(no);
        a.deposit(amount);
        write("DEPOSIT,"+amount);
    }

    void withdraw(String no,long amount) {
        Validator.amount(amount);
        Account a=get(no);
        a.withdraw(amount);
        write("WITHDRAW,"+amount);
    }

    void transfer(String from,String to,long amount) {
        Validator.amount(amount);
        Account a=get(from),b=get(to);

        synchronized(this) {
            a.withdraw(amount);
            b.deposit(amount);
        }

        write("WITHDRAW,"+amount);
        write("DEPOSIT,"+amount);
    }

    Account get(String no) {
        Account a=repo.findById(no);
        if(a==null) throw new BankException("Account not found");
        return a;
    }

    void statement(String no) {
        System.out.println(get(no));
    }

    void list() {
        for(Account a:repo.findAll())
            System.out.println(a);
    }

    void write(String s) {
        try {
            Files.writeString(log,s+"\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch(IOException e) {
            throw new BankException("Log error");
        }
    }
}

class TransactionProcessor {
    ConcurrentHashMap<String,Account> accounts=
            new ConcurrentHashMap<>();

    TransactionProcessor(ArrayList<Account> list) {
        for(Account a:list)
            accounts.put(a.no,a);
    }

    void deposit(String no,long amount) {
        accounts.get(no).deposit(amount);
    }
}

class ReportGenerator {
    static void report(Path folder) throws IOException {
        long dep=0,with=0;

        try(DirectoryStream<Path> files=
                Files.newDirectoryStream(folder)) {

            for(Path p:files) {
                if(!Files.isRegularFile(p)) continue;

                BasicFileAttributes a=
                    Files.readAttributes(p,
                    BasicFileAttributes.class);

                System.out.println(
                    p.getFileName()+" | "+
                    a.size()+" bytes | "+
                    a.lastModifiedTime());

                try(BufferedReader r=
                    Files.newBufferedReader(p)) {

                    String s;
                    while((s=r.readLine())!=null) {
                        String[] x=s.split(",");
                        if(x[0].equals("DEPOSIT"))
                            dep+=Long.parseLong(x[1]);
                        if(x[0].equals("WITHDRAW"))
                            with+=Long.parseLong(x[1]);
                    }
                }
            }
        }

        System.out.println("Total Deposits: "+dep);
        System.out.println("Total Withdrawals: "+with);
    }
}

public class P14_partB {

    public static void main(String[] args) throws Exception {

        Path state=Paths.get("bank.dat");
        Path folder=Paths.get("logs");
        Path log=folder.resolve("transactions.txt");

        Files.createDirectories(folder);

        Repository<Account> repo=new Repository<>();

        if(Files.exists(state)) {
            try(ObjectInputStream in=
                new ObjectInputStream(
                new FileInputStream(state.toFile()))) {

                ArrayList<Account> list=
                    (ArrayList<Account>)in.readObject();

                for(Account a:list)
                    repo.save(a.no,a);
            }
        }

        BankService bank=new BankService(repo,log);
        Scanner sc=new Scanner(System.in);

        while(true) {

            System.out.println(
                "\n1.Open  2.Deposit  3.Withdraw  "+
                "4.Transfer  5.Statement  6.List  "+
                "7.Report  8.Exit");

            int ch=sc.nextInt();

            try {
                if(ch==1) {
                    System.out.print("No Name Balance: ");
                    bank.openAccount(
                        sc.next(),sc.next(),sc.nextLong());
                }

                else if(ch==2) {
                    System.out.print("No Amount: ");
                    bank.deposit(sc.next(),sc.nextLong());
                }

                else if(ch==3) {
                    System.out.print("No Amount: ");
                    bank.withdraw(sc.next(),sc.nextLong());
                }

                else if(ch==4) {
                    System.out.print("From To Amount: ");
                    bank.transfer(
                        sc.next(),sc.next(),sc.nextLong());
                }

                else if(ch==5) {
                    bank.statement(sc.next());
                }

                else if(ch==6) {
                    bank.list();
                }

                else if(ch==7) {
                    ReportGenerator.report(folder);
                }

                else if(ch==8) {

                    try(ObjectOutputStream out=
                        new ObjectOutputStream(
                        new FileOutputStream(state.toFile()))) {

                        out.writeObject(repo.findAll());
                    }

                    System.out.println("Saved. Bye!");
                    break;
                }

                else {
                    throw new BankException("Invalid option");
                }

            } catch(BankException e) {
                System.out.println("Error: "+e.getMessage());
            }
        }

        sc.close();
    }
}