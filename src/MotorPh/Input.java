package MotorPh;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Input {
    private static final String FILENAME = "MotorPH Inventory Data.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Global comparator keep track of the current sorting order.
    public static Comparator<Inventory> currentComparator = 
        Comparator.comparing(Inventory::getEngine, String.CASE_INSENSITIVE_ORDER);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Inventory> inventoryList = loadInventoryFromFile(FILENAME);
        int choice;

        do {
            displayMenu();
            choice = getUserChoice(scanner);
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    Insert.addItems(scanner, inventoryList);
                    break;
                case 2:
                    displayInventory(inventoryList);
                    break;
                case 3:
                    Search_Upd_Del.searchInventory(scanner, inventoryList);
                    break;
                case 4:
                    System.out.println("Choose sorting option: 1 for brand, 2 for engine, 3 for date");
                    int sortOption = getUserChoice(scanner);
                    Sort.sortInventory(inventoryList, sortOption);
                    break;
                case 5:
                    saveInventoryToFile(inventoryList, FILENAME);
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("1.Insert");
        System.out.println("2.Display");
        System.out.println("3.Search");
        System.out.println("4.Sort");
        System.out.println("5.Exit");
        System.out.print("Enter command: ");
    }

    private static int getUserChoice(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine(); 
            }
        }
    }
     //Display method
    private static void displayInventory(List<Inventory> inventoryList) {
        System.out.println("============================================================");
        System.out.printf("%-13s %-9s %-12s %-12s %-10s%n", "Date", "Stock", "Brand", "Engine", "Status");
        System.out.println("============================================================");
        for (Inventory item : inventoryList) {
            System.out.println(item);
        }
        System.out.println("============================================================");
    }

    public static List<Inventory> loadInventoryFromFile(String filename) {
        List<Inventory> inventoryList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            if ((line = br.readLine()) != null) {
                System.out.println("Skipping first line: ");
            }
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        Inventory inventory = Inventory.fromFileString(line);
                        inventoryList.add(inventory);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }
            System.out.println("Inventory loaded successfully from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename + ". Starting with an empty list.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return inventoryList;
    }

    public static void saveInventoryToFile(List<Inventory> inventoryList, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Inventory item : inventoryList) {
                bw.write(String.format("%s,%b,%s,%s,%b",
                    DATE_FORMAT.format(item.getDate()), item.getStock(), item.getBrand(), item.getEngine(), item.getStatus()));
                bw.newLine();
            }
            System.out.println("Inventory saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
