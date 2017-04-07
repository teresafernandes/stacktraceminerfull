package br.ufrn.lets.stacktraceminer.stackoverflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import br.ufrn.lets.stacktraceminer.core.StacktraceManager;
import br.ufrn.lets.stacktraceminer.dao.StacktraceDao;
import br.ufrn.lets.stacktraceminer.util.HibernateUtil;

/**
 * Implements the methods necessary to mining stack traces by the Dump file on XML format.
 * This Dump is available on the site of MSR event: http://2015.msrconf.org
 * 
* @author Teresa Fernandes
* */
public class StackoverflowDumpXmlManager  extends StacktraceManager{

	private static String xmlFilePath = "/Users/Teresa/Documents/Mestrado/Mineração/StackexangeDump/Posts.xml";
	
	/** Singleton pattern*/
	private StackoverflowDumpXmlManager() {	}
	public static synchronized StackoverflowDumpXmlManager getInstance() {
        if (instance == null) 
            instance = new StackoverflowDumpXmlManager();
        return (StackoverflowDumpXmlManager) instance;
    }
	
	@Override
	public void startExtraction() {		
		in=new BufferedReader(new InputStreamReader(System.in));
        Thread t1=new Thread(new StackoverflowDumpXmlManager());
        t1.start(); 
        System.out.println("press Q THEN ENTER to terminate");
        
		startExtraction_bigFiles(0);
	}
	
	/** Extract data from xml file using SAX XML Parser. Its better to read big files.*/
	private void startExtraction_bigFiles(Integer initialRow){
		try {
			FileInputStream fin = new FileInputStream(new File(xmlFilePath));
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader r = factory.createXMLStreamReader(fin);
			int rowCount = 0;
			int totalRow = 21736594;  
			int event = r.getEventType();
			while (true) {
				if(quit==true) break;
				if (event == XMLStreamConstants.START_ELEMENT   && r.getName().toString().equalsIgnoreCase("row")) {
					if(rowCount % 1000 == 0)
						System.out.println(" Posts analysed : "+rowCount+", Stacktraces: "+(getStacktraces()!=null? getStacktraces().size() : 0)+", Falta: "+(totalRow - rowCount)+", Porc: "+(Double.valueOf(rowCount)/Double.valueOf(totalRow)*100)+"%");
					if(rowCount>=initialRow && r.getAttributeValue("", "PostTypeId") != null
							&& r.getAttributeValue("", "Id") != null
							&& r.getAttributeValue("", "Body") != null){
						
						boolean isQuestion = r.getAttributeValue("", "PostTypeId") != null && r.getAttributeValue("", "PostTypeId").equals("1"); 
			    		identifyAndLoadStacktraces(r.getAttributeValue("", "Body"), isQuestion ? r.getAttributeValue("", "Id") : null, !isQuestion ? r.getAttributeValue("", "Id") : null, r.getAttributeValue("", "Tags"),r.getAttributeValue("", "CreationDate"), r.getAttributeValue("", "Score"));
						
					}
					rowCount++;
				}
				if (!r.hasNext())
					break;
				else
					event = r.next();
			}
			r.close();
			fin.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("Stacktraces extracted: "+(getStacktraces()!=null? getStacktraces().size() : 0));
			saveStacktraces();
		}
	}
	
	
	public void countRows(){
		int rowCount = 0;
		try {
			FileInputStream fin = new FileInputStream(new File(xmlFilePath));
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader r = factory.createXMLStreamReader(fin);
			
			int event = r.getEventType();
			while (true) {		
				
				if (event == XMLStreamConstants.START_ELEMENT   && r.getName().toString().equalsIgnoreCase("row")) {
					rowCount++;
					if(rowCount % 1000000 == 0)
						System.out.println(" Posts : "+rowCount);
				}
				if (!r.hasNext())
					break;
				else
					event = r.next();
			}
			r.close();
			fin.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("rows: "+rowCount);
		}
	}
}
