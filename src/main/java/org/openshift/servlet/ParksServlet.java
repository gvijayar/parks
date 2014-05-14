package org.openshift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openshift.data.impl.ParksDao;
import org.openshift.data.impl.PostgresParksDaoImpl;

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
	
	public String findParksWithin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		//Get Request Parameters
		String lat1 = request.getParameter("lat1");
		String lon1 = request.getParameter("lon1");
		String lat2 = request.getParameter("lat2");
		String lon2 = request.getParameter("lon2");
		
		ParksDao parksDataAccess = new PostgresParksDaoImpl();
		ArrayList<HashMap<String, Object>> allParksList = parksDataAccess.getAllParksWithin(lat1, lon1, lat2, lon2);
		
		Gson gson = new Gson(); 
		String json = gson.toJson(allParksList); 
		
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();	
			
		return json;
	}		
}
