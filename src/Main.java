import db.DatabaseConnection;
import model.*;
import service.ApplicationService;
import service.AuthService;
import service.CertificateService;
import util.InputHelper;

import java.util.List;

/**
 * Birth Certificate Application System — entry point.
 *
 * Demonstrates all four OOP pillars:
 *   Encapsulation — private fields with getters/setters (see model classes)
 *   Abstraction   — abstract User base class with abstract methods
 *   Inheritance   — Citizen and Admin extend User
 *   Polymorphism  — showMenu() and getRole() dispatched at runtime
 */
public class Main {

    private static final AuthService        authService = new AuthService();
    private static final ApplicationService appService  = new ApplicationService();
    private static final CertificateService certService = new CertificateService();

    public static void main(String[] args) {
        printBanner();
        try {
            mainLoop();
        } finally {
            DatabaseConnection.closeConnection();
            System.out.println("\nGoodbye.");
        }
    }

    private static void mainLoop() {
        while (true) {
            System.out.println("\n============ MAIN MENU ============");
            System.out.println("1. Login");
            System.out.println("2. Register (Citizen)");
            System.out.println("3. Exit");
            System.out.println("===================================");
            int choice = InputHelper.readInt("Choose an option: ");

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> { return; }
                default -> System.out.println(">> Invalid choice.");
            }
        }
    }

    // ---------- Auth ----------

    private static void register() {
        System.out.println("\n--- New Citizen Registration ---");
        String username = InputHelper.readNonEmpty("Username: ");
        String password = InputHelper.readNonEmpty("Password: ");
        String fullName = InputHelper.readNonEmpty("Full Name: ");
        String email    = InputHelper.readString("Email: ");
        String phone    = InputHelper.readString("Phone: ");

        int newId = authService.register(username, password, fullName, email, phone);
        if (newId > 0) System.out.println(">> Registered successfully. Your User ID: " + newId);
        else           System.out.println(">> Registration failed.");
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        String username = InputHelper.readNonEmpty("Username: ");
        String password = InputHelper.readNonEmpty("Password: ");

        User u = authService.login(username, password);
        if (u == null) {
            System.out.println(">> Invalid credentials.");
            return;
        }
        System.out.println(">> Welcome, " + u.getFullName() + " (" + u.getRole() + ")");

        // Polymorphic dispatch on User reference
        if (u instanceof Citizen c) citizenSession(c);
        else if (u instanceof Admin a) adminSession(a);
    }

    // ---------- Citizen ----------

    private static void citizenSession(Citizen c) {
        while (true) {
            c.showMenu();                              // polymorphic
            int choice = InputHelper.readInt("");
            switch (choice) {
                case 1 -> applyForCertificate(c);
                case 2 -> viewMyApplications(c);
                case 3 -> viewMyCertificate(c);
                case 4 -> updateProfile(c);
                case 5 -> { System.out.println(">> Logged out."); return; }
                default -> System.out.println(">> Invalid choice.");
            }
        }
    }

    private static void applyForCertificate(Citizen c) {
        System.out.println("\n--- New Birth Certificate Application ---");
        Application a = new Application();
        a.setUserId(c.getUserId());
        a.setChildName    (InputHelper.readNonEmpty("Child's Name: "));
        a.setGender       (InputHelper.readGender  ("Gender"));
        a.setDateOfBirth  (InputHelper.readDate    ("Date of Birth"));
        a.setPlaceOfBirth (InputHelper.readNonEmpty("Place of Birth: "));
        a.setFatherName   (InputHelper.readNonEmpty("Father's Name: "));
        a.setMotherName   (InputHelper.readNonEmpty("Mother's Name: "));
        a.setAddress      (InputHelper.readNonEmpty("Residential Address: "));

        int appId = appService.submit(a);
        if (appId > 0)
            System.out.println(">> Application submitted. Application ID: " + appId);
        else
            System.out.println(">> Submission failed.");
    }

    private static void viewMyApplications(Citizen c) {
        List<Application> list = appService.myApplications(c.getUserId());
        if (list.isEmpty()) { System.out.println(">> No applications yet."); return; }
        System.out.println("\n--- Your Applications ---");
        list.forEach(Application::display);
    }

    private static void viewMyCertificate(Citizen c) {
        Certificate cert = certService.forUser(c.getUserId());
        if (cert == null) { System.out.println(">> No issued certificate found."); return; }
        cert.printCertificate();
    }

    private static void updateProfile(Citizen c) {
        System.out.println("\n--- Update Profile ---");
        String fn = InputHelper.readString("Full Name [" + c.getFullName() + "]: ");
        String em = InputHelper.readString("Email [" + c.getEmail() + "]: ");
        String ph = InputHelper.readString("Phone [" + c.getPhone() + "]: ");
        if (!fn.isEmpty()) c.setFullName(fn);
        if (!em.isEmpty()) c.setEmail(em);
        if (!ph.isEmpty()) c.setPhone(ph);
        if (authService.updateProfile(c)) System.out.println(">> Profile updated.");
        else                              System.out.println(">> Update failed.");
    }

    // ---------- Admin ----------

    private static void adminSession(Admin a) {
        while (true) {
            a.showMenu();                              // polymorphic
            int choice = InputHelper.readInt("");
            switch (choice) {
                case 1 -> listApplications(appService.pendingApplications(), "Pending");
                case 2 -> listApplications(appService.allApplications(),     "All");
                case 3 -> processApplication(a);
                case 4 -> listCertificates();
                case 5 -> searchCertificate();
                case 6 -> { System.out.println(">> Logged out."); return; }
                default -> System.out.println(">> Invalid choice.");
            }
        }
    }

    private static void listApplications(List<Application> list, String label) {
        System.out.println("\n--- " + label + " Applications ---");
        if (list.isEmpty()) { System.out.println(">> None."); return; }
        list.forEach(Application::display);
    }

    private static void processApplication(Admin admin) {
        int id = InputHelper.readInt("Enter Application ID: ");
        Application a = appService.find(id);
        if (a == null) { System.out.println(">> Application not found."); return; }
        a.display();

        String action = InputHelper.readNonEmpty("Approve or Reject? (A/R): ").toUpperCase();
        String remarks = InputHelper.readString("Remarks: ");

        if (action.equals("A")) {
            String certNo = appService.approve(id, admin.getUserId(), remarks);
            if (certNo != null) System.out.println(">> Approved. Certificate No: " + certNo);
            else                System.out.println(">> Approval failed.");
        } else if (action.equals("R")) {
            if (appService.reject(id, remarks)) System.out.println(">> Application rejected.");
            else                                System.out.println(">> Rejection failed.");
        } else {
            System.out.println(">> Invalid action.");
        }
    }

    private static void listCertificates() {
        List<Certificate> list = certService.all();
        if (list.isEmpty()) { System.out.println(">> No certificates issued yet."); return; }
        list.forEach(Certificate::printCertificate);
    }

    private static void searchCertificate() {
        String no = InputHelper.readNonEmpty("Enter Certificate No: ");
        Certificate c = certService.byNumber(no);
        if (c == null) System.out.println(">> No certificate with that number.");
        else           c.printCertificate();
    }

    private static void printBanner() {
        System.out.println("====================================================");
        System.out.println("     BIRTH CERTIFICATE APPLICATION SYSTEM (BCAS)    ");
        System.out.println("           Core Java + Oracle DB (JDBC)             ");
        System.out.println("====================================================");
    }
}
