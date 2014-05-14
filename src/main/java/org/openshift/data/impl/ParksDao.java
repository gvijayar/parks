package org.openshift.data.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.openshift.data.model.ParkData;

public interface ParksDao {

	public ArrayList<HashMap<String, Object>> getAllParksWithin(String lat1, String lon1, String lat2, String lon2);
	
	public ArrayList<ParkData> getAllParks();
	
}
