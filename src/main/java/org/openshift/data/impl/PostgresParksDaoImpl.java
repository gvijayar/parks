package org.openshift.data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.openshift.data.dbutilities.ConnectionManager;
import org.openshift.data.model.ParkData;
import org.openshift.data.seed.DataManager;

public class PostgresParksDaoImpl implements ParksDao{

	@Override
	public ArrayList<HashMap<String, Object>> getAllParksWithin(String lat1, String lon1, String lat2, String lon2) {
		
		ArrayList<HashMap<String, Object>> allParksList = new ArrayList<HashMap<String, Object>>();
		
		Connection con = null;
		PreparedStatement queryStatement = null;
		ResultSet set = null;
		
		try{
			con = ConnectionManager.getConnection();
			queryStatement = con.prepareStatement(new DataManager().getParkCoordinates(lon1, lat1, lon2, lat2));
			set = queryStatement.executeQuery();
			
			while (set.next()){
				HashMap<String, Object> holder = new HashMap<String, Object>();
				
				Float[] coordinates = new Float[2];
				coordinates[0] = set.getFloat("lon");
				coordinates[1] = set.getFloat("lat");
				
				holder.put("name", set.getString("name"));
				holder.put("position", coordinates);
				holder.put("id", set.getString("gid"));
				
				allParksList.add(holder);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeConnection(con);
			ConnectionManager.closeStatement(queryStatement);
		}
		
		return allParksList;
	}

	@Override
	public ArrayList<ParkData> getAllParks() {
		// TODO Auto-generated method stub
		return null;
	}


}
