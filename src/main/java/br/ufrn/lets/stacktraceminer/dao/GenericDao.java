package br.ufrn.lets.stacktraceminer.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.ufrn.lets.stacktraceminer.model.IdEntity;

/**
 * Generic operations to persistence and search an entity.
 * Is necessary to override the method getSession to load the session based on hibernate configurations. 
 * @author Teresa Fernandes
 */
public abstract class GenericDao<T extends IdEntity> implements IGenericDao<T>, Serializable {

	private static final long serialVersionUID = 1L;
	private Session session;

	public GenericDao() {}

	public void saveUpdate(T entity) throws HibernateException{
		try {
			session = getSession();
			session.beginTransaction();
			session.saveOrUpdate(entity);
			session.getTransaction().commit();
		}catch (HibernateException e){
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	public T save(T entity) throws HibernateException{
		try {
			session = getSession();
			session.beginTransaction();
			entity.setId((Long) session.save(entity));
			session.getTransaction().commit();
		}catch (HibernateException e){
			session.getTransaction().rollback();
			throw e;
		}
		return entity;
	}

	public void delete(T entity) throws HibernateException{
		try {
			session = getSession();
			session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();
		}catch (HibernateException e){
			session.getTransaction().rollback();
			throw e;
		}
	}

	public void update(T entity) throws HibernateException{
		try {
			session = getSession();
			session.beginTransaction();
			session.saveOrUpdate(entity);
			session.getTransaction().commit();
		}catch (HibernateException e){
			session.getTransaction().rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> persistentClass) {
		session = getSession();
		Criteria cri = session.createCriteria(persistentClass);
		return cri.list();
	}

	@SuppressWarnings("unchecked")
	public T findById(Class<T> persistentClass, Long id) {
		session = getSession();
		Criteria cri = session.createCriteria(persistentClass);
		cri.add(Restrictions.eq("id", id));
		return (T) cri.uniqueResult();
	}
	
	public Long count(Class<T> persistentClass) {
		session = getSession();
		Criteria cri = session.createCriteria(persistentClass);
		cri.setProjection(Projections.rowCount());
		return (Long) cri.uniqueResult();
	}

	protected abstract Session loadSession();
	
	protected Session getSession(){
		if(session == null || !session.isOpen()){
			session = loadSession();
//			session = HibernateUtil.getSession();
		}
		return session;
	}
	
	public void closeSession() {
		if(session != null && session.isOpen())
			session.close();
	}
	
	protected void initialize (List<T> entities){}
}
