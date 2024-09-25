package jace.shim.testlab.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public final class JsonUtils {

    private static final ObjectMapper MAPPER;
    private static final ObjectMapper IGNORE_NULL_MAPPER;

    static {
        MAPPER = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                ZonedDateTime offsetDateTime = value.atZone(ZoneId.systemDefault());
                jsonGenerator.writeString(convertDateToString(offsetDateTime));
            }
        });

        javaTimeModule.addSerializer(ZonedDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(
                ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
                jsonGenerator.writeString(convertDateToString(zonedDateTime));
            }
        });

        javaTimeModule.addDeserializer(ZonedDateTime.class, new JsonDeserializer<>() {
            @Override
            public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws
                IOException {
                return DateUtils.of(p.getText(), DateUtils.KST_DATE_FORMAT);
            }
        });

        MAPPER.registerModule(javaTimeModule);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        IGNORE_NULL_MAPPER = new ObjectMapper();
        IGNORE_NULL_MAPPER.registerModule(javaTimeModule);
        IGNORE_NULL_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        IGNORE_NULL_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        IGNORE_NULL_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static String convertDateToString(ZonedDateTime dateTime) {
        return DateUtils.parseKstDateFormat(dateTime);
    }

    private JsonUtils() {
        throw new UnsupportedOperationException("Unable to create instance");
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static ObjectMapper getIgnoreNullMapper() {
        return IGNORE_NULL_MAPPER;
    }

    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null) {
            return null;
        }

        try {
            return MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }

        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(JsonNode jsonNode, Class<T> clazz) {
        if (jsonNode == null) {
            return null;
        }

        try {
            return MAPPER.readValue(jsonNode.traverse(), clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(InputStream inputStream, TypeReference<T> typeReference) {
        if (inputStream == null) {
            return null;
        }

        try {
            return MAPPER.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        }

        try {
            return MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(JsonNode jsonNode, TypeReference<T> typeReference) {
        if (jsonNode == null) {
            return null;
        }

        try {
            return MAPPER.readValue(jsonNode.traverse(), typeReference);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(JsonNode jsonNode, CollectionLikeType collectionLikeType) {
        if (jsonNode == null) {
            return null;
        }

        try {
            return MAPPER.readValue(jsonNode.traverse(), collectionLikeType);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(JsonNode jsonNode, MapLikeType mapLikeType) {
        if (jsonNode == null) {
            return null;
        }

        try {
            return MAPPER.readValue(jsonNode.traverse(), mapLikeType);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T fromJson(byte[] bytes, TypeReference<T> typeReference) {
        if (isEmptyArray(bytes)) {
            return null;
        }

        try {
            return MAPPER.readValue(bytes, typeReference);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    private static boolean isEmptyArray(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }

    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        if (isEmptyArray(bytes)) {
            return null;
        }

        try {
            return MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static JsonNode fromJson(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        try {
            return MAPPER.readTree(inputStream);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static JsonNode fromJson(String json) {
        if (json == null) {
            return null;
        }

        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> List<T> fromJsonArray(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null) {
            return Collections.emptyList();
        }

        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);

        try {
            return MAPPER.readValue(inputStream, collectionType);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        if (json == null) {
            return Collections.emptyList();
        }

        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);

        try {
            return MAPPER.readValue(json, collectionType);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static String toJson(final Object object, boolean useNullValueField) {
        return doWriteValueAsString(object, useNullValueField ? getMapper() : getIgnoreNullMapper());
    }

    public static String toJson(final Object object) {
        return toJson(object, true);

    }

    private static String doWriteValueAsString(final Object object, final ObjectMapper mapper) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new JsonEncodeException(e);
        }
    }

    public static byte[] toJsonByte(final Object object) {
        try {
            return MAPPER.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new JsonEncodeException(e);
        }
    }

    public static String toPrettyJson(final Object object) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            throw new JsonEncodeException(e);
        }
    }

    public static <T> T jsonStringToObject(final String jsonString, Class<T> clazz) {
        try {
            return MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException ex) {
            throw new JsonEncodeException(ex);
        }
    }

    public static String get(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }

        return jsonNode.asText();
    }

    public static <T> T convertValue(Object obj, TypeReference<T> clazz) {
        return MAPPER.convertValue(obj, clazz);
    }

    public static class JsonEncodeException extends RuntimeException {

        private static final long serialVersionUID = 4975703115049362769L;

        public JsonEncodeException(Throwable cause) {
            super(cause);
        }
    }

    public static class JsonDecodeException extends RuntimeException {

        private static final long serialVersionUID = -2651564042039413190L;

        public JsonDecodeException(Throwable cause) {
            super(cause);
        }
    }
}
