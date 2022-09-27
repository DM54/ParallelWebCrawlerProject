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
    private final ZonedDateTime startTime;
    private final Object delegate;
    private final ProfilingState state;
   // private final Class<?> klass;



    // TODO: You will need to add more instance fields and constructor arguments to this class.

    ProfilingMethodInterceptor(Object delegate, Clock clock, ProfilingState state) {
        this.clock = Objects.requireNonNull(clock);
        this.startTime = ZonedDateTime.now(clock);
        //this.endTime = ZonedDateTime.now(clock);
        this.delegate = Objects.requireNonNull(delegate);
        this.state = state;
       // this.klass = klass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        // TODO: This method interceptor should inspect the called method to see if it is a profiled
        //       method. For profiled methods, the interceptor should record the start time, then
        //       invoke the method using the object that is being profiled. Finally, for profiled
        //       methods, the interceptor should record how long the method call took, using the
        //       ProfilingState methods
        ProfilingState profilingState = new ProfilingState();
        ProfilerImpl profiler1 = new ProfilerImpl(clock);
        Object res = null;
        ZonedDateTime start = null;
       // Method m = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());

        Class declaringclass = method.getDeclaringClass();


       if(declaringclass==Object.class){
            if(method.getName().equals("equals") && args.length>0 && args[0] instanceof Proxy){
               if(Proxy.getInvocationHandler(proxy) instanceof ProfilingMethodInterceptor){
                   args[0] = ((ProfilingMethodInterceptor) Proxy.getInvocationHandler(proxy)).delegate;
               }
            } else if (method.getName().equals("hashCode")) {
                return new Integer(System.identityHashCode(delegate));
            } else if (method.getName().equals("toString")) {
                return delegate.getClass().getName() + '@' + Integer.toHexString(delegate.hashCode());
            }
        }


        Method[] methods = delegate.getClass().getDeclaredMethods();
           try {
               if (Arrays.stream(methods).anyMatch(m->m.isAnnotationPresent(Profiled.class))) {
                   start = startTime;
                   res= method.invoke(delegate, args);
                   //Thread.sleep(1000);
                    ZonedDateTime end = ZonedDateTime.now(clock);
                    Duration duration = Duration.between(start, end);
                    profilingState.record(delegate.getClass(), method, duration);
               }else{
                   res = method.invoke(delegate,args);
               }

           }catch (InvocationTargetException |
                   IllegalAccessException exception) {
               // Find underlying causal Exception.
               if (exception.getCause() != null) {
                 throw  exception.getCause();
               }else {
                   // Catch expected Exceptions.
                   throw exception;
               }
           } catch (Exception exception) {
               // Catch unexpected Exceptions.
              throw exception;
           }finally {
               if (Arrays.stream(methods).anyMatch(m->m.isAnnotationPresent(Profiled.class))) {
                   start = startTime;
                   res= method.invoke(delegate, args);
                   //Thread.sleep(1000);
                   ZonedDateTime end = ZonedDateTime.now(clock);
                   Duration duration = Duration.between(start, end);
                   profilingState.record(delegate.getClass(), method, duration);
               }
           }
    return res;

    }
}
