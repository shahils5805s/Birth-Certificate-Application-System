package model;

import java.sql.Timestamp;

/**
 * Birth Certificate issued after admin approval.
 */
public class Certificate {

    private int certificateId;
    private String certificateNo;
    private int applicationId;
    private int issuedBy;
    private Timestamp issuedAt;

    // Joined fields (populated for display)
    private String childName;
    private String fatherName;
    private String motherName;
    private String dateOfBirth;
    private String placeOfBirth;

    public Certificate() { }

    public int getCertificateId() { return certificateId; }
    public void setCertificateId(int certificateId) { this.certificateId = certificateId; }

    public String getCertificateNo() { return certificateNo; }
    public void setCertificateNo(String certificateNo) { this.certificateNo = certificateNo; }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getIssuedBy() { return issuedBy; }
    public void setIssuedBy(int issuedBy) { this.issuedBy = issuedBy; }

    public Timestamp getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Timestamp issuedAt) { this.issuedAt = issuedAt; }

    public String getChildName() { return childName; }
    public void setChildName(String childName) { this.childName = childName; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public void printCertificate() {
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("*            BIRTH CERTIFICATE (OFFICIAL)        *");
        System.out.println("**************************************************");
        System.out.println(" Certificate No. : " + certificateNo);
        System.out.println(" Issued On       : " + issuedAt);
        System.out.println("--------------------------------------------------");
        System.out.println(" Name of Child   : " + childName);
        System.out.println(" Date of Birth   : " + dateOfBirth);
        System.out.println(" Place of Birth  : " + placeOfBirth);
        System.out.println(" Father's Name   : " + fatherName);
        System.out.println(" Mother's Name   : " + motherName);
        System.out.println("--------------------------------------------------");
        System.out.println("        Registrar, Office of Birth Records        ");
        System.out.println("**************************************************\n");
    }
}
