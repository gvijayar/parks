package org.openshift.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.openshift.data.mongo.DBConnection;
import org.openshift.data.postgres.DataManager;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@WebServlet("/parks")
public class ParksServlet extends HttpServlet {

	@Resource(name = "jdbc/PostgreSQLDS")
	DataSource ds;
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//findParksWithin(request, response);
		setUpDatabase(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//findParksWithin(request, response);	
		setUpDatabase(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		//findParksWithin(request, response);
		setUpDatabase(request, response);
	}
	
	public String getAllParks(HttpServletRequest request, HttpServletResponse response) throws IOException {
				
		ArrayList<HashMap<String, Object>> allParksList = new ArrayList<HashMap<String, Object>>();
		DB db = DBConnection.getInstance().getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");
		DBCursor cursor = parkListCollection.find();
		try {
			while (cursor.hasNext()) {
				DBObject dataValue = cursor.next();
				HashMap<String, Object> holder = new HashMap<String, Object>();
				holder.put("name", dataValue.get("Name"));
				holder.put("position", dataValue.get("pos"));
				holder.put("id", dataValue.get("_id").toString());
				allParksList.add(holder);

			}			
		} finally {
			cursor.close();
		}
		
		Gson gson = new Gson(); 
		String json = gson.toJson(allParksList); 
		
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();	
			
		return json;
	}

	public void setUpDatabase(HttpServletRequest request, HttpServletResponse response){
		try {
			Connection con = ds.getConnection();
			
			try{
				PreparedStatement extension = con.prepareStatement(new DataManager().installGisExtensions());
				extension.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try{
				PreparedStatement statement = con.prepareStatement(new DataManager().initializeDatabase());
				statement.executeUpdate();				
			}catch (Exception e){
				e.printStackTrace();
			}
			
			try{
				PreparedStatement insertstatement = con.prepareStatement(new DataManager().seedDatabase());
				
				insertstatement.setString(1, "Guna");
				insertstatement.setDouble(2, -85.7302);
				insertstatement.setDouble(3, 37.5332);
				insertstatement.executeUpdate();				
			}catch (Exception e){
				e.printStackTrace();
			}
			
			con.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String findParksWithin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		//Get Request Parameters
		String lat1 = request.getParameter("lat1");
		String lon1 = request.getParameter("lon1");
		String lat2 = request.getParameter("lat2");
		String lon2 = request.getParameter("lon2");
		
		ArrayList<HashMap<String, Object>> allParksList = new ArrayList<HashMap<String, Object>>();
		DB db = DBConnection.getInstance().getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");

		// make the query object
		BasicDBObject spatialQuery = new BasicDBObject();
		ArrayList<ArrayList<Float>> posList = new ArrayList<ArrayList<Float>>();
		ArrayList<Float> firstPair = new ArrayList<Float>();
		ArrayList<Float> secondPair = new ArrayList<Float>();
		firstPair.add(new Float(lon1));
		firstPair.add(new Float(lat1));
		secondPair.add(new Float(lon2));
		secondPair.add(new Float(lat2));

		posList.add(firstPair);
		posList.add(secondPair);

		BasicDBObject boxQuery = new BasicDBObject();
		boxQuery.put("$box", posList);

		spatialQuery.put("pos", new BasicDBObject("$within", boxQuery));

		DBCursor cursor = parkListCollection.find(spatialQuery);
		try {
			while (cursor.hasNext()) {
				DBObject dataValue = cursor.next();
				HashMap<String, Object> holder = new HashMap<String, Object>();
				holder.put("name", dataValue.get("Name"));
				holder.put("position", dataValue.get("pos"));
				holder.put("id", dataValue.get("_id").toString());
				allParksList.add(holder);
			}
		} finally {
			cursor.close();
		}

		Gson gson = new Gson(); 
		String json = gson.toJson(allParksList); 
		
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();	
			
		return json;
	}		
}
