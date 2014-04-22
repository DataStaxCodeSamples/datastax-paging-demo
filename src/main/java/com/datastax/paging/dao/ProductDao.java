package com.datastax.paging.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.paging.model.Product;

public class ProductDao {

	private static Logger logger = LoggerFactory.getLogger( ProductDao.class );
	
	private Cluster cluster;
	private Session session;
	private static AtomicLong TOTAL_PRODUCTS = new AtomicLong(0);
	
	private static String keyspaceName = "datastax_paging_demo";
	private static String tableNameProduct = keyspaceName + ".products";

	private static final String SELECT_ALL_PRODUCTS = "Select * from " + tableNameProduct;
	private static final String INSERT_INTO_PRODUCT = "Insert into " + tableNameProduct
			+ " (productId, capacityleft, orderIds) values (?,?,?);";	

	private PreparedStatement insertStmtProduct;
	private PreparedStatement selectStmtProduct;

	public ProductDao(String[] contactPoints) {

		this.cluster = Cluster.builder().addContactPoints(contactPoints).build();
		this.session = cluster.connect();

		this.insertStmtProduct = session.prepare(INSERT_INTO_PRODUCT);
		this.selectStmtProduct = session.prepare(SELECT_ALL_PRODUCTS);
		
		this.insertStmtProduct.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.selectStmtProduct.setConsistencyLevel(ConsistencyLevel.QUORUM);
	}

	public void initializeProductTable(int noOfProducts, int totalStock) {
		BatchStatement batch = new BatchStatement();
		
		for (int i=0; i < noOfProducts; i++){
			
			batch.add(this.insertStmtProduct.bind("P" + i, totalStock, new HashSet<String>()));
			
			if (i % 1000 == 0){
				this.session.execute(batch);
				batch = new BatchStatement();
			}
		}
		this.session.execute(batch);
		
		logger.info("Inserted " + noOfProducts + " products.");
	}

	public void getAllProducts() {

		Statement stmt = new SimpleStatement("Select * from " + tableNameProduct);
		stmt.setFetchSize(100);
		ResultSet resultSet = this.session.execute(stmt);
		
		Iterator<Row> iterator = resultSet.iterator();
		
		while (iterator.hasNext()){
			
			Row row = iterator.next();
						
			logger.info(createProduct(row).toString());
			TOTAL_PRODUCTS.incrementAndGet();			
		}		
	}
	
	public long getTotalProducts(){
		return TOTAL_PRODUCTS.longValue();
	}

	private Product createProduct(Row row) {
		return new Product(row.getString("productId"), row.getInt("capacityLeft"), row.getSet("orderIds", String.class));
	}

	public void close() {
		this.session.close();
		this.cluster.close();
	}

}
