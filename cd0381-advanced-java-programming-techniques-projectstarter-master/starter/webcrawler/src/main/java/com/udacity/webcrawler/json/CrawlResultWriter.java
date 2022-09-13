package com.udacity.webcrawler.json;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Objects;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
/**
 * Utility class to write a {@link CrawlResult} to file.
 */
public final class CrawlResultWriter {
  private final CrawlResult result;

  /**
   * Creates a new {@link CrawlResultWriter} that will write the given {@link CrawlResult}.
   */
  public CrawlResultWriter(CrawlResult result) {
    this.result = Objects.requireNonNull(result);
  }

  /**
   * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Path}.
   *
   * <p>If a file already exists at the path, the existing file should not be deleted; new data
   * should be appended to it.
   *
   * @param path the file path where the crawl result data should be written.
   */
  public void write(Path path) {

    //append to a existing file it should have second parameter as true in FileWriter.
     try(BufferedWriter writertofile = Files.newBufferedWriter(path, StandardOpenOption.CREATE)){
       write(writertofile);
     }catch (IOException e){
       e.printStackTrace();
     }
  }

  /**
   * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Writer}.
   *
   * @param writer the destination where the crawl result data should be written.
   */
  public void write(Writer writer) {
    JsonFactory factory = new JsonFactory();
    factory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,false);
    ObjectMapper objectMapper = new ObjectMapper(factory);
    try(JsonGenerator jsonGenerator = factory.createGenerator(writer)){
       objectMapper.writeValue(jsonGenerator, result);
    }catch (JsonMappingException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }

  }
}
