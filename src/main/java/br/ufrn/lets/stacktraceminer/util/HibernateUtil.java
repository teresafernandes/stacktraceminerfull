package br.ufrn.lets.stacktraceminer.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import br.ufrn.lets.stacktraceminer.model.STClass;
import br.ufrn.lets.stacktraceminer.model.STException;
import br.ufrn.lets.stacktraceminer.model.STFrame;
import br.ufrn.lets.stacktraceminer.model.STMethod;
import br.ufrn.lets.stacktraceminer.model.Stacktrace;
/**
 * 
 * @author Teresa Fernandes
 */
public class HibernateUtil{

	private static SessionFactory sessionFactory;
    protected static AnnotationConfiguration cfg;
    
    private static SessionFactory buildSessionFactory() {
    	try {
        	cfg = new AnnotationConfiguration().configure();
    		cfg.addAnnotatedClass(STClass.class);
    		cfg.addAnnotatedClass(STException.class);
    		cfg.addAnnotatedClass(STFrame.class);
    		cfg.addAnnotatedClass(STMethod.class);
    		cfg.addAnnotatedClass(Stacktrace.class);

        	
        	//Versao Hiberante 4
//        	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
//          sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        	//Versao Hiberante 3
    		sessionFactory = cfg.buildSessionFactory();

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    	return sessionFactory;
    }

    public static Session getSession() {
    	if(sessionFactory == null || sessionFactory.isClosed()) sessionFactory = buildSessionFactory();
        return sessionFactory.openSession();
    }
    
    public static void closeSession(){
    	if(sessionFactory != null) 
    		sessionFactory.close();
    }

}
