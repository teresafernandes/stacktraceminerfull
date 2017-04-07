package br.ufrn.lets.stacktraceminer.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufrn.lets.stacktraceminer.dao.StacktraceDao;
import br.ufrn.lets.stacktraceminer.model.STClass;
import br.ufrn.lets.stacktraceminer.model.STException;
import br.ufrn.lets.stacktraceminer.model.STFrame;
import br.ufrn.lets.stacktraceminer.model.STMethod;
import br.ufrn.lets.stacktraceminer.model.Stacktrace;
import br.ufrn.lets.stacktraceminer.util.HibernateUtil;
import br.ufrn.lets.stacktraceminer.util.RegexUtil;
import br.ufrn.lets.stacktraceminer.util.StringUtil;
import br.ufrn.lets.stacktraceminer.util.ThreadMiner;


/**
 * Contains base methods to identify, create and save the stack traces.
 * 
 * @author Teresa Fernandes
 * */
public abstract class StacktraceManager extends ThreadMiner{
	
	
	/** Stores all the stack traces identified on the data extraction that will be persisted */
	private List<Stacktrace> stacktraces = new ArrayList<Stacktrace>();
	private Stacktrace stacktrace;
	private Stacktrace lastMainStacktrace;
	/** Stores the last exception identified on the frames*/
	private STException lastExceptionNotEmpty;
	
	/** Attibute to implement singleton pattern*/
	protected static StacktraceManager instance;

	/** Start data mining from stacks, extracting and save on data base.*/
	public void startExtraction(Date fromDate){
		startExtraction();
	}
	
	public abstract void startExtraction();
	
	/** Stores on the data base the structure of stack traces*/
	protected void saveStacktraces(){
		if(stacktraces != null){
			StacktraceDao stacktraceDao = new StacktraceDao();
			int countStacktrace = 0;
			try{
				for(Stacktrace s: stacktraces){
					s = stacktraceDao.save(s);
					if(s.getId() != null && s.getId() > 0)
						countStacktrace ++;
				}
			}catch (java.lang.Exception e){
				e.printStackTrace();
			}finally{
				stacktraceDao.closeSession();
				HibernateUtil.closeSession();
				System.out.println("Stacktraces saved: "+countStacktrace);
			}
		}
	}
	

	/** From a string, the method identifies stack traces, decomposes the elements and adds their on a list.
	 * @param frames
	 * @param idQuestion
	 * @param idAnswer
	 * @param tags
	 * */
	protected void identifyAndLoadStacktraces(String frames, String idQuestion, String idAnswer, String tags, String createAt, String score){
		if(frames != null && !frames.isEmpty()){
			 stacktrace = new Stacktrace();
			 STException currentException = new STException();
			 lastExceptionNotEmpty = null;
			 lastMainStacktrace = null;
			 
			 //remove html tags from string 
			 frames = StringUtil.removeHtmlTags(frames);
			 
			 //verify if a string have the regex pattern of an frame
			 Pattern pattern = Pattern.compile(RegexUtil.stackFrameRegex);
			 Matcher matcher = pattern.matcher(frames);
			 while (matcher.find()){
				//stores the name and all description of the exception
				currentException.setName(matcher.group(RegexUtil.exceptionNameGroupRegex));
				currentException.setDescription(matcher.group(RegexUtil.exceptionDescGroupRegex));
				//if the actual pattern returns an exception, means that its the start of a new stack trace, 
				//in this case it's necessary to store previous informations and reset the object that represents stack trace.
				if(!currentException.isEmpty()){
					createStacktrace(matcher, idQuestion, idAnswer, tags, createAt,score);
					lastExceptionNotEmpty  = currentException.clone();
				}
						 
				//stores informations of actual stack trace
				stacktrace.setContent(stacktrace.getContent()+matcher.group());
				stacktrace.getFrames().add(createFrame(matcher));
			 }

			 // stores the informations of last stack trace identified, if some frame has been created.
			 createStacktrace(matcher, idQuestion, idAnswer, tags, createAt,score);
			 
		 }
	}

