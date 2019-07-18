package com.univpm.projectoop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.univpm.projectoop.model.Deliveries;
import com.univpm.projectoop.model.Delivery;
import com.univpm.projectoop.utilities.Stats;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;

import static com.univpm.projectoop.Main.deliveries;

@RestController
public class Controller {

    private final Deliveries source = deliveries;
    private final ArrayList <ArrayList<Delivery>> internalList = new ArrayList<>();

    @RequestMapping(value = "/meta", method = RequestMethod.GET, produces = "application/json")
    String getMetadata() throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();
        JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(map);
        JsonSchema deliverySchema = schemaGenerator.generateSchema(Delivery.class);
        return map.writeValueAsString(deliverySchema);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    String getList() throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(deliveries.getDeliveriesList());
    }

    @RequestMapping(value = {"/list", "/list/{listConnector}"}, method = RequestMethod.POST, produces = "application/json")
    String getList(@PathVariable Optional<String> listConnector, @RequestBody(required = false) String filter) throws IOException, JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Delivery> filtered = null;
        ObjectMapper map = new ObjectMapper();

        if (filter != null) {
            JSONObject obj = new JSONObject(filter);
            filtered = manageFilter(obj, listConnector);

            if (filtered.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no result for your filtering query!");
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

        for (int i = 2012; i <= 2017; i++) {
            allStat.add(new Stats(i));
        }
        return map.writeValueAsString(allStat);
    }

    @RequestMapping(value = "/stats/{year}", method = RequestMethod.GET, produces = "application/json")
    public String getStat(@PathVariable("year") String year) throws JsonProcessingException {
        int yearP;

        try{
            yearP = Integer.parseInt(year);
        } catch (NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not an year!");
        }

        if (yearP >= 2012 && yearP <= 2017) {
            ObjectMapper map = new ObjectMapper();
            Stats statistic = new Stats(yearP);
            return map.writeValueAsString(statistic);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This year isn't in dataset.");
        }
    }

    @RequestMapping(value = {"/stats", "/stats/{year}", "/stats/{year}/{listConnector}"}, method = RequestMethod.POST, produces = "application/json")
    public String getStat(@PathVariable Optional <String> year, @PathVariable Optional<String> listConnector, @RequestBody(required = false) String filter) throws JSONException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Delivery> out = null;
        ArrayList<Stats> allStat = new ArrayList<>();
        ObjectMapper map = new ObjectMapper();
        Stats statistic;
        int yearP;

        //TODO: due operatori uguali
        if (year.isPresent()) {

            try{
                yearP = Integer.parseInt(year.get());
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

    private ArrayList<Delivery> manageFilter (JSONObject userRequest, Optional<String> listConnector) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList <ArrayList<Delivery>> filtered = new ArrayList<>();

        JSONObject element;
        JSONArray parameters, getIkeys, getKeys = userRequest.names();
        String operator;

        //ITERAZIONE SU userRequest (oggetto esterno con and e or)
        for (int i = 0; i < getKeys.length(); i++) {

            operator = getKeys.getString(i);

            if (operator.equals("$or") || operator.equals("$and")) {

                try {
                    parameters = userRequest.getJSONArray(operator);

                    if (parameters != null) {

                        //ITERAZIONE SU parameters (Array che contiene le singole condizioni in and o in or)
                        for (int j = 0; j < parameters.length(); j++) {

                            element = parameters.getJSONObject(j);
                            getIkeys = element.names();

                            //ITERAZIONE SU element (CAMPO e oggetto operatore-valore)
                            fetchFieldObject(getIkeys, element);
                        }

                        if (operator.equals("$or")) {
                            filtered.add(source.or(internalList));
                        } else {
                            filtered.add(source.and(internalList));
                        }
                    }
                    internalList.clear();
                } catch (JsonParseException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON malformed!");
                }
            } else {

                String fields = "freq " + "unit " + "indic_PS " + "geo " + "2012 " + "2013 " + "2014 " + "2015 " + "2016 " + "2017 ";

                if (fields.contains(operator)){
                    internalList.clear();
                    fetchFieldObject(userRequest.names(), userRequest);
                    return source.or(internalList);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong field in your JSON request!");
                }
            }
        }

        if (listConnector.isPresent()) {
            if (listConnector.get().equalsIgnoreCase("and")) {
                return source.and(filtered);
            } else if (listConnector.get().equalsIgnoreCase("or")) {
                return source.or(filtered);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The listConnector can be only AND or OR!");
            }
        } else {
            return source.or(filtered);
        }
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
