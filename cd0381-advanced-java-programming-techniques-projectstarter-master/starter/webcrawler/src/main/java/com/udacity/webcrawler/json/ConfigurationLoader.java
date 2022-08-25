package com.udacity.webcrawler.json;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Objects;
import java.nio.file.Files;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
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
    try(Reader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)){
        while((char)reader.read()!=-1){
          reader.read();
        }

    }
    //reader.close();
    catch(IOException e){
      e.printStackTrace();
    }

    return new CrawlerConfiguration.Builder().build();
  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */

  public static CrawlerConfiguration read(Reader reader) {
    // This is here to get rid of the unused variable warning.
    Objects.requireNonNull(reader);
    ObjectMapper objectMapper = new ObjectMapper();
    // TODO: Fill in this method
    reader = load();
   CrawlerConfiguration = objectMapper.readValue(reader, CrawlerConfiguration.class);

    return new CrawlerConfiguration.Builder().build();
  }
}
