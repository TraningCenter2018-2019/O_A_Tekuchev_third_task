package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import java.io.IOException;

public class JsonConverter {
  static private final String META_TYPE_KEY = "__type__";
  static private final String MAIN_OBJECT_KEY = "__mainObj__";

  public Object jsonToObject(String json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode wholeObj = objectMapper.readTree(json);
      if (!wholeObj.has(META_TYPE_KEY) && !wholeObj.has(MAIN_OBJECT_KEY)) {
        return null;
      }
      String className = wholeObj.get(META_TYPE_KEY).asText();
      JsonNode mainObj = wholeObj.get(MAIN_OBJECT_KEY);
      Gson gson = new Gson();
      String a = mainObj.asText();
      Object parsedObj = gson.fromJson(a, Class.forName(className));
      return parsedObj;
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String objectToJson(Object object) {
    try {
      JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
      ObjectNode wholeJson = new ObjectNode(jsonNodeFactory);
      wholeJson.put(META_TYPE_KEY, object.getClass().getName());
      ObjectMapper objectMapper = new ObjectMapper();
      String mainJsonObj = objectMapper.writeValueAsString(object);
      wholeJson.put(MAIN_OBJECT_KEY, mainJsonObj);
      return wholeJson.toString();
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
