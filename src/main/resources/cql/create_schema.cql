create keyspace datastax_paging_demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };

use datastax_paging_demo;

create table products(
    productId text,
    capacityleft int,
	orderIds set<text>,
	PRIMARY KEY (productId)
);

	
