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
import java.lang.reflect.UndeclaredThrowableException;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

    private final Clock clock;
    private final Object delegate;
    private final ProfilingState state;


    // TODO: You will need to add more instance fields and constructor arguments to this class.

    ProfilingMethodInterceptor(Object delegate, Clock clock, ProfilingState state) {
        this.clock = Objects.requireNonNull(clock);
        this.delegate = Objects.requireNonNull(delegate);
        this.state = state;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        // TODO: This method interceptor should inspect the called method to see if it is a profiled
        //       method. For profiled methods, the interceptor should record the start time, then
        //       invoke the method using the object that is being profiled. Finally, for profiled
        //       methods, the interceptor should record how long the method call took, using the
        //       ProfilingState methods

        Object res = null;
        ZonedDateTime start = null;

        Class declaringclass = method.getDeclaringClass();


        if (declaringclass == Object.class) {
            if (method.getName().equals("equals") && args.length > 0 && args[0] instanceof Proxy) {
                if (Proxy.getInvocationHandler(proxy) instanceof ProfilingMethodInterceptor) {
                    args[0] = ((ProfilingMethodInterceptor) Proxy.getInvocationHandler(proxy)).delegate;
                }
            } else if (method.getName().equals("hashCode")) {
                return new Integer(System.identityHashCode(delegate));
            } else if (method.getName().equals("toString")) {
                return delegate.getClass().getName() + '@' + Integer.toHexString(delegate.hashCode());
            }
        }

        try {
            if (method.isAnnotationPresent(Profiled.class)) {
                start = ZonedDateTime.now(clock);
                res = method.invoke(delegate, args);
                //Thread.sleep(3000);
            }else{
                res = method.invoke(delegate, args);
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
         if (method.isAnnotationPresent(Profiled.class)) {
                ZonedDateTime end = ZonedDateTime.now(clock);
                Duration duration = Duration.between(start, end);
                state.record(delegate.getClass(), method, duration);
            }
        }

       return res;

    }
}
