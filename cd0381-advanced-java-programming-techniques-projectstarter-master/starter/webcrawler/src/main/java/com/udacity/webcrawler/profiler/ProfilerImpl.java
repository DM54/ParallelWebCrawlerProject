package com.udacity.webcrawler.profiler;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.time.Clock;
import java.lang.annotation.Annotation;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.lang.reflect.Proxy;
import java.util.*;
import java.nio.charset.StandardCharsets;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import java.util.HashMap;

/**
 * Concrete implementation of the {@link Profiler}.
 */
final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state = new ProfilingState();
  private final ZonedDateTime startTime;


  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
  }

  @Override @SuppressWarnings("unchecked")
  public <T> T wrap(Class<T> klass, T delegate) {
      // Objects.requireNonNull(klass);

      // TODO: Use a dynamic proxy (java.lang.reflect.Proxy) to "wrap" the delegate in a
      //       ProfilingMethodInterceptor and return a dynamic proxy from this method.
      //       See https://docs.oracle.com/javase/10/docs/api/java/lang/reflect/Proxy.html.
      Object proxy = null;

      Method[] methods = klass.getDeclaredMethods();
      if(Arrays.stream(methods).noneMatch(m->m.isAnnotationPresent(Profiled.class))){
          throw new IllegalArgumentException();
      }
     else {
          proxy = Proxy.newProxyInstance(
                  delegate.getClass().getClassLoader(),// classloader for whichever interface we want.
                  new Class<?>[]{klass}, // all the interfaces
                  new ProfilingMethodInterceptor(delegate, clock, state)); //the handler
      }
              return (T) proxy;

 }

  @Override
  public void writeData(Path path) {
    // TODO: Write the ProfilingState data to the given file path. If a file already exists at that
    //       path, the new data should be appended to the existing file.
      try(BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)){
      writeData(writer);
    }catch (IOException e){
       e.printStackTrace();
    }
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
    writer.write(System.lineSeparator());
    state.write(writer);
    writer.write(System.lineSeparator());
  }
}
