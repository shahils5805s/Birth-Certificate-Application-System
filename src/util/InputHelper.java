package util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static String readNonEmpty(String prompt) {
        while (true) {
            String s = readString(prompt);
            if (!s.isEmpty()) return s;
            System.out.println(">> Value cannot be empty.");
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readString(prompt));
            } catch (NumberFormatException e) {
                System.out.println(">> Please enter a valid integer.");
            }
        }
    }

    public static Date readDate(String prompt) {
        while (true) {
            String s = readString(prompt + " (YYYY-MM-DD): ");
            try {
                LocalDate d = LocalDate.parse(s, FMT);
                return Date.valueOf(d);
            } catch (DateTimeParseException e) {
                System.out.println(">> Invalid date format. Try again.");
            }
        }
    }

    public static String readGender(String prompt) {
        while (true) {
            String g = readNonEmpty(prompt + " (Male/Female/Other): ").toUpperCase();
            if (g.equals("MALE") || g.equals("FEMALE") || g.equals("OTHER"))
                return g.charAt(0) + g.substring(1).toLowerCase();
            System.out.println(">> Please enter Male, Female, or Other.");
        }
    }
}
