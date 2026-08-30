package service;

import dao.ApplicationDAO;
import dao.CertificateDAO;
import model.Application;

import java.util.List;

public class ApplicationService {

    private final ApplicationDAO appDAO   = new ApplicationDAO();
    private final CertificateDAO certDAO  = new CertificateDAO();

    public int submit(Application a) {
        return appDAO.submitApplication(a);
    }

    public List<Application> myApplications(int userId)   { return appDAO.findByUser(userId); }
    public List<Application> allApplications()            { return appDAO.findAll(); }
    public List<Application> pendingApplications()        { return appDAO.findByStatus("PENDING"); }
    public Application       find(int applicationId)      { return appDAO.findById(applicationId); }

    /**
     * Approve an application → issues a certificate atomically at the service layer.
     * Returns the new certificate number, or null on failure.
     */
    public String approve(int applicationId, int adminId, String remarks) {
        Application a = appDAO.findById(applicationId);
        if (a == null) {
            System.out.println(">> Application not found.");
            return null;
        }
        if (!"PENDING".equals(a.getStatus())) {
            System.out.println(">> Application already processed. Current status: " + a.getStatus());
            return null;
        }
        boolean updated = appDAO.updateStatus(applicationId, "APPROVED", remarks);
        if (!updated) return null;
        return certDAO.issueCertificate(applicationId, adminId);
    }

    public boolean reject(int applicationId, String remarks) {
        Application a = appDAO.findById(applicationId);
        if (a == null || !"PENDING".equals(a.getStatus())) {
            System.out.println(">> Cannot reject: not found or already processed.");
            return false;
        }
        return appDAO.updateStatus(applicationId, "REJECTED", remarks);
    }
}
