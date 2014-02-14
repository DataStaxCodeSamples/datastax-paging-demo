package com.datastax.demo;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.demo.utils.FileUtils;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;

public abstract class RunCQLFile {

	static final Logger LOG = Logger.getLogger("SchemaSetup");
	static String CREATE_KEYSPACE;
	static String DROP_KEYSPACE;

	private Cluster cluster;
	private Session session;
	private String CQL_FILE;

	RunCQLFile(String cqlFile) {
		
		System.out.println("Running file " + cqlFile);
		this.CQL_FILE = cqlFile;
		
		String contactPointsStr = System.getProperty("contactPoints");
		if (contactPointsStr == null) {
			contactPointsStr = "127.0.0.1";
		}

		cluster = Cluster.builder().addContactPoints(contactPointsStr.split(",")).build();
		session = cluster.connect();	
	}
	
	void internalSetup() {
		this.runfile();		
	}
	
	void runfile() {
		String readFileIntoString = FileUtils.readFileIntoString(CQL_FILE);
		
		String[] commands = readFileIntoString.split(";");
		
		for (String command : commands){
			
			String cql = command.trim();
			
			if (cql.isEmpty()){
				continue;
			}
			
			if (cql.toLowerCase().startsWith("drop") && cql.toLowerCase().startsWith("truncate")){
				this.runAllowFail(cql);
			}else{
				this.run(cql);
			}			
		}
	}

	void runAllowFail(String cql) {
		try {
			run(cql);
		} catch (InvalidQueryException e) {
			LOG.log(Level.WARNING, "Ignoring exception - " + e.getMessage());
		}
	}

	void run(String cql){
		LOG.info("Running : " + cql);
		session.execute(cql);
	}

	void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (Exception e) {
		}
	}

	
	void shutdown() {
		session.shutdown();
		cluster.shutdown();
	}
}
