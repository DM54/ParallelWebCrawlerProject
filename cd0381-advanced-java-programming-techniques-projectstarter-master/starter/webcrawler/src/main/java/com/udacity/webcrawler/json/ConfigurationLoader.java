package com.udacity.webcrawler.json;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
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
  public CrawlerConfiguration load() {
      String stringjson = "";
      Reader reader1 = new StringReader(stringjson);
    try(BufferedReader reader = new BufferedReader(new FileReader(path.toString()))){
       String data = reader.readLine();
       StringBuilder stringBuilder = new StringBuilder();
     while((data)!=null){
       data = reader.readLine();
       stringBuilder.append(data);
       stringjson = stringBuilder.toString();
       reader1 = new StringReader(stringjson);
     }

    }
    catch(IOException e){
      e.printStackTrace();
    }
    return read(reader1);
  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */

  public static CrawlerConfiguration read(Reader reader){
    CrawlerConfiguration crawlerConfiguration = null;
    JsonFactory factory = new JsonFactory();
    factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
    ObjectMapper objectMapper = new ObjectMapper(factory);
    try(JsonParser parser = factory.createParser(reader)){

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,true);
        crawlerConfiguration = objectMapper.readValue(parser, CrawlerConfiguration.class);

    }catch (JsonMappingException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }
    return  crawlerConfiguration;
  }
}
