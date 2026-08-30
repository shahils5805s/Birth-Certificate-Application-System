package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Birth Certificate application submitted by a Citizen.
 * Demonstrates ENCAPSULATION.
 */
public class Application {

    private int applicationId;
    private int userId;
    private String childName;
    private String gender;
    private Date dateOfBirth;
    private String placeOfBirth;
    private String fatherName;
    private String motherName;
    private String address;
    private String status;        // PENDING / APPROVED / REJECTED
    private Timestamp submittedAt;
    private String remarks;

    public Application() { }

    // Full constructor (for reads from DB)
    public Application(int applicationId, int userId, String childName, String gender,
                       Date dateOfBirth, String placeOfBirth, String fatherName,
                       String motherName, String address, String status,
                       Timestamp submittedAt, String remarks) {
        this.applicationId = applicationId;
        this.userId        = userId;
        this.childName     = childName;
        this.gender        = gender;
        this.dateOfBirth   = dateOfBirth;
        this.placeOfBirth  = placeOfBirth;
        this.fatherName    = fatherName;
        this.motherName    = motherName;
        this.address       = address;
        this.status        = status;
        this.submittedAt   = submittedAt;
        this.remarks       = remarks;
    }

    // Getters / setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getChildName() { return childName; }
    public void setChildName(String childName) { this.childName = childName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Timestamp submittedAt) { this.submittedAt = submittedAt; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public void display() {
        System.out.println("--------------------------------------------------");
        System.out.println(" Application ID  : " + applicationId);
        System.out.println(" Child Name      : " + childName);
        System.out.println(" Gender          : " + gender);
        System.out.println(" Date of Birth   : " + dateOfBirth);
        System.out.println(" Place of Birth  : " + placeOfBirth);
        System.out.println(" Father's Name   : " + fatherName);
        System.out.println(" Mother's Name   : " + motherName);
        System.out.println(" Address         : " + address);
        System.out.println(" Status          : " + status);
        System.out.println(" Submitted At    : " + submittedAt);
        if (remarks != null && !remarks.isEmpty())
            System.out.println(" Remarks         : " + remarks);
        System.out.println("--------------------------------------------------");
    }
}
