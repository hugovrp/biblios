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
            String jpql = "SELECT l FROM Loan l WHERE l.status = :status AND l.expectedReturnDate < :now";
            
            return em.createQuery(jpql, Loan.class).setParameter("status", LoanStatus.ACTIVE).setParameter("now", java.util.Calendar.getInstance()).getResultList();
        } finally { em.close(); }
    }
}