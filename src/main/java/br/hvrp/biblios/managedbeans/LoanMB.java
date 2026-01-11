package br.hvrp.biblios.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import br.hvrp.biblios.dao.DAO;
import br.hvrp.biblios.dao.LoanDAO;
import br.hvrp.biblios.model.Loan;
import br.hvrp.biblios.model.Magazine;
import br.hvrp.biblios.model.User;
import br.hvrp.biblios.model.enums.AvailabilityStatus;
import br.hvrp.biblios.model.enums.LoanStatus;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ViewScoped
@Named("loanMB")
public class LoanMB extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LoanDAO loanDAO = new LoanDAO();
    private DAO<Magazine> magazineDAO = new DAO<>(Magazine.class);
    
    private List<Loan> userLoans;
    private List<Loan> activeLoans; 
    private List<Loan> filteredLoans; 
    
    private String reportType; 
    private User selectedUserForReport;

    @Inject
    private UserMB userMB;

    private List<Magazine> availableMagazines;

    public void requestLoan(Magazine magazine) {
        User user = userMB.getLoggedUser();

        try {
            if(user == null) {
                showError(ERROR, "Você precisa estar logado para solicitar um empréstimo.");
                return;
            }

            if(loanDAO.hasActiveLoan(user)) {
                showError(LIMIT, "Você já possui um empréstimo ativo. Devolva a revista atual para solicitar outra.");
                return;
            }

            Magazine freshMagazine = magazineDAO.findById(magazine.getId());
            if(freshMagazine.getStatus() != AvailabilityStatus.AVAILABLE) {
                showError(UNAVAILABLE, "Esta revista acabou de ser reservada ou emprestada.");
                availableMagazines = null; 
                return;
            }

            // Processa o Empréstimo
            Loan newLoan = new Loan();
            newLoan.setUser(user);
            newLoan.setMagazine(freshMagazine);
            newLoan.setStatus(LoanStatus.ACTIVE);

            // Atualiza status
            freshMagazine.setStatus(AvailabilityStatus.BORROWED);
            
            magazineDAO.alter(freshMagazine);
            loanDAO.insert(newLoan);

            showInfo(SUCCESS, "Empréstimo realizado. Devolução prevista para: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(newLoan.getExpectedReturnDate().getTime()));
            
            availableMagazines = null; 
        } catch(Exception e) {
            showError(ERROR, "Não foi possível processar o empréstimo: " + e.getMessage());
        }
    }
    
    public void returnMagazine(Loan loan) {
        try {
            loan.setStatus(LoanStatus.RETURNED);
            loan.setActualReturnDate(java.util.Calendar.getInstance());
            
            Magazine mag = loan.getMagazine();
            mag.setStatus(AvailabilityStatus.AVAILABLE);
            
            magazineDAO.alter(mag);
            loanDAO.alter(loan);
            
            showInfo(SUCCESS, "Devolução registrada.");
            
            this.activeLoans = null;       
            this.availableMagazines = null; 
            this.userLoans = null;        
            this.filteredLoans = null;    
            
        } catch(Exception e) {
            showError(ERROR, "Falha ao registrar devolução.");
        }
    }
    
    public void generateReport() {
        if(reportType == null) return;
        
        switch(reportType) {
            case "ALL_BORROWED":
                filteredLoans = loanDAO.findAllActive();
                break;
            case "OVERDUE":
                filteredLoans = loanDAO.findOverdue();
                break;
            case "USER":
                if(selectedUserForReport != null) {
                    filteredLoans = loanDAO.findByUser(selectedUserForReport);
                }
                break;
        }
    }

    public List<Loan> getActiveLoans() {
        if(activeLoans == null) {
            activeLoans = loanDAO.findAllActive();
        }
        return activeLoans;
    }

    public List<Loan> getUserLoans() {
        if(userLoans == null && userMB.getLoggedUser() != null) {
            userLoans = loanDAO.findByUser(userMB.getLoggedUser());
        }
        return userLoans;
    }

    public List<Magazine> getAvailableMagazines() {
        if(availableMagazines == null) {
            availableMagazines = magazineDAO.listAll().stream().filter(m -> m.getStatus() == AvailabilityStatus.AVAILABLE).collect(Collectors.toList());
        }
        return availableMagazines;
    }

    public String getStatusLabel(String status) {
    	if(status == null) return "";
        switch(status) {
            case "ACTIVE": return "Ativo";
            case "OVERDUE": return "Atrasado";
            case "RETURNED": return "Devolvido";
            case "CANCELLED": return "Cancelado";
            default: return status;
        }
    }
    
    public List<Loan> getFilteredLoans() { 
    	return filteredLoans; 
    }
    
    public String getReportType() { 
    	return reportType; 
    }
    
    public void setReportType(String reportType) { 
    	this.reportType = reportType; 
    }
    
    public User getSelectedUserForReport() { 
    	return selectedUserForReport; 
    }
    
    public void setSelectedUserForReport(User selectedUserForReport) { 
    	this.selectedUserForReport = selectedUserForReport; 
    }
}