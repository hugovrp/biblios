package br.hvrp.biblios.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *  Provedor de EntityManager para acesso ao banco de dados.
 *  Gerencia a criação e fornecimento de instâncias do EntityManager usando a unidade de persistência "biblios".
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
public class EntityManagerProvider {
	/**
	 *  Factory para criação de EntityManagers.
	 *  Inicializada com a unidade de persistência "biblios".
	 */
	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("biblios");

	/**
	 *  Cria e retorna uma nova instância de EntityManager.
	 * 
	 *  @return nova instância de EntityManager configurada
	 */
	public EntityManager getEntityManager() {
		EntityManager entityManager = factory.createEntityManager();
		return entityManager;
	}
}