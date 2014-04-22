package com.datastax.paging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.demo.utils.Timer;
import com.datastax.paging.dao.ProductDao;

public class Main {

	private static int NO_OF_PRODUCTS = 1000;
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public Main() {

		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
		String noOfProductsStr = PropertyHelper.getProperty("noOfProducts", "1000");
		String inStockStr =  PropertyHelper.getProperty("inStock", "1000");
		
		NO_OF_PRODUCTS = Integer.parseInt(noOfProductsStr);
		int totalStock = Integer.parseInt(inStockStr);	

		logger.info("Starting with " + NO_OF_PRODUCTS + " products and " + totalStock + " quantity in stock.");
		
		ProductDao dao = new ProductDao(contactPointsStr.split(","));
		dao.initializeProductTable(NO_OF_PRODUCTS, totalStock);
		
		Timer timer = new Timer();
		timer.start();
		printProducts(dao);
		timer.end();
		logger.info("Paging demo took " + timer.getTimeTakenSeconds() + " secs. Total Products : " + dao.getTotalProducts());

		dao.close();
		System.exit(0);
	}

	private void printProducts(ProductDao dao) {
		dao.getAllProducts();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}
