package org.openshift.data.postgres;

public class DataManager {

	public void getParkCoordinates(String lon1, String lat1, String lon2, String lat2){
		
		String query = "SELECT gid, "
							+ "name, "
							+ "ST_X(the_geom) as lon,"
							+ "ST_Y(the_geom) as lat "
					+ "FROM parkdata t "
					+ "WHERE ST_Intersects( ST_MakeEnvelope("+ lon1 + ", " + lat1 + ", " + lon2 + ", "+lat2 + ", 4326), "
							+ "t.the_geom) LIMIT 50";
	}
	
	public String seedDatabase(){
		String insert = "INSERT into parkdata (name, the_geom) "
					+ "VALUES (?, ST_GeomFromText(?, 4326))";
		return insert;
	}

	public String installGisExtensions(){
		return ("CREATE EXTENSION postgis");
	}
	
	public String initializeDatabase(){
		return "CREATE TABLE parkdata ( gid serial NOT NULL, "
															+ "name character varying(240), "
															+ "the_geom geometry, "
															+ "CONSTRAINT parkdata_pkey "
															+ "PRIMARY KEY (gid), "
															+ "CONSTRAINT enforce_dims_geom "
															+ "CHECK (st_ndims(the_geom) = 2), "
															+ "CONSTRAINT enforce_geotype_geom "
															+ "CHECK (geometrytype(the_geom) = 'POINT'::text OR the_geom IS NULL),"
															+ "CONSTRAINT enforce_srid_geom "
															+ "CHECK (st_srid(the_geom) = 4326) ) "
															+ "WITH ( OIDS=FALSE )";
	}
	
}
