import java.util.*;

public class P12_partA_1 {

    public static void main(String[] args) {

        String paragraph = "java is easy and java is powerful and java is popular";

        String[] words = paragraph.toLowerCase()
                .replaceAll("[^a-z ]", "")
                .split("\\s+");

        HashMap<String, Integer> frequency = new HashMap<>();

        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        ArrayList<Map.Entry<String, Integer>> list =
                new ArrayList<>(frequency.entrySet());

        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a,
                               Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });

        System.out.println("Words sorted by frequency:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}