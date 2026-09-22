package com.fleetingtrails.fleetingjobsbackend.auth.annotation;

import com.fleetingtrails.fleetingjobsbackend.common.AppModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

    AppModule module();

    String action();

    AppModule.Submodule submodule() default AppModule.Submodule.DEFAULT;
}