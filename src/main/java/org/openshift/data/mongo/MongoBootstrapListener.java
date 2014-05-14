package org.openshift.data.mongo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class MongoBootstrapListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DBConnection.initialize();		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

