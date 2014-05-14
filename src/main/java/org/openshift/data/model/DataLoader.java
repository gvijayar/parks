package org.openshift.data.model;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.openshift.data.postgres.ConnectionManager;
import org.openshift.data.postgres.DataManager;

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
	

	public void setUpParksData(){
		Connection con = null;
		PreparedStatement extension = null;
		PreparedStatement statement = null;
		
		try {
			con = ConnectionManager.getConnection();		
			
			try{
				extension = con.prepareStatement(new DataManager().installGisExtensions());
				extension.executeUpdate();
			}catch(Exception e){
				//e.printStackTrace();
			}
			
			try{
				statement = con.prepareStatement(new DataManager().initializeDatabase());
				statement.executeUpdate();				
			}catch (Exception e){
				//e.printStackTrace();
			}
			
			try{
				DataLoader myParkLoader = new DataLoader();
				ArrayList<ParkData> myParkData = myParkLoader.getAllParksData();
				PreparedStatement insertstatement = con.prepareStatement(new DataManager().seedDatabase());

				for (int i=0; i<myParkData.size(); i++){
					insertstatement.setString(1, myParkData.get(i).getName());
					insertstatement.setString(2, "POINT(" + myParkData.get(i).getPos()[0] + " " + myParkData.get(i).getPos()[1] + ")");
					insertstatement.executeUpdate();						
				}			
			}catch (Exception e){
				e.printStackTrace();
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeStatement(extension);
			ConnectionManager.closeStatement(statement);
			ConnectionManager.closeConnection(con);
		}		
	}
}
