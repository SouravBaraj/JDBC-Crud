
import java.sql.*;
import java.util.Scanner;

public class Db {
    static final String URL = "jdbc:mysql://localhost:3306/crud_demo";
    static final String USER = "root"; // your MySQL username
    static final String PASSWORD = "sourav"; // your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- CRUD Menu ---");
            System.out.println("1. Create User");
            System.out.println("2. Read Users");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    createUser(name, email);
                    break;
                case 2:
                    readUsers();
                    break;
                case 3:
                    System.out.print("Enter user ID to update: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine();
                    updateUser(updateId, newName, newEmail);
                    break;
                case 4:
                    System.out.print("Enter user ID to delete: ");
                    int deleteId = sc.nextInt();
                    deleteUser(deleteId);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // CREATE
    public static void createUser(String name, String email) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); // <-- Load MySQL driver
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.executeUpdate();
        System.out.println("✅ User added successfully!");
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // READ
    public static void readUsers() {
        String sql = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- User List ---");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " +
                                   rs.getString("name") + " - " +
                                   rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public static void updateUser(int id, String name, String email) {
        String sql = "UPDATE users SET name=?, email=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ User updated successfully!");
            } else {
                System.out.println("⚠ No user found with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public static void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("⚠ No user found with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
