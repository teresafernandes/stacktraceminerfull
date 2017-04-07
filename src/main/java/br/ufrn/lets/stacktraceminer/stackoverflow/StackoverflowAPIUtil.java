package br.ufrn.lets.stacktraceminer.stackoverflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class StackoverflowAPIUtil {

	private static String keyapp = "mTXQUc*QnErAXCUqxFE)Gw((";
	
	public static JSONObject getPostByIdQuestion(String idQuestion){ 
		try {
			String url = "https://api.stackexchange.com/2.2/questions/"+idQuestion+"?order=desc&sort=activity&site=stackoverflow&filter=!9YdnSIN18&key="+keyapp;
			return getPostById(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getPostByIdAnswer(String idAsnwer){ 
		try {
			String url = "https://api.stackexchange.com/2.2/answers/"+idAsnwer+"?order=desc&sort=activity&site=stackoverflow&filter=!9YdnSMKKT&key="+keyapp;
			return getPostById(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static JSONObject getPostById(String url) throws IOException{
		BufferedReader in = requestNextPage(url,1,1);
		String line = in.readLine();
		if(line != null){
			return (JSONObject) JSONValue.parse(line);
		}
		return null;
	}
	
	/**
	 * Método que realiza uma nova requisição de uma página específica da resposta da api
	 * @param page
	 * @param pagesize
	 * @throws IOException
	 * */
	private static  BufferedReader requestNextPage(String url, Integer page, Integer pagesize) throws IOException{
		String currenturl;
		if(page != null && page > 0 )
			currenturl = url.replace("?", "?pageSize="+pagesize+"&page="+page+"&");
		else
			currenturl = url;
		return reloadUrl(currenturl);
	}
	
	/**
	 * Método que carrega a resposta conforme requisição pre-configurada na url
	 * @throws IOException
	 * */
	private static  BufferedReader reloadUrl(String currenturl) throws IOException{
		BufferedReader in = null;
		try{
			if(currenturl != null && !currenturl.isEmpty()){
				URL url = new URL(currenturl);
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
			}
		}catch(IOException e){
			try {
				Thread.sleep(20000);
				System.out.println("IOException....wait 20 seconds");
				reloadUrl(currenturl);
			} catch (InterruptedException e1) {
				throw e;
			}
		}

		return in;
	}
}

