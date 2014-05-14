package org.openshift.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openshift.data.postgres.ConnectionManager;
import org.openshift.data.postgres.DataManager;

import com.google.gson.Gson;

@WebServlet("/parks")
public class ParksServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		findParksWithin(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		findParksWithin(request, response);	
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		findParksWithin(request, response);
	}
	
	public void getAllParks(HttpServletRequest request, HttpServletResponse response) {
				
	}
	
	public String findParksWithin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		//Get Request Parameters
		String lat1 = request.getParameter("lat1");
		String lon1 = request.getParameter("lon1");
		String lat2 = request.getParameter("lat2");
		String lon2 = request.getParameter("lon2");
		
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
				coordinates[0] = set.getFloat("lat");
				coordinates[1] = set.getFloat("lon");
				
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
		
		Gson gson = new Gson(); 
		String json = gson.toJson(allParksList); 
	
		System.out.println("JSON DATA: "+json);
		
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();	
			
		return json;
	}		
}
