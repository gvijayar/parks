package org.openshift.webservice;


//@RequestScoped
//@Path("/parks")
public class ParkWS {/*

	@Inject
	private DBConnection dbConnection;

	// get all the parks
	@GET()
	@Produces("application/json")
	public List getAllParks() {
		ArrayList allParksList = new ArrayList();
		DB db = dbConnection.getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");
		DBCursor cursor = parkListCollection.find();
		try {
			while (cursor.hasNext()) {
				DBObject dataValue = cursor.next();
				HashMap holder = new HashMap<String, Object>();
				holder.put("name", dataValue.get("Name"));
				holder.put("position", dataValue.get("pos"));
				holder.put("id", dataValue.get("_id").toString());
				allParksList.add(holder);

			}
		} finally {
			cursor.close();
		}

		return allParksList;
	}

	// ////////get a park given the id
	@GET()
	@Produces("application/json")
	@Path("park/{id}")
	public HashMap getAPark(@PathParam("id") String id) {

		DB db = dbConnection.getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");

		DBObject park = parkListCollection.findOne(new BasicDBObject().append(
				"_id", new ObjectId(id)));
		HashMap holder = new HashMap<String, Object>();
		holder.put("name", park.get("Name"));
		holder.put("position", park.get("pos"));
		holder.put("id", park.get("_id").toString());
		return holder;
	}

	// /////////get parks within a bounding box (used for map application)
	@GET
	@Produces("application/json")
	@Path("within")
	public List findParksWithin(@QueryParam("lat1") float lat1,
			@QueryParam("lon1") float lon1, @QueryParam("lat2") float lat2,
			@QueryParam("lon2") float lon2) {
		ArrayList<Map> allParksList = new ArrayList<Map>();
		DB db = dbConnection.getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");

		// make the query object
		BasicDBObject spatialQuery = new BasicDBObject();
		ArrayList posList = new ArrayList();
		ArrayList firstPair = new ArrayList();
		ArrayList secondPair = new ArrayList();
		firstPair.add(new Float(lon1));
		firstPair.add(new Float(lat1));
		secondPair.add(new Float(lon2));
		secondPair.add(new Float(lat2));

		posList.add(firstPair);
		posList.add(secondPair);

		BasicDBObject boxQuery = new BasicDBObject();
		boxQuery.put("$box", posList);

		spatialQuery.put("pos", new BasicDBObject("$within", boxQuery));

		System.out.println("within spatial query: " + spatialQuery.toString());

		DBCursor cursor = parkListCollection.find(spatialQuery);
		try {
			while (cursor.hasNext()) {
				DBObject dataValue = cursor.next();
				HashMap holder = new HashMap<String, Object>();
				holder.put("name", dataValue.get("Name"));
				holder.put("position", dataValue.get("pos"));
				holder.put("id", dataValue.get("_id").toString());
				allParksList.add(holder);
			}
		} finally {
			cursor.close();
		}

		return allParksList;

	}

	// /////////get parks near a coord ?lat=37.5&lon=-83.0
	@GET
	@Produces("application/json")
	@Path("near")
	public List findParksNear(@QueryParam("lat") float lat,
			@QueryParam("lon") float lon) {
		ArrayList<Map> allParksList = new ArrayList<Map>();
		DB db = dbConnection.getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");

		// make the query object
		BasicDBObject spatialQuery = new BasicDBObject();
		ArrayList posList = new ArrayList();
		posList.add(new Float(lon));
		posList.add(new Float(lat));
		spatialQuery.put("pos", new BasicDBObject("$near", posList));

		System.out.println("spatial query: " + spatialQuery.toString());

		DBCursor cursor = parkListCollection.find(spatialQuery);
		try {
			while (cursor.hasNext()) {
				DBObject dataValue = cursor.next();
				HashMap holder = new HashMap<String, Object>();
				holder.put("name", dataValue.get("Name"));
				holder.put("position", dataValue.get("pos"));
				holder.put("id", dataValue.get("_id").toString());
				allParksList.add(holder);
			}
		} finally {
			cursor.close();
		}

		return allParksList;

	}

	// //////get parks near a coord with a certain name
	@GET
	@Produces("application/json")
	@Path("name/near/{name}")
	public List findParksNearName(@PathParam("name") String name,
			@QueryParam("lat") float lat, @QueryParam("lon") float lon) {
		ArrayList<Map> allParksList = new ArrayList<Map>();
		DB db = dbConnection.getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");

		// make the query object
		BasicDBObject wholeQuery = new BasicDBObject();
		// first the regex
		Pattern namePattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
		wholeQuery.put("Name", namePattern);

		 then the spatial 
		ArrayList posList = new ArrayList();
		posList.add(new Float(lon));
		posList.add(new Float(lat));
		wholeQuery.put("pos", new BasicDBObject("$near", posList));

		System.out.println("whole query: " + wholeQuery.toString());

		DBCursor cursor = parkListCollection.find(wholeQuery);
		try {
			while (cursor.hasNext()) {
				DBObject dataValue = cursor.next();
				HashMap holder = new HashMap<String, Object>();
				holder.put("name", dataValue.get("Name"));
				holder.put("position", dataValue.get("pos"));
				holder.put("id", dataValue.get("_id").toString());
				allParksList.add(holder);
			}
		} finally {
			cursor.close();
		}

		return allParksList;

	}

	// /////still need the insert
	@POST
	@Path("park")
	@Consumes("application/json")
	@Produces("application/json")
	public HashMap insertAPark(Park park) {
		BasicDBObject parkObject = new BasicDBObject("Name", park.getName());
		parkObject.append("pos", park.getPos());

		DB db = dbConnection.getDB();
		DBCollection parkListCollection = db.getCollection("parkpoints");
		try {
			parkListCollection.insert(parkObject, WriteConcern.SAFE);
		} catch (Exception e) {
			System.out.println("threw an exception: " + e.getClass() + " :: "
					+ e.getMessage());
		}

		// now make it look pretty
		HashMap holder = new HashMap<String, Object>();
		holder.put("name", parkObject.get("Name"));
		holder.put("position", parkObject.get("pos"));
		holder.put("id", parkObject.get("_id").toString());
		return holder;

	}

	*//****** Just for testing purposes ***********//*
	@GET()
	@Path("/test")
	@Produces("text/plain")
	public String sayHello() {
		System.out.println("Where is this getting written");
		return "Hello World In Both Places";
	}

*/}
