package br.ufrn.lets.stacktraceminer.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import br.ufrn.lets.stacktraceminer.model.STFrame;


/**
 * @author Teresa Fernandes
 * */
public class FrameDao extends GenericMinerDao<STFrame>{

	private static final long serialVersionUID = 1L;

	public FrameDao() {
		super();
	}
	
	@Override
	public STFrame save(STFrame entity) throws HibernateException {
		if(entity!=null){
			MethodDao methodDao = new MethodDao();
			try{
				entity.setMethod(methodDao.save(entity.getMethod()));
				entity = super.save(entity);
			}finally{
				methodDao.closeSession();
			}
		}
		return entity;
	}
	
	public STFrame findByMethodAndException(STFrame entity) {
		if(entity != null && (entity.getMethod() != null || entity.getStacktrace().getException()!=null)){
			Criteria cri = getSession().createCriteria(STFrame.class);
			if(entity.getMethod() != null)
				cri.add(Restrictions.eq("method.id", entity.getMethod().getId()));
			if(entity.getStacktrace().getException() != null){
				cri.createAlias("stacktrace", "s").createAlias("s.exception", "e");
				cri.add(Restrictions.eq("e.id", entity.getStacktrace().getException().getId()));
			}
			cri.setMaxResults(1);
			return (STFrame) cri.uniqueResult();
		}
		return null;
	}
}
