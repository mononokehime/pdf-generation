package org.project.company.greenhomes.dao;

import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.List;

/**
 * See <a href="http://www.hibernate.org/328.html">Hibernate's GenericDAO</a>
 *
 * @author fmacderm
 */
public interface GenericDAO<T, ID extends Serializable> {

	/**
	 * Find entity by ID
	 *
	 * @param id   Entity Identifier
	 * @param lock If true, lock the entity during the operation
	 * @return Found entity or <code>null</code> if the entity doesn't exist
	 * @throws DataAccessException      In a case of persistent layer exception. Unchecked.
	 * @throws IllegalArgumentException If id is <code>null</code>. Unchecked.
	 */
	public T findById (ID id, boolean lock) throws DataAccessException;

	/**
	 * Returns list of all entities
	 */
	public List<T> findAll () throws DataAccessException;

	public List<T> findByExample (T exampleInstance, String[] excludeProperty) throws DataAccessException;

	/**
	 * Persists the entity into the underlying DB repository
	 *
	 * @param entity Entity to persist
	 * @return Persisted instance
	 */
	public T save (T entity) throws DataAccessException;

	/**
	 * Removes the entity from the underlying DB repository
	 *
	 * @param entity Entity to remove
	 */
	public void delete (T entity) throws DataAccessException;

}
