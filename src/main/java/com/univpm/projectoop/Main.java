package com.univpm.projectoop;

import com.univpm.projectoop.model.Deliveries;
import com.univpm.projectoop.utilities.CSVParser;
import com.univpm.projectoop.utilities.Connect;
import com.univpm.projectoop.utilities.JSONManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Applicazione SpringBoot (Entry point)
 */
@SpringBootApplication
public class Main {

	/**
	 * Oggetto pubblico in cui viene salvata la lista dei dati parsati da CSV
	 */
	public static Deliveries deliveries;

	/**
	 * Metodo main
	 * @param args Eventuali args da cmd (non implementato)
	 * @throws Exception Lanciato in caso di errori generici provenienti dai metodi utilizzati
	 */
	public static void main(String[] args) throws Exception {
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

