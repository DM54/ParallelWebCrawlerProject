package com.udacity.webcrawler.main;
import com.google.inject.Guice;
import com.udacity.webcrawler.WebCrawler;
import com.udacity.webcrawler.WebCrawlerModule;
import com.udacity.webcrawler.json.ConfigurationLoader;
import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.json.CrawlResultWriter;
import com.udacity.webcrawler.json.CrawlerConfiguration;
import com.udacity.webcrawler.profiler.Profiler;
import com.udacity.webcrawler.profiler.ProfilerModule;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.io.*;
import java.io.File;
import java.io.IOException;

public final class WebCrawlerMain {

  private final CrawlerConfiguration config;

  private WebCrawlerMain(CrawlerConfiguration config) {
    this.config = Objects.requireNonNull(config);
  }

  @Inject
  private WebCrawler crawler;

  @Inject
  private Profiler profiler;

  private void run() throws Exception {
    Guice.createInjector(new WebCrawlerModule(config), new ProfilerModule()).injectMembers(this);

    CrawlResult result = crawler.crawl(config.getStartPages());
    CrawlResultWriter resultWriter = new CrawlResultWriter(result);
    // TODO: Write the crawl results to a JSON file (or System.out if the file name is empty)
    Path path = Paths.get(config.getResultPath());
    File file = new File(path.toString());
    if(file.exists()){
      resultWriter.write(path);
    }else{
      try(Writer outputStream = new BufferedWriter(new OutputStreamWriter(System.out))){
        resultWriter.write(outputStream);
      }catch (IOException e){
        e.printStackTrace();
      }
    }

    // TODO: Write the profile data to a text file (or System.out if the file name is empty)
    Path path1 = Paths.get(config.getProfileOutputPath());
    File file1 = new File(path1.toString());
    if(file1.exists()){
      resultWriter.write(path1);
    }else{
      try(Writer outputStream1 = new BufferedWriter(new OutputStreamWriter(System.out))){
        resultWriter.write(outputStream1);
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage: WebCrawlerMain [starting-url]");
      return;
    }

    CrawlerConfiguration config = new ConfigurationLoader(Path.of(args[0])).load();
    new WebCrawlerMain(config).run();
  }
}
