class Box<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {

    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class P13_partA_1 {

    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        Box<Integer> numberBox = new Box<>();
        numberBox.set(100);

        Box<String> textBox = new Box<>();
        textBox.set("Hello Java");

        System.out.println("Integer Box: " + numberBox.get());
        System.out.println("String Box: " + textBox.get());

        Pair<String, Integer> student =
                new Pair<>("Neel", 21);

        Pair<Integer, String> product =
                new Pair<>(101, "Laptop");

        System.out.println("\nStudent Pair:");
        System.out.println("Name: " + student.getFirst());
        System.out.println("Age: " + student.getSecond());

        System.out.println("\nProduct Pair:");
        System.out.println("ID: " + product.getFirst());
        System.out.println("Name: " + product.getSecond());

        String[] names = {
                "Neel", "Raj", "Amit"
        };

        Integer[] numbers = {
                10, 20, 30
        };

        System.out.println("\nFirst element of String array: "
                + firstElement(names));

        System.out.println("First element of Integer array: "
                + firstElement(numbers));
    }
}