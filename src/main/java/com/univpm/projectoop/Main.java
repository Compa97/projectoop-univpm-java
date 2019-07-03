package com.univpm.projectoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		try {
			Connect Connection = new Connect();
			Connection.startConnect();
			JSONManager handler = new JSONManager(Connection.getData());
			handler.createCSV();
		} catch (NullPointerException e){
			System.out.println("Error");
			e.printStackTrace();
		}
		CSVParser parser = new CSVParser();
		CSVParser.parse();
		//SpringApplication.run(Main.class, args);
	}
}

