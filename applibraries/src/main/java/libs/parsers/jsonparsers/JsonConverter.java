package libs.parsers.jsonparsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import libs.parsers.jsonparsers.exceptions.CannotConvertObjectException;
import libs.parsers.jsonparsers.exceptions.InvalidJsonException;
import libs.parsers.jsonparsers.exceptions.NoSuchClassException;
import libs.simulationattributes.*;

import java.io.IOException;

public class JsonConverter {
  static private final String META_TYPE_KEY = "__type__";
  static private final String MAIN_OBJECT_KEY = "__mainObj__";

  public Object jsonToObject(String json) throws NoSuchClassException, InvalidJsonException {
    String className = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode wholeObj = objectMapper.readTree(json);
      if (!wholeObj.has(META_TYPE_KEY) && !wholeObj.has(MAIN_OBJECT_KEY)) {
        return null;
      }
      Crane crane;
      className = wholeObj.get(META_TYPE_KEY).asText();
      JsonNode mainObj = wholeObj.get(MAIN_OBJECT_KEY);
      Gson gson = new Gson();
      String a = mainObj.asText();
      Object parsedObj = gson.fromJson(a, Class.forName(className));
      return parsedObj;
    }
    catch (ClassNotFoundException e) {
      throw new NoSuchClassException("Cannot find " + className);
    }
    catch (IOException e) {
      throw new InvalidJsonException("Invalid json");
    }
  }

  public String objectToJson(Object object) throws CannotConvertObjectException {
    String className = object.getClass().getName();
    try {
      JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
      ObjectNode wholeJson = new ObjectNode(jsonNodeFactory);
      wholeJson.put(META_TYPE_KEY, className);
      ObjectMapper objectMapper = new ObjectMapper();
      String mainJsonObj = objectMapper.writeValueAsString(object);
      wholeJson.put(MAIN_OBJECT_KEY, mainJsonObj);
      return wholeJson.toString();
    }
    catch (JsonProcessingException e) {
      throw new CannotConvertObjectException("Cannot convert type " + className + " to Json");
    }
  }
}
