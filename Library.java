import java.io.*;
import java.util.Scanner;

class LibrarySystem {
    static void AddBook(int id, String bname, String author) {
        File f = new File("library.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f, true);
            fw.write(id + "," + bname + "," + author + "\n");
            fw.close();
            System.out.println("✅ Book added successfully!");
        } catch (IOException e) {
            System.out.println("Error in adding book: " + e.getMessage());
        }
    }

    static void viewBooks() {
        File f = new File("library.txt");
        if (!f.exists()) {
            System.out.println("No books found!");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            System.out.println("\nID | Book Name | Author");
            System.out.println("---------------------------");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println(parts[0] + " | " + parts[1] + " | " + parts[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    static void issueBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to issue: ");
        String bookId = sc.nextLine();
        System.out.print("Enter Member Name: ");
        String member = sc.nextLine();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader("library.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(bookId + ",")) {
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading library.txt");
            return;
        }

        if (!found) {
            System.out.println("Book ID not found!");
            return;
        }
        File issuedFile = new File("issued.txt");
        try {
            if (!issuedFile.exists()) {
                issuedFile.createNewFile();
            }
            try (BufferedReader br = new BufferedReader(new FileReader(issuedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(bookId + ",")) {
                        System.out.println("Book already issued!");
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking issued books: " + e.getMessage());
            return;
        }
        try (FileWriter fw = new FileWriter(issuedFile, true)) {
            fw.write(bookId + "," + member + "\n");
            System.out.println("✅ Book issued to " + member);
        } catch (IOException e) {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }

    static void returnBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to return: ");
        String bookId = sc.nextLine();

        File issuedFile = new File("issued.txt");
        if (!issuedFile.exists()) {
            System.out.println("No issued books found!");
            return;
        }

        try {
            File tempFile = new File("temp.txt");
            BufferedReader br = new BufferedReader(new FileReader(issuedFile));
            FileWriter fw = new FileWriter(tempFile);

            String line;
            boolean returned = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(bookId + ",")) {
                    returned = true; // skip this line
                } else {
                    fw.write(line + "\n");
                }
            }
            br.close();
            fw.close();
            if (issuedFile.delete()) {
                tempFile.renameTo(issuedFile);
            }

            if (returned) {
                System.out.println("✅ Book returned successfully!");
            } else {
                System.out.println("Book ID not found in issued books!");
            }

        } catch (IOException e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n******** WELCOME TO THE KALAM LIBRARY ********");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            option = s.nextInt();
            s.nextLine(); // consume newline

            switch (option) {
                case 1:
                    System.out.println("Enter Book ID, Book Name, Author Name:");
                    int id = s.nextInt();
                    s.nextLine();
                    String bname = s.nextLine();
                    String author = s.nextLine();
                    AddBook(id, bname, author);
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    issueBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } while (option != 5);

        s.close();
    }
}
