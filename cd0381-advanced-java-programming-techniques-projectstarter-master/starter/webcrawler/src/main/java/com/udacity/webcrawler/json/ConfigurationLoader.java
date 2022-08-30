package com.udacity.webcrawler.json;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Objects;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.io.*;
import java.io.StringReader;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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
    // TODO: Fill in this method.
      String stringjson = "";
      Reader reader1 = null;
      ClassLoader classLoader = getClass().getClassLoader();
      File file = new File(classLoader.getResource(path.toString()).getFile());
    try(BufferedReader reader = new BufferedReader(new FileReader(file))){
        StringBuilder stringBuilder = new StringBuilder();
       String data = reader.readLine();
     while((data)!=null){
         stringBuilder.append(data);
       data = reader.readLine();
     }
     stringjson = stringBuilder.toString();
     reader1 = new StringReader(stringjson);
     //read(reader1);

    }
    catch(IOException e){
      e.printStackTrace();
    }
    return read(reader1);
   //return new CrawlerConfiguration.Builder().build();
  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */

  public static CrawlerConfiguration read(Reader reader){
    // This is here to get rid of the unused variable warning.
   // Objects.requireNonNull(reader);

    CrawlerConfiguration crawlerConfiguration = null;
    JsonFactory factory = new JsonFactory();
    factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
    ObjectMapper objectMapper = new ObjectMapper(factory);
    // TODO: Fill in this method
    try(JsonParser parser = factory.createParser(reader)){
        while(!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if(!JsonToken.END_OBJECT.equals(jsonToken)){
                if(JsonToken.FIELD_NAME.equals(jsonToken)){
                    String fieldname = parser.getCurrentName();
                    if(crawlerConfiguration.getStartPages().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                        if (!JsonToken.END_ARRAY.equals(jsonToken)) {
                            //parser.getValueAsString()=crawlerConfiguration.getStartPages();
                        }
                    }
                       else if(crawlerConfiguration.getIgnoredUrls().equals(fieldname)){
                            jsonToken = parser.nextToken();
                            if(!JsonToken.END_ARRAY.equals(jsonToken)){
                             //  parser.getValueAsString()= crawlerConfiguration.getIgnoredUrls();
                            }
                    }else if(crawlerConfiguration.getIgnoredWords().equals(fieldname)) {
                             jsonToken = parser.nextToken();
                        if (!JsonToken.END_ARRAY.equals(jsonToken)) {
                           //parser.getValueAsString()= crawlerConfiguration.getIgnoredWords();
                        }
                    } else if (crawlerConfiguration.getParallelism().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                         //parser.getValueAsInt()=crawlerConfiguration.getParallelism();

                    } else if (crawlerConfiguration.getImplementationOverride().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                       //parser.getValueAsString()= crawlerConfiguration.getImplementationOverride();

                    } else if ( crawlerConfiguration.getMaxDepth().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                        // parser.getValueAsInt()=crawlerConfiguration.getMaxDepth();

                    } else if (crawlerConfiguration.getTimeout().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                        //parser.getValueAsInt()=crawlerConfiguration.getTimeout();

                    } else if (crawlerConfiguration.getPopularWordCount().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                        //parser.getValueAsInt()= crawlerConfiguration.getPopularWordCount();

                    } else if (crawlerConfiguration.getProfileOutputPath().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                        //parser.getValueAsString()=crawlerConfiguration.getProfileOutputPath();
                    } else if (crawlerConfiguration.getResultPath().equals(fieldname)) {
                        jsonToken = parser.nextToken();
                      // parser.getValueAsString()= crawlerConfiguration.getResultPath();
                    }
                }
            }

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            crawlerConfiguration = objectMapper.readValue(parser, CrawlerConfiguration.class);
        }

    }catch (JsonMappingException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }
    return  crawlerConfiguration;
    //return new CrawlerConfiguration.Builder().build();
  }
}
