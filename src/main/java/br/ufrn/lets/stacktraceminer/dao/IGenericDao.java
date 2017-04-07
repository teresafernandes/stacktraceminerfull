
package br.ufrn.lets.stacktraceminer.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;


/**
 * Interface os generic operations to persistence and search an entity.
 * @author Teresa Fernandes
 */
public interface IGenericDao  <T extends Serializable>{
    
    public List<T> list (Class<T> persistentClass);
    
    public T findById (Class<T> persistentClass, Long id);
    
    public void saveUpdate(T entity) throws HibernateException;
    
    public T save(T entity) throws HibernateException;

    public void delete(T entity) throws HibernateException;

    public void update(T entity) throws HibernateException;
    
    public Long count(Class<T> persistentClass) ;
}
