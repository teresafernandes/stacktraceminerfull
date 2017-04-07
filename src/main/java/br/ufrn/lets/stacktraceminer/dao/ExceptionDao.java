package br.ufrn.lets.stacktraceminer.dao;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.ufrn.lets.stacktraceminer.model.STException;

/**
 * @author Teresa Fernandes
 * */
public class ExceptionDao  extends GenericMinerDao<STException>{

	private static final long serialVersionUID = 1L;

	public ExceptionDao() {
		super();
	}
	
	@Override
	public STException save(STException entity) throws HibernateException {
		STException obj = findByName(entity);
		if(obj != null){
			obj.setCount(obj.getCount() != null ? obj.getCount()+1 : 1L);
			super.update(obj);
			return obj;
		}
		entity.setCount(1L);
		return super.save(entity);
	}

	public STException findByName(STException entity) {
		if(entity != null && entity.getName() != null){
			Criteria cri = getSession().createCriteria(STException.class);
			cri.add(Restrictions.eq("name", entity.getName()));
			cri.add(Restrictions.eq("description", entity.getDescription()));
			cri.setMaxResults(1);
			return (STException) cri.uniqueResult();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<STException> findByRange(int first, int last) {
		Criteria cri = getSession().createCriteria(STException.class);
		cri.addOrder(Order.asc("id"));
		cri. setFirstResult(first);
		cri.setMaxResults(last);
		return cri.list();
	}

	public Long count() {
		Criteria cri = getSession().createCriteria(STException.class);
		cri.setProjection(Projections.rowCount());
		return (Long) cri.uniqueResult();
	}
}
