package br.ufrn.lets.stacktraceminer.util;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import br.ufrn.lets.stacktraceminer.model.STClass;
import br.ufrn.lets.stacktraceminer.model.STException;
import br.ufrn.lets.stacktraceminer.model.STFrame;
import br.ufrn.lets.stacktraceminer.model.STMethod;
import br.ufrn.lets.stacktraceminer.model.Stacktrace;
/**
 * 
 * @author Teresa Fernandes
 */
public class DatabaseBuilder{
	protected static AnnotationConfiguration cfg;
	
	public static void main(String[] args) {
		createSchema();
	}
	
	public static void createSchema(){
		cfg = new AnnotationConfiguration().configure();	
		cfg.addAnnotatedClass(STClass.class);
		cfg.addAnnotatedClass(STException.class);
		cfg.addAnnotatedClass(STFrame.class);
		cfg.addAnnotatedClass(STMethod.class);
		cfg.addAnnotatedClass(Stacktrace.class);
	
		
		SchemaExport se = new SchemaExport(cfg);
		se.create(true, true);
	}
}
