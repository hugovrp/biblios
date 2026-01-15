package br.hvrp.biblios.util;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.hvrp.biblios.dao.LoanDAO;
import br.hvrp.biblios.model.Loan;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class LoanTimer implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                
            	LoanDAO dao = new LoanDAO();
        	    
        	    // Primeiro atualiza todos os empréstimos atrasados
        	    dao.updateOverdueLoans();
        	    
        	    // Depois busca e envia emails
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
        	            e.printStackTrace();
        	        }
        	    }
            	
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS); // executa a cada 1 hora
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}