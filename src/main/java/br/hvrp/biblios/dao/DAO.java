package br.hvrp.biblios.dao;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 *  Data Access Object genérico para operações CRUD.
 *  Implementa operações básicas de banco de dados (criar, ler, atualizar, deletar) para qualquer entidade JPA.
 * 
 *  @param <T> tipo da entidade que este DAO irá gerenciar
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
public class DAO<T> {
	/**
	 *  Classe da entidade que este DAO gerencia.
	 */
	private Class<T> entityClass;
	
	/**
	 *  Construtor que inicializa o DAO com a classe da entidade.
	 * 
	 *  @param entityClass classe da entidade a ser gerenciada
	 */
	public DAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 *  Insere uma nova entidade no banco de dados.
	 *  Utiliza merge para permitir tanto inserção quanto atualização.
	 * 
	 *  @param entity entidade a ser inserida
	 */
	public void insert(T entity) {
	    EntityManager entityManager = new EntityManagerProvider().getEntityManager();
	    try {
	        entityManager.getTransaction().begin();
	        entityManager.persist(entity); 
	        entityManager.getTransaction().commit();
	    } catch (Exception e) {
	        if (entityManager.getTransaction().isActive()) {
	            entityManager.getTransaction().rollback();
	        }
	        throw e;
	    } finally {
	        entityManager.close(); 
	    }
	}
	
	/**
	 *  Atualiza uma entidade existente no banco de dados.
	 * 
	 *  @param entity entidade a ser atualizada
	 */
	public void alter(T entity) {
		EntityManager entityManager = new EntityManagerProvider().getEntityManager();
		
		entityManager.getTransaction().begin();
		entityManager.merge(entity);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	/**
	 *  Remove uma entidade do banco de dados.
	 *  Realiza merge antes da remoção para garantir que a entidade está gerenciada.
	 *  Inclui tratamento de erro com rollback em caso de exceção.
	 * 
	 *  @param entity entidade a ser removida
	 *  @throws Exception se ocorrer erro durante a remoção
	 */
	public void remove(T entity) {
	    EntityManager entityManager = new EntityManagerProvider().getEntityManager();
	    try {
	        entityManager.getTransaction().begin();
	   
	        entityManager.remove(entityManager.merge(entity)); 
	        
	        entityManager.getTransaction().commit();
	    } catch (Exception e) {
	        if (entityManager.getTransaction().isActive()) {
	            entityManager.getTransaction().rollback();
	        }
	        throw e;
	    } finally {
	        entityManager.close();
	    }
	}
	
	/**
	 *  Lista todas as entidades do tipo gerenciado por este DAO.
	 *  Utiliza Criteria API para criar uma consulta genérica.
	 * 
	 *  @return lista contendo todas as entidades encontradas
	 */
	public List<T> listAll() {
		EntityManager entityManager = new EntityManagerProvider().getEntityManager();
		
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
		query.select(query.from(entityClass));
		List<T> list = entityManager.createQuery(query).getResultList();
		
		return list;
	}
	
	/**
	 *  Busca uma entidade pelo seu ID.
	 * 
	 *  @param id identificador único da entidade
	 *  @return entidade encontrada ou null se não existir
	 */
	public T findById(int id) {
	    EntityManager entityManager = new EntityManagerProvider().getEntityManager();
	    T entity = entityManager.find(entityClass, id);
	    entityManager.close();
	    return entity;
	}
	
}