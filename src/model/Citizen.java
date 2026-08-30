package model;

/**
 * Citizen — a registered public user who can submit and track applications.
 * Demonstrates INHERITANCE (extends User) and POLYMORPHISM (overrides
 * getRole() and showMenu()).
 */
public class Citizen extends User {

    public Citizen() { super(); }

    public Citizen(int userId, String username, String password,
                   String fullName, String email, String phone) {
        super(userId, username, password, fullName, email, phone);
    }

    @Override
    public String getRole() {
        return "CITIZEN";
    }

    @Override
    public void showMenu() {
        System.out.println("\n=========== CITIZEN MENU ===========");
        System.out.println("1. Apply for Birth Certificate");
        System.out.println("2. View My Applications");
        System.out.println("3. View My Certificate");
        System.out.println("4. Update Profile");
        System.out.println("5. Logout");
        System.out.println("====================================");
        System.out.print("Choose an option: ");
    }
}
