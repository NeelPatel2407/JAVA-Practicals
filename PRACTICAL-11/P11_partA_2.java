import java.io.*;
import java.nio.file.*;

public class P11_partA_2 {
    public static void main(String[] args) throws IOException {
        Path file1 = Paths.get("log1.txt");
        Path file2 = Paths.get("log2.txt");
        Path file3 = Paths.get("log3.txt");

        Files.write(file1, java.util.Arrays.asList(
            "Server started successfully",
            "User login successful",
            "Database connection established"
        ));

        Files.write(file2, java.util.Arrays.asList(
            "User login failed",
            "Server running",
            "Connection timeout occurred"
        ));

        Files.write(file3, java.util.Arrays.asList(
            "Server stopped",
            "User login successful",
            "Database error found"
        ));

        Path[] files = {file1, file2, file3};

        String keyword = "login";
        int totalLines = 0;
        int keywordLines = 0;

        for (Path file : files) {
            int fileLines = 0;
            int fileKeywordLines = 0;

            for (String line : Files.readAllLines(file)) {
                fileLines++;
                totalLines++;

                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    keywordLines++;
                    fileKeywordLines++;
                }
            }

            long size = Files.size(file);

            System.out.println("File: " + file.getFileName());
            System.out.println("Size: " + size + " bytes");
            System.out.println("Lines: " + fileLines);
            System.out.println("Keyword lines: " + fileKeywordLines);
            System.out.println();
        }

        System.out.println("Total Lines: " + totalLines);
        System.out.println("Lines containing '" + keyword + "': " + keywordLines);
    }
}