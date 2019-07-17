package com.univpm.projectoop.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.univpm.projectoop.model.Deliveries;
import com.univpm.projectoop.model.Delivery;
import com.univpm.projectoop.utilities.Stats;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.univpm.projectoop.Main.deliveries;

@RestController
public class Controller {

    private Deliveries source = deliveries;
    private ArrayList <ArrayList<Delivery>> internalList = new ArrayList<>();

    @RequestMapping(value = "/meta", method = RequestMethod.GET, produces = "application/json")
    String getMetadata() {
        try {
            ObjectMapper map = new ObjectMapper();
            JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(map);
            JsonSchema deliverySchema = schemaGenerator.generateSchema(Delivery.class);
            return map.writeValueAsString(deliverySchema);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
            return ("Errore");
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    String getList() throws IOException, JSONException, ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Delivery> filtered = null;
        ObjectMapper map = new ObjectMapper();

        return map.writeValueAsString(deliveries.getDeliveriesList());
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    String getList(@RequestParam (required = false, defaultValue = "or") String listConnector, @RequestBody(required = false) String filter) throws IOException, JSONException, ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Delivery> filtered = null;
        ObjectMapper map = new ObjectMapper();

        if (filter != null) {

            JSONObject obj = new JSONObject(filter);
            filtered = manageFilter(obj, listConnector);

            if (filtered == null){
                return "There is no result for your filtering query!";
            }
            return map.writeValueAsString(filtered);
        } else {
            return map.writeValueAsString(deliveries.getDeliveriesList());
        }
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET, produces = "application/json")
    public String getStat() throws JsonProcessingException {
        ArrayList<Stats> allStat = new ArrayList<>();
        ObjectMapper map = new ObjectMapper();

        map.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        map.enable(SerializationFeature.INDENT_OUTPUT);
        for (int i = 2012; i <= 2017; i++) {
            allStat.add(new Stats(i));
        }
        return map.writeValueAsString(allStat);
    }

    @RequestMapping(value = "/stats/{year}", method = RequestMethod.GET, produces = "application/json")
    public String getStat(@PathVariable("year") String year) throws IOException {
        Integer yearP;

        try{
            yearP = Integer.parseInt(year);
        } catch (Exception e){
            e.printStackTrace();
            return "Formato anno non corretto!";
        }

        if (yearP >= 2012 && yearP <= 2017) {
            ObjectMapper map = new ObjectMapper();
            Stats statistic = new Stats(yearP);
            return map.writeValueAsString(statistic);
        } else {
            return "Anno non valido!";
        }
    }

    @RequestMapping(value = "/stats", method = RequestMethod.POST, produces = "application/json")
    public String getStat(@RequestParam(required = false) String year, @RequestParam (required = false, defaultValue = "or") String listConnector, @RequestBody(required = false) String filter) throws JSONException, ParseException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Delivery> out = null;
        ArrayList<Stats> allStat = new ArrayList<>();
        ObjectMapper map = new ObjectMapper();
        Stats statistic;
        Integer yearP;

        if (year != null) {

            try{
                yearP = Integer.parseInt(year);
            } catch (Exception e){
                e.printStackTrace();
                return "Formato anno non corretto!";
            }

            if (yearP >= 2012 && yearP <= 2017) {
                try {
                    if (filter != null) {
                        JSONObject jOb = new JSONObject(filter);
                        out = manageFilter(jOb, listConnector);
                    }
                    if (out != null) {
                        statistic = new Stats(yearP, new Deliveries(out));
                        return map.writeValueAsString(statistic);
                    } else {
                        statistic = new Stats(yearP);
                        return map.writeValueAsString(statistic);
                    }
                } catch (JsonProcessingException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            } else {
                return "Anno non valido!";
            }
        } else if (filter != null) {
            JSONObject jOb = new JSONObject(filter);
            out = manageFilter(jOb, listConnector);
            Deliveries d = new Deliveries(out);
            for (int i = 2012; i <= 2017; i++) {
                allStat.add(new Stats(i, d));
            }
            return map.writeValueAsString(allStat);
        } else {
            return getStat();
        }
    }

    private ArrayList<Delivery> manageFilter (JSONObject userRequest, String listConnector) throws ParseException, JSONException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList <ArrayList<Delivery>> filtered = new ArrayList<>();

        JSONObject element;
        JSONArray parameters, getIkeys, getKeys = userRequest.names();
        String operator = null;

        //ITERAZIONE SU userRequest (oggetto esterno con and e or)
        for (int i = 0; i < getKeys.length(); i++) {

            //TODO: funzione per gestire l'assenza di and oppure or esterni
            operator = getKeys.getString(i);

            if (operator.equals("$or") || operator.equals("$and")) {

                try {
                    parameters = (JSONArray) userRequest.getJSONArray(operator);

                    if (parameters != null) {

                        //ITERAZIONE SU parameters (Array che contiene le singole condizioni in and o in or)
                        for (int j = 0; j < parameters.length(); j++) {

                            element = parameters.getJSONObject(j);
                            getIkeys = element.names();

                            //ITERAZIONE SU element (CAMPO e oggetto operatore-valore)
                            fetchFieldObject(getIkeys, element);
                        }

                        if (operator.equals("$or")) {
                            //orList = source.or(internalList);
                            filtered.add(source.or(internalList));
                        } else if (operator.equals("$and")) {
                            //andList = source.and(internalList);
                            filtered.add(source.and(internalList));
                        }
                    }
                    internalList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                String fields = "freq " + "unit " + "indic_PS " + "geo " + "2012 " + "2013 " + "2014 " + "2015 " + "2016 " + "2017 ";

                if (fields.contains(operator)){
                    internalList.clear();
                    fetchFieldObject(userRequest.names(), userRequest);
                    return source.or(internalList);
                }
            }

        }

        if (listConnector.equals("and")){
            return source.and(filtered);
        }

        return source.or(filtered);
    }

    private void fetchFieldObject (JSONArray getIkeys, JSONObject element) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int k = 0; k < getIkeys.length(); k++) {

            String field = getIkeys.getString(k);
            JSONObject condition = element.getJSONObject(field);
            String op = condition.keys().next().toString();
            Object value = condition.getString(condition.keys().next().toString());

            internalList.add(source.filterField(field, op, value));
        }
    }

}
