package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Objects;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final ZonedDateTime startTime;
  private final ZonedDateTime endTime;
//  private final Object delegate;

  //private static Logger logger = LoggerFactory.getLogger(ProfilingMethodInterceptor.class);

  // TODO: You will need to add more instance fields and constructor arguments to this class.

  ProfilingMethodInterceptor(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
    this.endTime = ZonedDateTime.now(clock);
    //this.delegate = delegate;
  }
/*public Profiler getProxy() {
  Profiler proxy = (Profiler) Proxy.newProxyInstance(
          ProfilingMethodInterceptor.class.getClassLoader(),
          new Class[]{Profiler.class},
          new ProfilingMethodInterceptor(clock));
  return proxy;
}*/

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, InvocationTargetException{
    // TODO: This method interceptor should inspect the called method to see if it is a profiled
    //       method. For profiled methods, the interceptor should record the start time, then
    //       invoke the method using the object that is being profiled. Finally, for profiled
    //       methods, the interceptor should record how long the method call took, using the
    //       ProfilingState methods
      ProfilingState profilingState = new ProfilingState();
      Duration duration = Duration.between(startTime,endTime);

    // method = Profiled.class.getDeclaredMethod(method.getName());
      try {
          ProfilingMethodInterceptor interceptor = new ProfilingMethodInterceptor(clock);
          Class c = interceptor.getClass();
          Method method1 = c.getMethod(method.getName());
          Profiled annotation = method1.getAnnotation(Profiled.class);
          if (annotation.equals(method1.isAnnotationPresent(Profiled.class))) {
              profilingState.record(Profiled.class, method, duration);
          }
      }
      catch (IllegalArgumentException e){
          e.getCause();
      }

      return method.invoke(proxy, args);

  }
}
