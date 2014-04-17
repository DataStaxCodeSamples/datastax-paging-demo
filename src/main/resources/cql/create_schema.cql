create keyspace if not exists datastax_paging_demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };

use datastax_paging_demo;

create table if not exists products(
    productId text,
    capacityleft int,
	orderIds set<text>,
	PRIMARY KEY (productId)
);

truncate products;

	
