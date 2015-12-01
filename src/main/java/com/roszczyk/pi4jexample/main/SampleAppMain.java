package com.roszczyk.pi4jexample.main;

import com.roszczyk.pi4jexample.PiDemoApp;
import com.roszczyk.pi4jexample.config.AppConfig;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SampleAppMain {

    public static void main( String[] args ) throws InterruptedException{
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext( AppConfig.class );
        PiDemoApp sampleApp = BeanFactoryUtils.beanOfType( ctx, PiDemoApp.class );
        sampleApp.run();
        ctx.close();
    }
}
