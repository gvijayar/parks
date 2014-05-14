package org.openshift.data.mongo;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class DBConnection {

	private static DBConnection _iMongoConnection = null;
	private DB mongoDB;

	private DBConnection() {
		super();
	}

	public void afterCreate() {

		String host = "";

		if (!isOnOpenShift()) {
			// we are not on openshift
			System.out.println("Checking local Mongo");
			Mongo mongo = null;
			try {
				mongo = new Mongo("localhost", 27017);
				mongoDB = mongo.getDB("parks");
			} catch (UnknownHostException e) {
				System.out.println("Could not connect to Mongo on Localhost: "
						+ e.getMessage());
			}
		} else {
			// on openshift
			String mongoport = System.getenv("OPENSHIFT_MONGODB_DB_PORT");
			String user = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
			String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
			String db = System.getenv("OPENSHIFT_APP_NAME");
			int port = Integer.decode(mongoport);

			Mongo mongo = null;
			try {
				mongo = new Mongo(host, port);
			} catch (UnknownHostException e) {
				System.out.println("Couldn't connect to Mongo: "
						+ e.getMessage() + " :: " + e.getClass());
			}

			mongoDB = mongo.getDB(db);

			if (mongoDB.authenticate(user, password.toCharArray()) == false) {
				System.out.println("Failed to authenticate DB ");
			}
		}

	}

	public boolean isOnOpenShift(){
		String db = System.getenv("OPENSHIFT_APP_NAME");
		
		if ((db == null) || db.equalsIgnoreCase("")){
			return false;
		}
		return true;
	}
	
	public static DBConnection getInstance(){
		return _iMongoConnection;
	}

	public static void initialize(){
		_iMongoConnection = new DBConnection();
		_iMongoConnection.afterCreate();				
	}
	
	public DB getDB() {
		return mongoDB;
	}
	
}
