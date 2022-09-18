package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Objects;
import java.time.Duration;
import java.lang.annotation.Annotation;
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
    //private final ZonedDateTime startTime;
    //private final ZonedDateTime endTime;
    private final Object delegate;


    // TODO: You will need to add more instance fields and constructor arguments to this class.

    ProfilingMethodInterceptor(Object delegate, Clock clock) {
        this.clock = Objects.requireNonNull(clock);
       // this.startTime = ZonedDateTime.now(clock);
       // this.endTime = ZonedDateTime.now(clock);
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        // TODO: This method interceptor should inspect the called method to see if it is a profiled
        //       method. For profiled methods, the interceptor should record the start time, then
        //       invoke the method using the object that is being profiled. Finally, for profiled
        //       methods, the interceptor should record how long the method call took, using the
        //       ProfilingState methods
        ProfilingState profilingState = new ProfilingState();
        //ProfilerImpl profiler1 = new ProfilerImpl(clock);
        Object res = null;
        long starttime = System.currentTimeMillis();

       Class<?> classess = method.getClass();
       Class<?>[] inter = classess.getInterfaces();
        for (Class<?> c: inter
             ) {
            for (Method m: c.getMethods()
                 ) {
                if(m.isAnnotationPresent(Profiled.class)){

                    method.invoke(delegate, args);
                    long endtime = System.currentTimeMillis();
                    Duration duration = Duration.ofMillis(endtime - starttime);
                    profilingState.record(ProfilingMethodInterceptor.class, method, duration);

                }else {
                    throw new IllegalArgumentException("no");
                }
            }
        }

        return method.invoke(delegate,args);
    }
}
