package br.ufrn.lets.stacktraceminer.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import br.ufrn.lets.stacktraceminer.model.STClass;

/**
 * @author Teresa Fernandes
 * */
public class ClassDao  extends GenericMinerDao<STClass>{

	private static final long serialVersionUID = 1L;

	public ClassDao() {
		super();
	}
	
	@Override
	public STClass save(STClass entity) throws HibernateException {
		STClass obj = findByName(entity);
		if(obj != null)
			return obj;
		return super.save(entity);
	}

	public STClass findByName(STClass entity) {
		if(entity != null && entity.getName() != null){
			Criteria cri = getSession().createCriteria(STClass.class);
			cri.add(Restrictions.eq("name", entity.getName()));
			cri.add(Restrictions.eq("path", entity.getPath()));
			cri.setMaxResults(1);
			return (STClass) cri.uniqueResult();
		}
		return null;
	}

	
}
