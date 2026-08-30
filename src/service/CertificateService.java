package service;

import dao.CertificateDAO;
import model.Certificate;

import java.util.List;

public class CertificateService {

    private final CertificateDAO certDAO = new CertificateDAO();

    public Certificate forUser(int userId)               { return certDAO.findByUserId(userId); }
    public Certificate forApplication(int applicationId) { return certDAO.findByApplicationId(applicationId); }
    public Certificate byNumber(String certNo)           { return certDAO.findByCertificateNo(certNo); }
    public List<Certificate> all()                       { return certDAO.findAll(); }
}
