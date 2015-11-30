package com.roszczyk.pi4jexample.main;

import com.roszczyk.pi4jexample.SampleApp;
import com.roszczyk.pi4jexample.config.AppConfig;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SampleAppMain {

    public static void main( String[] args ) throws InterruptedException{
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext( AppConfig.class );
        SampleApp sampleApp = BeanFactoryUtils.beanOfType( ctx, SampleApp.class );
        sampleApp.run();
        ctx.close();
    }
}
