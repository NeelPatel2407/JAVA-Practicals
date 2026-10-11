import java.io.*;

class Student implements Serializable {
    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println(id + " " + name + " " + password);
    }
}

public class P11_partA_1 {
    public static void main(String[] args) {
        Student[] students = {
            new Student(1, "Neel", "abc123"),
            new Student(2, "Raj", "xyz456"),
            new Student(3, "Dev", "pass789")
        };

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("students.dat"));

            out.writeObject(students);
            out.close();

            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("students.dat"));

            Student[] loaded = (Student[]) in.readObject();
            in.close();

            System.out.println("Original Objects:");
            for (Student s : students) {
                s.display();
            }

            System.out.println("\nLoaded Objects:");
            for (Student s : loaded) {
                s.display();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
