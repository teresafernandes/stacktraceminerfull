package br.ufrn.lets.stacktraceminer.dao;

import org.hibernate.Session;

import br.ufrn.lets.stacktraceminer.model.IdEntity;
import br.ufrn.lets.stacktraceminer.util.HibernateUtil;

public class GenericMinerDao<T extends IdEntity> extends GenericDao<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Session loadSession() {
		return HibernateUtil.getSession();
	}
}
