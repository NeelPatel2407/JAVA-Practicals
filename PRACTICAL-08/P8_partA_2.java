class OutOfStockException extends Exception {
    private int shortfall;

    OutOfStockException(int shortfall) {
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {
    private int stock;

    Warehouse(int stock) {
        this.stock = stock;
    }

    void issue(int qty) throws OutOfStockException, InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        if (qty > stock) {
            throw new OutOfStockException(qty - stock);
        }

        stock -= qty;
        System.out.println("Issued " + qty + " items. Remaining stock: " + stock);
    }
}

public class P8_partA_2 {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(50);

        int[] requests = {10, 20, 30, -5, 15, 5, 0, 10};

        for (int qty : requests) {
            try {
                System.out.println("Request: " + qty);
                warehouse.issue(qty);
            }
            catch (OutOfStockException e) {
                System.out.println("Failed: Out of stock. Shortfall: " + e.getShortfall());
            }
            catch (InvalidQuantityException e) {
                System.out.println("Failed: " + e.getMessage());
            }
        }
    }
}