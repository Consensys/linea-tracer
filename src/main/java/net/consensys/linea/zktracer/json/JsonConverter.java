package net.consensys.linea.zktracer.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;

/** A wrapper class handling Jackson's {@link ObjectMapper} configuration. */
public class JsonConverter {

  @Getter private final ObjectMapper objectMapper;

  private JsonConverter(Builder builder) {
    this.objectMapper = builder.objectMapper;
  }

  /**
   * Serializes an object to a JSON string.
   *
   * @param object the object to be serialized
   * @return a JSON string representing the object's data
   * @throws JsonProcessingException n case of a serialization failure
   */
  public String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  /**
   * Deserializes a JSON string to a specified type.
   *
   * @param jsonString JSON string to be deserialized
   * @param clazz class type of the type for deserialization
   * @param <T> the deserialization type
   * @return an instance of the deserialized type
   * @throws JsonProcessingException in case of a deserialization failure
   */
  public <T> T fromJson(String jsonString, Class<T> clazz) throws JsonProcessingException {
    return objectMapper.readValue(jsonString, clazz);
  }

  /**
   * A factory for the {@link Builder} instance.
   *
   * @return an instance of {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder for {@link JsonConverter}. */
  public static class Builder {
    private ObjectMapper objectMapper;
    private final SimpleModule module;

    private Builder() {
      this.objectMapper = new ObjectMapper();
      this.module = new SimpleModule();
    }

    /**
     * Configures a custom jackson serializer for a specific type.
     *
     * @param type the class type targeted for custom serialization
     * @param serializer the serializer implementation handling the specified type
     * @param <T> the type targeted for custom serialization
     * @return the current instance of the builder
     */
    public <T> Builder addCustomSerializer(Class<T> type, JsonSerializer<T> serializer) {
      module.addSerializer(type, serializer);
      return this;
    }

    public <T> Builder enableYaml() {
      objectMapper = new ObjectMapper(new YAMLFactory());
      return this;
    }

    /**
     * Builds an instance of {@link JsonConverter}.
     *
     * @return an instance of {@link JsonConverter}.
     */
    public JsonConverter build() {
      objectMapper.registerModule(module);

      return new JsonConverter(this);
    }
  }
}
