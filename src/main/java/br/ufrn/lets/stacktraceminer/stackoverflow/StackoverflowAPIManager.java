package br.ufrn.lets.stacktraceminer.stackoverflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import br.ufrn.lets.stacktraceminer.core.StacktraceManager;
import br.ufrn.lets.stacktraceminer.util.CalendarUtil;
/**
 * Implements the methods necessary to mining stack traces by the Stackexchange API.
 * @author Teresa Fernandes
 * */
public class StackoverflowAPIManager extends StacktraceManager{
	
	/** URL to request to API*/
	private URL url;
	/** Buffer to stores API response*/
	private BufferedReader in;
	/** Application token. It's used to get larger quota to request form an IP.
	 *  To register an application and get this token, access: http://stackapps.com/apps/oauth/register*/
	private static String keyapp = "mTXQUc*QnErAXCUqxFE)Gw(("; 
	/** URL base content, without filters of pagination*/
	private static String baseurl = "https://api.stackexchange.com/2.2/search?tagged=hibernate&site=stackoverflow&filter=!1PVL)N6vG9NpQYyHB8lB.eC79G.zm_*GT&key="+keyapp;
	/** URL base content, with date filter*/
	private static String baseurl_fromdate = "https://api.stackexchange.com/2.2/search?fromdate=data_inicio&tagged=hibernate&site=stackoverflow&filter=!1PVL)N6vG9NpQYyHB8lB.eC79G.zm_*GT&key="+keyapp;
	/** URL with the filters that defines the page that have to be requested*/
	private  String currenturl;
	/** Page size to be requested*/
	private static int pagesize = 100;
	
	/** Singleton pattern*/
	private StackoverflowAPIManager() {	}
	public static synchronized StackoverflowAPIManager getInstance() {
        if (instance == null) 
            instance = new StackoverflowAPIManager();
        return (StackoverflowAPIManager) instance;
    }
	
	/**
	 * Start the data mining of Stackoverflow with the configured URL. <br/>
	 * The requests are paginated. It's a request for each page, conform the page size previously defined,
	 * until there is any page or any quota. <br/>
	 * The extraction is made on the questions and answers that contains the tag "Java".<br/>
	 * After, each ones is compared with the regular expression that represents an stack trace.<br/>
	 * If was identified as a stack trace, the information are stored on the data base. <br/>
	 * About the Stackexchange API: https://api.stackexchange.com/docs
	 * @throws IOException
	 * */
	@Override
	public void startExtraction() {
		try{
			
			// find total pages of the response
			Long total = getTotalNumberReponsePages();
			System.out.println("PAGES TOTAL: "+ total);
			if(total != null && total > 0){
				//init variables
				boolean hasmore = true;
				boolean hasquota = true;
				String line;
				JSONObject obj = null, question, answer;
				JSONArray questions, answers;
				long count = 0;
				
				//iteration on page amout
				//if there is no page or quota, the iteration is stoped
				for(int ridx = 1;  hasmore && hasquota; ridx++){
					//request the page of current iteration
					requestNextPage(ridx);
					while ((line = getNextLine()) != null) {
						obj = (JSONObject) JSONValue.parse(line);
						hasmore = (Boolean) obj.get("has_more");
						hasquota = (Long) obj.get("quota_remaining") > 0;
						
						questions = (JSONArray) obj.get("items");
						if(questions != null){
							//iterate the questions
							for(int qidx = 0; qidx < questions.size(); qidx++){
								question = (JSONObject) questions.get(qidx);
								count++;
								//verifies and stores the stacktraces of question on an list 
								identifyAndLoadStacktraces((String) question.get("body"), String.valueOf(question.get("question_id")),null, jsonArrayToString((JSONArray) question.get("tags")), CalendarUtil.convertUnixTimeToDate(String.valueOf(question.get("creation_date"))), (String) question.get("score"));
								
								answers = (JSONArray) question.get("answers");
								if (answers != null) {
									//iterate answers of the question
									for(int aidx = 0; aidx < answers.size(); aidx++){
										answer = (JSONObject) answers.get(aidx);
										////verifies and stores the stacktraces of answer on an list
										identifyAndLoadStacktraces((String) answer.get("body"),null, String.valueOf(answer.get("answer_id")), jsonArrayToString((JSONArray) answer.get("tags")), CalendarUtil.convertUnixTimeToDate(String.valueOf(question.get("creation_date"))), (String) answer.get("score"));
									}
								}
							}
						}
					}
					System.out.println("PAGE "+ridx+":" + count +" questions, "+ (getStacktraces()!= null ? getStacktraces().size() : 0)+" stacktraces, quota"+obj.get("quota_remaining"));
				}			
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			System.out.println("Stacktraces extracted: "+(getStacktraces()!=null? getStacktraces().size() : 0));
			saveStacktraces();
		}
	}
	

	@Override
	public void startExtraction(Date fromDate) {

		baseurl = baseurl_fromdate.replaceFirst("data_inicio", CalendarUtil.convertDateToUnixTime(fromDate));
		startExtraction();
	}
	
	/**
	 * Returns number of the pages of the request form the configured URL
	 * @throws IOException
	 * @return
	 * */
	public long getTotalNumberReponsePages() throws IOException{
		requestNextPage(1,1);
		String line = in.readLine();
		if(line != null){
			JSONObject obj = (JSONObject) JSONValue.parse(line);
			Long totalItems = (Long) obj.get("total"); 
			System.out.println("QUESTIONS TOTAL: "+ totalItems);
			return (totalItems/pagesize)+1;
		}
		return 0;
	}
	
	/**
	 * Releases anew request form a specific page of a response
	 * @param page
	 * @throws IOException
	 * */
	public  void requestNextPage(Integer page) throws IOException{
		requestNextPage(page, pagesize);
	}
	
	/**
	 * Releases anew request form a specific page of a response
	 * @param page
	 * @param pagesize
	 * @throws IOException
	 * */
	private  void requestNextPage(Integer page, Integer pagesize) throws IOException{
		if(page != null && page > 0 )
			currenturl = baseurl.replace("?", "?pageSize="+pagesize+"&page="+page+"&");
		else
			currenturl = baseurl;
		reloadUrl();
	}
	
	/**
	 * Load the response according the request pre-configures on the URL
	 * @throws IOException
	 * */
	private  void reloadUrl() throws IOException{
		try{
			if(currenturl != null && !currenturl.isEmpty()){
				url = new URL(currenturl);
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
			}
		}catch(IOException e){
			try {
				Thread.sleep(20000);
				System.out.println("IOException....wait 20 seconds");
				reloadUrl();
			} catch (InterruptedException e1) {
				throw e;
			}
		}
	}
	
	/** 
	 * Returns the next page line of the current response
	 * @throws IOException
	 * */
	public  String getNextLine() throws IOException{
		return in.readLine();
	}

	
	/**
	 * Returns the JSONArray content on a String
	 * @param array
	 * @return
	 * */
	public  String jsonArrayToString(JSONArray array){
		String resultado = "";
		for(int aidx = 0; aidx < array.size(); aidx++)
			resultado += " "+array.get(aidx).toString();
		return resultado;		
		
	}
}
