package org.project.company.greenhomes.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.project.company.greenhomes.dao.GenericDAO;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * See <a href="http://www.hibernate.org/328.html">Hibernate's GenericDAO</a>
 *
 * @author fmacderm
 */
@Repository // to use Spring's exception hierarchy

public class GenericDAOImpl<T, ID extends Serializable> extends HibernateDaoSupport
		implements GenericDAO<T, ID>, InitializingBean {

	//////////////////////////////////////////////////////////////////
	// Instance variables
	//////////////////////////////////////////////////////////////////

	private Class<T> persistentClass;

	//////////////////////////////////////////////////////////////////
	// Constructors
	//////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public GenericDAOImpl () {
		this.persistentClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	//////////////////////////////////////////////////////////////////
	// Business methods
	//////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public T findById (ID id, boolean lock) throws DataAccessException {
		T entity;
		if (lock) {
			entity = (T)getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
		} else {
			entity = (T)getSession().load(getPersistentClass(), id);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<T> findAll () throws DataAccessException {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<T> findByExample (T exampleInstance, String[] excludeProperty) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public T save (T entity) throws DataAccessException {
		T storedEntity = (T)getSession().merge(entity);
		flush();
		return storedEntity;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete (T entity) throws DataAccessException {
		getSession().delete(entity);
		flush();
	}

	/**
	 * When flush() is subsequently called, the state of that object will be synchronized with the database.
	 *
	 * @throws DataAccessException
	 */
	public void flush () throws DataAccessException {
		getSession().flush();
	}

	/**
	 * To completely evict all objects from the session cache, calls Session.clear()
	 *
	 * @throws DataAccessException
	 */
	public void clear () throws DataAccessException {
		getSession().clear();
	}

	//////////////////////////////////////////////////////////////////
	// Protected methods
	//////////////////////////////////////////////////////////////////

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria (Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

	//////////////////////////////////////////////////////////////////
	// Property methods
	//////////////////////////////////////////////////////////////////

	public Class<T> getPersistentClass () {
		return persistentClass;
	}

}
