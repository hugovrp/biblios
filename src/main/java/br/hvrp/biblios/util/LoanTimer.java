package br.hvrp.biblios.util;

import java.util.List;
import br.hvrp.biblios.dao.LoanDAO;
import br.hvrp.biblios.model.Loan;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;

@Singleton
public class LoanTimer {

    // Roda todo dia às 08:00 da manhã
    @Schedule(hour = "08", minute = "00", second = "00", persistent = false)
    public void checkOverdueLoans() {
        LoanDAO dao = new LoanDAO();
        List<Loan> overdueList = dao.findOverdue();
        
        for(Loan loan : overdueList) {
            try {
                EmailUtil.sendOverdueNotice(
                    loan.getUser().getEmail(), 
                    loan.getUser().getName(), 
                    loan.getMagazine().getEdition().getSeries().getName()
                );
            } catch(Exception e) {
                System.err.println("Erro ao enviar e-mail para: " + loan.getUser().getEmail());
            }
        }
    }
}