package br.ufrn.lets.stacktraceminer.util;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil<T> {

	private Class<T> persistentClass;

	public JsonUtil(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public List<T> parseFromJsonArray(String json) {
		List<T> list = new ArrayList<T>();
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray courseArray = jsonParser.parse(json).getAsJsonArray();

		for (JsonElement objJson : courseArray) {
			T obj = (T) gson.fromJson(objJson, persistentClass);
			list.add(obj);
		}
		return list;

	}

	public T parseFromJsonElement(String json) {

		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonElement objJson = jsonParser.parse(json).getAsJsonObject();

		T obj = (T) gson.fromJson(objJson, persistentClass);

		return obj;

	}

	public String parseToJson(List<T> list) {
		Gson gson = new Gson();
		return gson.toJson(list);
	}

	public String parseToJson(T item) {
		Gson gson = new Gson();
		return gson.toJson(item);
	}
	
	public static String jsonArrayToString(JSONArray array){
		String resultado = "";
		for(int aidx = 0; aidx < array.size(); aidx++)
			resultado += " "+array.get(aidx).toString();
		return resultado;		
		
	}
}
