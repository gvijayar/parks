package org.openshift.model;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class DataLoader {

	public static void main(String args[]) throws FileNotFoundException{		
		DataLoader loader = new DataLoader();
		loader.getAllParksData();
		 	
	}
	
	public ArrayList<ParkData> getAllParksData() throws FileNotFoundException{
		Gson myGson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("parkcoord.json")));
		JsonParser jsonParser = new JsonParser();
		JsonArray parkarray= jsonParser.parse(reader).getAsJsonArray();
	
		ArrayList<ParkData> parkcoordinates = new ArrayList<ParkData>();
		 for ( JsonElement park : parkarray ) {
			 ParkData aParkData = myGson.fromJson(park, ParkData.class);
			 parkcoordinates.add(aParkData);
		 }
		 		 
		 return parkcoordinates;
	}
	
	
}
