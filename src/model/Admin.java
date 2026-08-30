package model;

/**
 * Admin — a government officer who reviews and issues certificates.
 * Demonstrates INHERITANCE and POLYMORPHISM (method overriding).
 */
public class Admin extends User {

    public Admin() { super(); }

    public Admin(int userId, String username, String password,
                 String fullName, String email, String phone) {
        super(userId, username, password, fullName, email, phone);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public void showMenu() {
        System.out.println("\n============ ADMIN MENU ============");
        System.out.println("1. View All Pending Applications");
        System.out.println("2. View All Applications");
        System.out.println("3. Approve / Reject Application");
        System.out.println("4. View All Issued Certificates");
        System.out.println("5. Search Certificate by Number");
        System.out.println("6. Logout");
        System.out.println("====================================");
        System.out.print("Choose an option: ");
    }
}