	/**
	 * Creates an new object of the stack trace type with the elements identified on the regular expression.
	 * @param matcher
	 * @return
	 * */
	private void createStacktrace(Matcher matcher, String idQuestion, String idAnswer, String tags, String createAt, String score){
		//if it's identified a new exception means that it's the start of a new stack trace.
		 if(lastExceptionNotEmpty!=null && stacktrace.getFrames() != null && !stacktrace.getFrames().isEmpty()){
			 //Associates the question id with the stack trace
			 if(idQuestion != null)
			 	stacktrace.setQuestion(idQuestion);
			//Associates the answer id with the stack trace
			 if(idAnswer != null)
				stacktrace.setAnswer(idAnswer);
			//verifies if the description of exception has the term "Caused by". If yes, the stack trace is a "Caused by" block.
			 stacktrace.setCausedby(stacktrace.getContent()!=null && stacktrace.getContent().toLowerCase().contains("caused by"));
			 //get main block that precedes the cause block
			 if(!stacktrace.isCausedby())
				 lastMainStacktrace = stacktrace;
			 if(lastMainStacktrace != null 
					 && stacktrace.isCausedby() 
					 && stacktraces.size() > 0 
					 && (idQuestion != null && lastMainStacktrace.getQuestion() != null && lastMainStacktrace.getQuestion().equals(idQuestion) 
					 		|| idAnswer != null && lastMainStacktrace.getAnswer() != null && lastMainStacktrace.getAnswer().equals(idAnswer))){
				 stacktrace.setMainStack(lastMainStacktrace);				 
			 }
			//associates the post tags with the stack trace
			 stacktrace.setTags(tags);
			 stacktrace.setException(lastExceptionNotEmpty);
			 stacktrace.setCreationDatePost(createAt);
			 stacktrace.setScore(score);
			 stacktraces.add(stacktrace);
			 stacktrace = new Stacktrace();
		 }			
	}
	
	/**
	 * Creates a new object of Frame type with the element identified on the regular expression.
	 * @param matcher
	 * @return
	 * */
	private STFrame createFrame(Matcher matcher){
		//stores classe data
		STClass classObj = new STClass();
		//if it's been identified the "method(class:line)" pattern, it stores just informations about class.
		classObj.setName(matcher.group(RegexUtil.unknownMethodGroupRegex));		
		if(classObj.getName() == null || classObj.getName().isEmpty())
			classObj.setName(matcher.group(RegexUtil.classGroupRegex));
		classObj.setPath(getClassPath(matcher.group(RegexUtil.methodPathGroupRegex)));
		
		//stores method data
		STMethod methodObj = new STMethod();
		methodObj.setName(getMethodName(matcher.group(RegexUtil.methodPathGroupRegex)));
		methodObj.setClasss(classObj);
		
		//stores the Frame based on the data previously collected and on the line of the failure class
		STFrame frameObj = new STFrame();
		frameObj.setMethod(methodObj);
		frameObj.setFileline(matcher.group(RegexUtil.lineGroupRegex));
		
		//check if the Frame has a signaler method
		if(stacktrace != null && stacktrace.getFrames() != null && stacktrace.getFrames().size() == 0){
			frameObj.setSignaler(true);
		}
		
		//reference to the method of the Frame that precedes the stack
		if(stacktrace != null && stacktrace.getFrames() != null && stacktrace.getFrames().size() > 0){
			frameObj.setCallFrame(stacktrace.getFrames().get(stacktrace.getFrames().size() - 1));
		}
		
		return frameObj;
	}
	
	/**
	 * Return the method name contained on the string that represent the method path on an Frame.
	 * Example: based on the method path 'sun.net.www.protocol.http.HttpURLConnection.getInputStream', 
	 * it will return the string 'getInputStream', that corresponds to the method name.
	 * @param methodPath
	 * @return
	 * */
	private String getMethodName(String methodPath){
		if(methodPath != null && !methodPath.isEmpty()){
			String[] stringSplited = methodPath.split("\\.");
			if(stringSplited.length > 0)
				return stringSplited[stringSplited.length-1];
		}
		return methodPath;
	}
	
	/**
	 * Return the class path contained on the string that represent the method path on an Frame.
	 * Example: based on the method path 'sun.net.www.protocol.http.HttpURLConnection.getInputStream', 
	 * it will return the string 'getInputStream', that corresponds to the class path.
	 * @param methodPath
	 * @return
	 * */
	private String getClassPath(String methodPath){
		if(methodPath != null && !methodPath.isEmpty()){
			String[] stringSplited = methodPath.split("\\.");
			if(stringSplited.length > 1){
				String path = "";
				for(int i = 0; i< stringSplited.length - 1; i++){
					path += stringSplited[i];
					if(i < stringSplited.length - 2)
						path += ".";
				}
				return path;
			}
		}
		return methodPath;
	}
	
	/**
	 * Print the Frames on the case that the string is part of the stack trace based on regular expression.
	 * @param frames
	 * @return
	 * */
	public void printStacktrace(String frames){
		stacktraces = new ArrayList<Stacktrace>();
		identifyAndLoadStacktraces(frames,null,null,null,null, null);
		for(Stacktrace s : stacktraces)
			System.out.println("STACKTRACE\n"+s.toString());
	}

	public List<Stacktrace> getStacktraces() {
		return stacktraces;
	}
	
}
