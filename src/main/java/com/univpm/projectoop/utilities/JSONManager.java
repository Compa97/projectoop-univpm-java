package com.univpm.projectoop.utilities;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

public class JSONManager {
    private JSONObject obj ;
    private JSONObject objI;
    private JSONArray objA;

    public JSONManager(String data) {
        try {
            obj = (JSONObject) JSONValue.parseWithException(data);
            objI = (JSONObject) (obj.get("result"));
            objA = (JSONArray) (objI.get("resources"));
        } catch (ParseException e) {
            System.out.println("Error during parsing");
            e.printStackTrace();
        }
    }

    public  void createCSV(){
        for (Object o : objA) {
            if (o instanceof JSONObject) {
                JSONObject o1 = (JSONObject) o;
                String format = (String) o1.get("format");
                String urlD = (String) o1.get("url");
                if (format.charAt(format.length()-3)=='C'&& format.charAt(format.length()-2)=='S' && format.charAt(format.length()-1)=='V') {
                  try {
                      download(urlD, "to_parse.csv");
                    } catch(Exception e){
                        System.out.println("Error");
                        e.printStackTrace();
                  }
                }
            }
        }
    }

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

