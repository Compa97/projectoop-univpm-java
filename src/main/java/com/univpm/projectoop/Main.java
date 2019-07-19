package com.univpm.projectoop;

import com.univpm.projectoop.model.Deliveries;
import com.univpm.projectoop.utilities.CSVParser;
import com.univpm.projectoop.utilities.Connect;
import com.univpm.projectoop.utilities.JSONManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
public class Main {

	/**
	 *
	 */
	public static Deliveries deliveries;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Connect connection = new Connect();
			connection.startConnect();
			JSONManager handler = new JSONManager(connection.getData());
			handler.createCSV();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		CSVParser p = new CSVParser();
		p.parse();
		deliveries = new Deliveries(CSVParser.getList());
		SpringApplication.run(Main.class, args);
	}
}

