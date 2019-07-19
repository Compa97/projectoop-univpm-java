package com.univpm.projectoop.utilities;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.springframework.boot.json.JsonParseException;

/**
 * Classe che gestisce il parsing del JSON
 */
public class JSONManager {

    /**
     * Oggetto JSON che contiene le "resources"
     */
    private JSONArray objA;

    /**
     * Costruttore che inizializza objA
     * @param data Stringa contenente il JSON da parsare
     * @throws ParseException Lanciato in caso di errori nel parsing del JSON
     */
    public JSONManager(String data) throws ParseException {
        JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
        JSONObject objI = (JSONObject) (obj.get("result"));
        objA = (JSONArray) (objI.get("resources"));
    }

    /**
     * Metodo che riconosce la risorsa contenente il CSV e ne effettua il download
     * @throws Exception Lanciato in caso di errori nel download
     */
    public  void createCSV() throws Exception {
        for (Object o : objA) {
            if (o instanceof JSONObject) {
                JSONObject o1 = (JSONObject) o;
                String format = (String) o1.get("format");
                String urlD = (String) o1.get("url");
                if (format.charAt(format.length()-3)=='C'&& format.charAt(format.length()-2)=='S' && format.charAt(format.length()-1)=='V') {
                    download(urlD, "to_parse.csv");
                }
            }
        }
    }

    /**
     * Metodo per effettuare il download di un file
     * @param url URL da cui scaricare
     * @param fileName Nome da assegnare al file
     * @throws Exception Lanciato in caso di errori nel salvataggio del file
     */
    private void download(String url, String fileName) throws Exception {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            try {
                Files.copy(in, Paths.get(fileName));
                System.out.println("Download phase finished");
            }catch (FileAlreadyExistsException e){
                System.out.println("File with the same name is already existing");
            }
        }
    }

}

