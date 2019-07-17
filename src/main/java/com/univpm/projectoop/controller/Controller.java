package com.univpm.projectoop.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.univpm.projectoop.model.Deliveries;
import com.univpm.projectoop.model.Delivery;
import com.univpm.projectoop.utilities.Stats;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.univpm.projectoop.Main.deliveries;

@RestController
public class Controllers {

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
}