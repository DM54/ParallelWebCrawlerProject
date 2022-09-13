package com.udacity.webcrawler.json;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Objects;
import java.io.FileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.io.*;
import java.io.StringReader;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
/**
 * A static utility class that loads a JSON configuration file.
 */
public final class ConfigurationLoader {

  private final Path path;

  /**
   * Create a {@link ConfigurationLoader} that loads configuration from the given {@link Path}.
   */
  public ConfigurationLoader(Path path) {
    this.path = Objects.requireNonNull(path);
  }

  /**
   * Loads configuration from this {@link ConfigurationLoader}'s path
   *
   * @return the loaded {@link CrawlerConfiguration}.
   */
  public CrawlerConfiguration load() throws IOException {

    try(BufferedReader reader = new BufferedReader(new FileReader(path.toString()))){
        return read(reader);
    }

  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */

  public static CrawlerConfiguration read(Reader reader){
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    try{
        return objectMapper.readValue(reader, CrawlerConfiguration.class);
    }catch (IOException e){
      e.printStackTrace();
      return new CrawlerConfiguration.Builder().build();
    }
  }
}
