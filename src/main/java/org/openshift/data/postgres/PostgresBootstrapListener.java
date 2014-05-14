package org.openshift.data.postgres;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.openshift.data.model.DataLoader;

@WebListener()
public class PostgresBootstrapListener implements ServletContextListener {

	@Resource(name = "jdbc/PostgreSQLDS")
	DataSource ds;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ConnectionManager.initialize(ds);
		
		DataLoader dataLoader = new DataLoader();
		dataLoader.setUpParksData();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	
}

