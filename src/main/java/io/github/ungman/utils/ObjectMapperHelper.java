package io.github.ungman.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapperHelper {
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @SneakyThrows
    public static <T> T readValueFromString(String jsonObject, Class<T> type, boolean isRuntimeExceptionThrow) {

        if (isRuntimeExceptionThrow && (jsonObject == null || jsonObject.isEmpty())) {
            throw new RuntimeException("Empty data. Cant parse string to object: " + type.getSimpleName());
        }
        return type.cast(mapper.readValue(jsonObject, type));
    }

    public static <T> T readValueFromString(String jsonObject, Class<T> type) {
        return readValueFromString(jsonObject, type, false);
    }

    @SneakyThrows
    public static <T> String writeObjectToString(T objectToString, Boolean isNotNullField) {
        if (isNotNullField)
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(objectToString);
    }

    public static <T> String writeObjectToString(T objectToString) {
        return writeObjectToString(objectToString, true);
    }

    @SneakyThrows
    public static <T> List<T> getListObjectFromJson(String data, Class<T> responseClass) {
        JsonNode arrJsonNode = mapper.readTree(data);
        List<T> listObject = new ArrayList<>();
        for (JsonNode jsonNode : arrJsonNode) {
            listObject.add(readValueFromString(jsonNode.toString(), responseClass));
        }
        return listObject;
    }

    @SneakyThrows
    public static String addFieldToJsonObject(String jsonObject, String fieldName, String data) {
        JsonNode jsonNode = mapper.readTree(jsonObject);
        if (jsonNode.isMissingNode()) {
            jsonNode = mapper.createObjectNode();
        }
        ((ObjectNode) jsonNode).put(fieldName, data);
        return mapper.writeValueAsString(jsonNode);
    }

    @SneakyThrows
    public static <T> T getField(String json, String fieldName, Class<T> tClass) {
        JsonNode jsonNode = mapper.readTree(json);
        return (T) readValueFromString(jsonNode.get(fieldName).toString(), tClass);
    }

}
