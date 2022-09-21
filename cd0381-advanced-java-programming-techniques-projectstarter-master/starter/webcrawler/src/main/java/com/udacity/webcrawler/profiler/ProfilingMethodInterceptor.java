package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Objects;
import java.time.Duration;
import java.time.Instant;
import java.lang.annotation.Annotation;
import java.time.ZonedDateTime;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.*;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

    private final Clock clock;
   // private final ZonedDateTime startTime;
    // private final ZonedDateTime endTime;
    private final Object delegate;


    // TODO: You will need to add more instance fields and constructor arguments to this class.
    Map<String, Method> methodMap = new HashMap<>();

    ProfilingMethodInterceptor(Object delegate, Clock clock) {
        this.clock = Objects.requireNonNull(clock);
      //  this.startTime = ZonedDateTime.now(clock);
        //this.endTime = ZonedDateTime.now(clock);
        this.delegate = delegate;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, InvocationTargetException, IllegalAccessException,
            NoSuchMethodException {
        // TODO: This method interceptor should inspect the called method to see if it is a profiled
        //       method. For profiled methods, the interceptor should record the start time, then
        //       invoke the method using the object that is being profiled. Finally, for profiled
        //       methods, the interceptor should record how long the method call took, using the
        //       ProfilingState methods
        ProfilingState profilingState = new ProfilingState();
        ProfilerImpl profiler1 = new ProfilerImpl(clock);
        Object res = null;
        // Instant starttime = clock.instant();
        //Instant endtime = clock.instant();
       if(delegate instanceof Proxy){
           //this is a proxy
           InvocationHandler handler = Proxy.getInvocationHandler(delegate);

           if (delegate.getClass().getMethod(method.getName()).getAnnotation(Profiled.class) != null) {
               ZonedDateTime start = ZonedDateTime.now(clock);
               res = handler.invoke(delegate,method,args);
               Duration duration = Duration.between(start, ZonedDateTime.now(clock));
               profilingState.record(delegate.getClass(), method, duration);
           }
           return res;

       }
       //this is not a proxy
        return method.invoke(delegate, args);

     //  return null;

    }
}
