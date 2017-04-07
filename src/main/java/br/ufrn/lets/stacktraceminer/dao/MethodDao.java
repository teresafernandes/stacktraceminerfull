package br.ufrn.lets.stacktraceminer.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import br.ufrn.lets.stacktraceminer.model.STMethod;


/**
 * @author Teresa Fernandes
 * */
public class MethodDao  extends GenericMinerDao<STMethod>{

	private static final long serialVersionUID = 1L;

	public MethodDao() {
		super();
	}

	@Override
	public STMethod save(STMethod entity) throws HibernateException {
		if(entity != null){
			ClassDao classDao = new ClassDao();
			try{
				entity.setClasss(classDao.save(entity.getClasss()));
				STMethod obj = findByNameAndClass(entity);
				if(obj != null)
					return obj;
				entity = super.save(entity);
			}finally{
				classDao.closeSession();
			}
		}
		return entity;
	}
	
	public STMethod findByNameAndClass(STMethod entity) {
		if(entity != null && entity.getName() != null){
			Criteria cri = getSession().createCriteria(STMethod.class);
			cri.add(Restrictions.eq("name", entity.getName()));
			if(entity.getClasss() != null)
				cri.add(Restrictions.eq("classs.id", entity.getClasss().getId()));
			cri.setMaxResults(1);
			return (STMethod) cri.uniqueResult();
		}
		return null;
	}
}
