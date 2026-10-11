import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

class Account {

    @Id
    private String accountNumber;

    private String name;
    private double balance;

    Account(String accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public String toString() {
        return accountNumber + " - " + name + " - Rs." + balance;
    }
}

class Repository<T> {

    private HashMap<String, T> data = new HashMap<>();

    public T save(T entity) {
        try {
            Field idField = null;

            Class<?> current = entity.getClass();

            while (current != null) {
                for (Field field : current.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        idField = field;
                        break;
                    }
                }

                if (idField != null)
                    break;

                current = current.getSuperclass();
            }

            if (idField == null)
                throw new RuntimeException("No @Id field found");

            idField.setAccessible(true);

            String id = String.valueOf(idField.get(entity));

            data.put(id, entity);

            return entity;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T findById(String id) {
        return data.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(String id) {
        return data.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public static <N extends Number> double average(N[] values) {

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }
}

public class P13_partB {

    public static void main(String[] args) {

        Repository<Account> repository =
                new Repository<>();

        Account a1 =
                new Account("A101", "Neel", 25000);

        Account a2 =
                new Account("A102", "Raj", 15000);

        Account a3 =
                new Account("A103", "Amit", 40000);

        repository.saveAll(a1, a2, a3);

        System.out.println("Account with ID A102:");

        Account account =
                repository.findById("A102");

        System.out.println(account);

        System.out.println("\nAll Accounts:");

        for (Account a : repository.findAll()) {
            System.out.println(a);
        }

        System.out.println("\nAverage:");

        Integer[] numbers = {10, 20, 30, 40};

        System.out.println(
                Repository.average(numbers)
        );

        System.out.println("\nDelete A101:");

        System.out.println(
                repository.delete("A101")
        );

        System.out.println("\nAccounts after deletion:");

        for (Account a : repository.findAll()) {
            System.out.println(a);
        }
    }
}