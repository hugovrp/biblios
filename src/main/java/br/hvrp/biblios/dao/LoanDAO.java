package br.hvrp.biblios.dao;

import java.util.List;

import br.hvrp.biblios.model.Loan;
import br.hvrp.biblios.model.User;
import br.hvrp.biblios.model.enums.LoanStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class LoanDAO extends DAO<Loan> {
    public LoanDAO() {
        super(Loan.class);
    }

    public boolean hasActiveLoan(User user) {
        EntityManager em = new EntityManagerProvider().getEntityManager();
        try {
            String jpql = "SELECT COUNT(l) FROM Loan l WHERE l.user = :user AND l.status = :status";
            
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("user", user);
            query.setParameter("status", LoanStatus.ACTIVE);
            
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
    
    public List<Loan> findByUser(User user) {
        EntityManager em = new EntityManagerProvider().getEntityManager();
        try {
            String jpql = "SELECT l FROM Loan l WHERE l.user = :user ORDER BY l.loanDate DESC";
            
            TypedQuery<Loan> query = em.createQuery(jpql, Loan.class);
            query.setParameter("user", user);
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Loan> findAllActive() {
        EntityManager em = new EntityManagerProvider().getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Loan l WHERE l.status != 'RETURNED' ORDER BY l.loanDate DESC", Loan.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Loan> findOverdue() {
        EntityManager em = new EntityManagerProvider().getEntityManager();
        try {
            String jpql = """
                SELECT l FROM Loan l 
                WHERE l.status = :status
            """;

            return em.createQuery(jpql, Loan.class)
                     .setParameter("status", LoanStatus.OVERDUE)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    
    public void updateOverdueLoans() {
        EntityManager em = new EntityManagerProvider().getEntityManager();
        try {
            em.getTransaction().begin();
            
            String jpql = "UPDATE Loan l SET l.status = :overdue " +
                          "WHERE l.status = :active " +
                          "AND l.expectedReturnDate < :now";
            
            int updated = em.createQuery(jpql)
                .setParameter("overdue", LoanStatus.OVERDUE)
                .setParameter("active", LoanStatus.ACTIVE)
                .setParameter("now", java.util.Calendar.getInstance())
                .executeUpdate();
            
            em.getTransaction().commit();
            
            System.out.println("Empréstimos atualizados para OVERDUE: " + updated);
        } catch(Exception e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}