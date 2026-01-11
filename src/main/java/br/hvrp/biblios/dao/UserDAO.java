package br.hvrp.biblios.dao;

import br.hvrp.biblios.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserDAO extends DAO<User> {

	public UserDAO() {
		super(User.class);
	}
	
	public User findByLogin(String login) {
		EntityManager em = new EntityManagerProvider().getEntityManager();
		
		try {
			return em.createQuery("SELECT u FROM User u where u.login = :login", User.class).setParameter("login", login).getSingleResult();
		} catch(NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
	
	public User findByEmail(String email) {
	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    
	    try {
	        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
	    } catch(NoResultException e) {
	        return null;
	    } finally {
	        em.close();
	    }
	}
	
	public User findByToken(String token) {
		EntityManager em = new EntityManagerProvider().getEntityManager();
		
		try {
			return em.createQuery("SELECT u FROM User u where u.confirmationToken = :token", User.class).setParameter("token", token).getSingleResult();
		} catch(NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}
	
}
