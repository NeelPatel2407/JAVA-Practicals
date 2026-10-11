import java.util.ArrayList;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Product {
    private String name;
    private double price;

    Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class ProductService {

    private ArrayList<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        products.add(product);
    }

    public int getCount() {
        return products.size();
    }

    public Product getProduct(int index) {
        if (index < 0 || index >= products.size()) {
            throw new IllegalArgumentException("Invalid product index");
        }

        return products.get(index);
    }

    public void invalidAction() {
        throw new IllegalArgumentException("Invalid action");
    }
}

class ProductServiceTest {

    @Test
    void addingProductIncreasesCount() {
        ProductService service = new ProductService();

        assertEquals(0, service.getCount());

        service.addProduct(
                new Product("Laptop", 50000)
        );

        assertEquals(1, service.getCount());
    }

    @Test
    void addingTwoProductsIncreasesCount() {
        ProductService service = new ProductService();

        service.addProduct(
                new Product("Mouse", 800)
        );

        service.addProduct(
                new Product("Keyboard", 1500)
        );

        assertEquals(2, service.getCount());
    }

    @Test
    void invalidActionThrowsException() {
        ProductService service = new ProductService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.invalidAction()
        );
    }
}

public class P14_partA {

    public static void main(String[] args) {

        ProductService service = new ProductService();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n1. Add Product");
            System.out.println("2. Show Count");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    sc.nextLine();

                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();

                    service.addProduct(
                            new Product(name, price)
                    );

                    System.out.println("Product added.");
                    break;

                case 2:
                    System.out.println(
                            "Total products: " +
                            service.getCount()
                    );
                    break;

                case 3:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    throw new IllegalArgumentException(
                            "Invalid action"
                    );
            }
        }
    }
}