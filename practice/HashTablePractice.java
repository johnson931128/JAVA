import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

public class HashTablePractice {
    Map<Integer, String> hashTable = new HashMap<>();
    Scanner scanner = new Scanner(System.in);

    // adding operations to the hash table
    public void addOperation() {
        System.out.print("Enter key: ");
        int key = scanner.nextInt();
        System.out.print("Enter value: ");
        String value = scanner.next();
        hashTable.put(key, value);
        System.out.println("Added (" + key + ", " + value + ") to the hash table.");
    }

    // removed operations from the hash table
    public void removeOperation() {
        System.out.print("Enter key to remove: ");
        int key = scanner.nextInt();
        if (hashTable.containsKey(key)) {
            hashTable.remove(key);
            System.out.println("Removed key " + key + " from the hash table.");
        } else {
            System.out.println("Key " + key + " not found in the hash table.");
        }
    }

    // search operations in the hash table
    public void searchOperation() {
        
    }


}
