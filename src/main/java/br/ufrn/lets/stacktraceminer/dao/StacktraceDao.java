package br.ufrn.lets.stacktraceminer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import br.ufrn.lets.stacktraceminer.model.STFrame;
import br.ufrn.lets.stacktraceminer.model.Stacktrace;

/**
 * @author Teresa Fernandes
 * */
public class StacktraceDao  extends GenericMinerDao<Stacktrace>{

	private static final long serialVersionUID = 1L;

	public StacktraceDao() {
		super();
	}

	@Override
	public Stacktrace save(Stacktrace entity)  throws HibernateException {
		if(entity != null){
			ExceptionDao expDao = new ExceptionDao();
			FrameDao frameDao = new FrameDao();
			try{				
				entity.setException(expDao.save(entity.getException()));
				
				entity.setCreationDate(new Date());
				entity = super.save(entity);
				if(entity.getFrames()!=null){
			
					for(STFrame f : entity.getFrames()){
						f.setStacktrace(entity);
						frameDao.save(f);				
					}
				}
			}finally{
				expDao.closeSession();
				frameDao.closeSession();
			}
		}		
		return entity;
	}
	public Stacktrace findByQuestionAnswerAndException(Stacktrace entity) {
		if(entity != null && (entity.getAnswer() != null || entity.getQuestion()!=null) && entity.getException() != null){
			Criteria cri = getSession().createCriteria(Stacktrace.class);
			if(entity.getAnswer() != null)
				cri.add(Restrictions.eq("answer", entity.getAnswer()));
			if(entity.getQuestion() != null)
				cri.add(Restrictions.eq("question", entity.getQuestion()));
			if(entity.getException() != null){
				cri.createAlias("exception", "e");
				cri.add(Restrictions.eq("e.name", entity.getException().getName()));
				cri.add(Restrictions.eq("e.description", entity.getException().getDescription()));
			}
			cri.setMaxResults(1);
			return (Stacktrace) cri.uniqueResult();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findStacktraces() {
		String sql = "select s.id_question as id_question, s.id_answer as id_answer from Stacktrace s";
		List<Map<String, Object>> rows = getSession().createSQLQuery(sql)
				.addScalar("id_question")
				.addScalar("id_answer")
				.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).list();
		List<String> idsStacktraces = new ArrayList<String>();
		for(Map<String, Object> obj : rows){
			String idQuestion = (String) obj.get("id_question");
			String idAnswer = (String) obj.get("id_answer");
			if(idQuestion != null && !idQuestion.isEmpty())
				idsStacktraces.add(idQuestion);
			else
				idsStacktraces.add(idAnswer);
		}
		
		return idsStacktraces;
	}
	
	public void saveFields(String id_question, String id_answer, String score){
		try{
			String sql = "update Stacktrace set score = '"+score+"' where id_question = "+id_question+" or id_answer = "+id_answer;
			getSession().beginTransaction();
			getSession().createSQLQuery(sql).executeUpdate();
			getSession().getTransaction().commit();
		}catch (HibernateException e){
			getSession().getTransaction().rollback();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Stacktrace> listarTodos() {
		Query q = getSession().createQuery("from Stacktrace");
		q.setMaxResults(100);
	    return  q.list();
	}

	@SuppressWarnings("unchecked")
	public List<Stacktrace> getStacksByTag(String tag) {
		Query q = getSession().createQuery("from Stacktrace where tags like '%"+tag+"%'");
		q.setMaxResults(100);
	    return  q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Stacktrace> getStacksByContent(String query) {
		Query q = getSession().createQuery("from Stacktrace where content like '%"+query+"%'");
		q.setMaxResults(100);
	    return  q.list();
	}
	
	@SuppressWarnings("unchecked")
	public Stacktrace getUltimoStacktrace() {
		Query q = getSession().createQuery("from Stacktrace order by id desc");
		q.setMaxResults(1);
		
		List<Stacktrace> lista = q.list();
		if(lista != null && !lista.isEmpty())
			return lista.get(0);
		return null;
	}
}
