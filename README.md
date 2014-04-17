Paging Demo
====================

NOTE - this example requires apache cassandra version > 2.0.5 and the cassandra-driver-core version > 2.0.0

## Scenario

We want to insert lots of products to our product catalog and retrieve them all with a simple 'select * from products'.

For a more detailed description of automatic paging please see http://www.datastax.com/dev/blog/client-side-improvements-in-cassandra-2-0

## Schema Setup
Note : This will drop the keyspace "datastax_paging_demo" and create a new one. All existing data will be lost. 

To specify contact points use the contactPoints command line parameter e.g. '-DcontactPoints=192.168.25.100,192.168.25.101'
The contact points can take mulitple points in the IP,IP,IP (no spaces).

To create the a single node cluster with replication factor of 1 for standard localhost setup, run the following

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaSetup"

To run the insert

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.paging.Main"

To run the insert with more products please use

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.paging.Main" -DnoOfProducts=500000
		
To remove the tables and the schema, run the following.

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaTeardown"
    
    
    
