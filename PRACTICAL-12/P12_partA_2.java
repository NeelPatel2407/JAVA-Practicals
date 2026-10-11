import java.util.*;

public class P12_partA_2 {

    public static void main(String[] args) {

        ArrayList<String> logins = new ArrayList<>(Arrays.asList(
                "Neel", "Raj", "Amit", "Neel", "Dev", "Raj", "Karan"
        ));

        HashSet<String> uniqueUsers = new HashSet<>(logins);

        LinkedHashSet<String> firstSeen = new LinkedHashSet<>(logins);

        System.out.println("Unique users: " + uniqueUsers.size());

        System.out.println("First three distinct names:");

        int count = 0;

        for (String name : firstSeen) {
            System.out.println(name);
            count++;

            if (count == 3)
                break;
        }
    }
}