/**
 * Data: 19/03/2016
 */
package br.ufrn.lets.stacktraceminer.core;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufrn.lets.stacktraceminer.dao.ExceptionDao;
import br.ufrn.lets.stacktraceminer.dao.StacktraceDao;
import br.ufrn.lets.stacktraceminer.model.STException;
import br.ufrn.lets.stacktraceminer.model.Stacktrace;
import br.ufrn.lets.stacktraceminer.util.HibernateUtil;
import br.ufrn.lets.stacktraceminer.util.RegexUtil;
import br.ufrn.lets.stacktraceminer.util.StringUtil;

/**
 * @author Teresa Fernandes
 *
 */
public class StacktraceMinerTest {
	
	public static void processExceptionName(){
		ExceptionDao exceptionDao = new ExceptionDao();
		List<STException> all = exceptionDao.list(STException.class);
		for(STException e: all){
			if(e.getId()%500 == 0)
				System.out.println(e.getId());
			
			e.setDescription(StringUtil.removeHtmlTags(e.getName()+e.getDescription()));
			Pattern pattern = Pattern.compile(RegexUtil.exceptionRegex);
			Matcher matcher = pattern.matcher(e.getDescription());
			if (matcher.find()){
				e.setName(matcher.group(3));
				exceptionDao.update(e);
//					System.out.println(e.getId());
			}
			
		}
		exceptionDao.closeSession();
		HibernateUtil.closeSession();
	}
	
	public static void collectRandomStacktraces(Integer quantidade){
		StacktraceDao dao = new StacktraceDao();
		try{
			Long countStacktraces = dao.count(Stacktrace.class);
			Random gerador = new Random();
			
			for(int i =1; i<quantidade;i++){
				System.out.println(gerador.nextInt(countStacktraces.intValue()));
			}
		}finally{
			dao.closeSession();
		}
	}
	
	public static void matchTest(String frames){
		Pattern pattern = Pattern.compile(RegexUtil.stackFrameRegex);
//		frames =  frames.replaceAll("\\<.*?>","").replaceAll("\\s+", " ");
		System.out.println(frames);
		 Matcher matcher = pattern.matcher(frames);
		 
		 while (matcher.find()){
			 
//			 System.out.println("---->  "+matcher.group());
			 for(int i =0; i<matcher.groupCount() ; i++){
				 System.out.println(i+"---->  "+matcher.group(i));
			 }
		 }
	}
}
