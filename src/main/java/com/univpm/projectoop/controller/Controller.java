package com.univpm.projectoop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

/**
 * Questa classe gestisce le richieste REST
 */
@RestController
public class Controller {

    /**
     * Contiene l'oggetto Collection che viene inizializzato nel Main
     */
    private final Deliveries source = deliveries;

    /**
     * Rappresenta la lista di istanze che viene ogni volta utilizzata per inserire gli elementi
     * corrispondenti al filtro scelto nella richiesta dall'utente
     */
    private final ArrayList <ArrayList<Delivery>> internalList = new ArrayList<>();

    /**
     * Insieme di campi accettati come "field" per il filtraggio senza insiemi di filtri in and o in or
     */
    private final String fields = "freq " + "unit " + "indic_PS " + "geo " + "2012 " + "2013 " + "2014 " + "2015 " + "2016 " + "2017 ";

    private final String operators = "$eq " + "$in " + "$nin " + "$bt " + "$not " + "$gt " + "$gte " + "$lt "+ "$lte ";
    /**
     * Lista che viene restituita in uscita
     */
    private ArrayList<Delivery> out = null;

    /**
     * Oggetto che trasforma la lista in JSON
     */
    private ObjectMapper map;


    /**
     * Path che restituisce i metadati (frequenza, unità di misura, codice spedizioni, paese di riferimento, vettore di dati)
     * @return JSON che elenca le informazioni contenute nel model
     * @throws JsonProcessingException Lanciato se ci sono errori nel parsing o nella generazione del JSON
     */
    @RequestMapping(value = "/meta", method = RequestMethod.GET, produces = "application/json")
    public String getMetadata() throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();
        JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(map);
        JsonSchema deliverySchema = schemaGenerator.generateSchema(Delivery.class);
        return map.writeValueAsString(deliverySchema);
    }

    /**
     * Path che restituisce la lista completa di oggetti
     * @return JSON che elenca tutti gli oggetti del dataset
     * @throws JsonProcessingException Lanciato se ci sono errori nel parsing o nella generazione del JSON
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public String getList() throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(deliveries.getDeliveriesList());
    }

    /**
     * Richiesta POST con possibilità opzionale di specificare l'operatore logico tra i filtri in and e in or
     * @param listConnector ("and" oppure "or")
     * @param filter (JSON da inserire nel body per il filtraggio)
     * @return JSON con la lista degli oggetti filtrata
     * @throws IOException Lanciato in caso di problemi di I/O
     * @throws JSONException Lanciato se ci sono problemi con le JSON API
     * @throws NoSuchMethodException Lanciato quando un metodo non viene trovato
     * @throws IllegalAccessException Lanciato quando un'applicazione non ha accesso alla
     * definizione di una classe, un campo, un metodo o un costruttore
     * @throws InvocationTargetException Lanciato se un metodo chiamato contiene a sua volta un'eccezione
     */
    @RequestMapping(value = {"/list", "/list/{listConnector}"}, method = RequestMethod.POST, produces = "application/json")
    public String getList(@PathVariable Optional<String> listConnector, @RequestBody(required = false) String filter) throws IOException, JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Delivery> filtered = null;
        ObjectMapper map = new ObjectMapper();

        if (filter != null) {
            JSONObject obj = new JSONObject(filter);

            filtered = manageFilter(obj, listConnector);

            if (filtered != null) {
                if (filtered.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no result for your filtering query!");
                }
                return map.writeValueAsString(filtered);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown error: list is empty.");
            }
        } else {
            if (listConnector.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "listConnector path is unnecessary if body request is empty.");
            }
            return map.writeValueAsString(deliveries.getDeliveriesList());
        }
    }

    /**
     * Path che restituisce tutte le statische del dataset non filtrato
     * @return JSON contenente l'elenco delle statistiche per tutti gli anni
     * @throws JsonProcessingException Lanciato se ci sono problemi con le JSON API
     */
    @RequestMapping(value = "/stats", method = RequestMethod.GET, produces = "application/json")
    public String getStat() throws JsonProcessingException {
        ArrayList<Stats> allStat = new ArrayList<>(6);
        ObjectMapper map = new ObjectMapper();

        for (int i = 2012; i <= 2017; i++) {
            allStat.add(new Stats(i));
        }
        return map.writeValueAsString(allStat);
    }

    /**
     * Path che restituisce le statische del dataset per l'anno indicato
     * @param year Anno richiesto per le statistiche
     * @return JSON contenente l'elenco delle statistiche per l'anno richiesto
     * @throws JsonProcessingException Lanciato se ci sono problemi con le JSON API
     */
    @RequestMapping(value = "/stats/{year}", method = RequestMethod.GET, produces = "application/json")
    public String getStat(@PathVariable("year") String year) throws JsonProcessingException {
        out = null;
        return parseYear(year);
    }

    /**
     * Richiesta POST che permette di ottenere le statistiche filtrate, eventualmente per anno
     * @param year Anno su cui viene richiesta la statistica
     * @param listConnector ("and" oppure "or")
     * @param filter JSON contenuto nel body utilizzato per filtrare
     * @return JSON che elenca le statistiche filtrate
     * @throws JSONException Lanciato se ci sono problemi con le JSON API
     * @throws IOException Lanciato in caso di problemi di I/O
     * @throws NoSuchMethodException Lanciato quando un metodo non viene trovato
     * @throws IllegalAccessException Lanciato quando un'applicazione non ha accesso alla
     * definizione di una classe, un campo, un metodo o un costruttore
     * @throws InvocationTargetException Lanciato se un metodo chiamato contiene a sua volta un'eccezione
     */
    @RequestMapping(value = {"/stats", "/stats/year/{year}", "/stats/listConnector/{listConnector}", "/stats/year/{year}/listConnector/{listConnector}"}, method = RequestMethod.POST, produces = "application/json")
    public String getStat(@PathVariable Optional <String> year, @PathVariable Optional<String> listConnector, @RequestBody(required = false) String filter) throws JSONException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        out = null;
        ArrayList<Stats> allStat = new ArrayList<>();

        if (filter != null) {

            if (filter.split("or").length <= 2 && filter.split("and").length <= 2) {

                JSONObject jOb = new JSONObject(filter);
                out = manageFilter(jOb, listConnector);

                if (year.isPresent()) {
                    // 1) FILTER NOT NULL - YEAR NOT NULL
                    return parseYear (year.get());

                } else {
                    // 2) FILTER NOT NULL - YEAR NULL
                    map = new ObjectMapper();
                    Deliveries d = new Deliveries(out);
                    if (d.getDeliveriesList().isEmpty()){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no result for your filtering query!");
                    }
                    for (int i = 2012; i <= 2017; i++) {
                        allStat.add(new Stats(i, d));
                    }
                    return map.writeValueAsString(allStat);

                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON: you can't use two or more disjunctive or conjunctive forms separated.");
            }
        }
        else {
            if (listConnector.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "listConnector path is unnecessary if body request is empty.");
            }
            if (year.isPresent()) {
                // 3) FILTER NULL - YEAR NOT NULL
                out = null;
                return parseYear (year.get());
            } else {
                // 4) FILTER NULL - YEAR NULL
                return getStat();
            }
        }
    }

    /**
     * Metodo che gestisce il parsing del body request in formato JSON
     * @param userRequest JSON utilizzato per filtrare
     * @param listConnector Eventuale connettore tra i due insiemi di condizioni in and o in or
     * @return Lista degli oggetti filtrata
     * @throws JSONException Lanciato se ci sono problemi con le JSON API
     * @throws NoSuchMethodException Lanciato quando un metodo non viene trovato
     * @throws IllegalAccessException Lanciato quando un'applicazione non ha accesso alla
     * definizione di una classe, un campo, un metodo o un costruttore
     * @throws InvocationTargetException Lanciato se un metodo chiamato contiene a sua volta un'eccezione
     */
    private ArrayList<Delivery> manageFilter (JSONObject userRequest, Optional<String> listConnector) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList <ArrayList<Delivery>> filtered = new ArrayList<>();

        JSONObject element;
        JSONArray parameters, getIkeys, getKeys = userRequest.names();
        String operator;

        //ITERAZIONE SU userRequest (oggetto esterno con and e or)
        for (int i = 0; i < getKeys.length(); i++) {

            operator = getKeys.getString(i);
            userRequest.keys();

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
            }
            else {

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
            if(!(listConnector.get().equals("and") || listConnector.get().equals("or"))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "listConnector path is uncorrect!");
            }
            if (listConnector.get().equalsIgnoreCase("and")) {
                return source.and(filtered);
            } else if (listConnector.get().equalsIgnoreCase("or")) {
                return source.or(filtered);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The listConnector can be only AND or OR!");
            }
        }
        else {
            return source.or(filtered);
        }
    }

    /**
     * Effettua il parsing dell'oggetto più interno della richiesta di filtraggio (se ve ne sono
     * multiple in and o in or è l'istanza del JSON Array, altrimenti è la singola condizione)
     * @param getIkeys Array con i nomi delle chiavi del JSON
     * @param element JSON object interno
     * @throws JSONException Lanciato se ci sono problemi con le JSON API
     * @throws NoSuchMethodException Lanciato quando un metodo non viene trovato
     * @throws IllegalAccessException Lanciato quando un'applicazione non ha accesso alla
     * definizione di una classe, un campo, un metodo o un costruttore
     * @throws InvocationTargetException Lanciato se un metodo chiamato contiene a sua volta un'eccezione
     */
    private void fetchFieldObject (JSONArray getIkeys, JSONObject element) throws JSONException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int k = 0; k < getIkeys.length(); k++) {

            String field = getIkeys.getString(k);

            if (!fields.contains(field)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong field in your JSON request!");
            }

            JSONObject condition = element.getJSONObject(field);
            String op = condition.keys().next().toString();
            Object value = condition.getString(condition.keys().next().toString());

            if (!operators.contains(op)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong operator in your JSON request!");
            }

            internalList.add(source.filterField(field, op, value));
        }
    }

    /**
     * Effettua il parsing dell'anno richiesto come path e restituisce le statistiche
     * @param year Stringa anno individuata nell'URL
     * @return Statistiche per l'anno indicato
     */
    private String parseYear (String year){

        try {
            int yearP = Integer.parseInt(year);
            if (yearP >= 2012 && yearP <= 2017) {
                try {
                    map = new ObjectMapper();
                    Stats statistic;
                    if (out != null) {
                        if (out.isEmpty()){
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no result for your filtering query!");
                        }
                        statistic = new Stats(yearP, new Deliveries(out));
                        map.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                        return map.writeValueAsString(statistic);
                    } else {
                        statistic = new Stats(yearP);
                        return map.writeValueAsString(statistic);
                    }
                } catch (JsonProcessingException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while producing your JSON result.");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This year isn't in dataset.");
            }
        } catch (NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is not an year!");
        }
    }

}
