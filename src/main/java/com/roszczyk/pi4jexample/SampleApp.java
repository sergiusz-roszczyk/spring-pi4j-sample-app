package com.roszczyk.pi4jexample;

import com.roszczyk.pi4jexample.beans.SampleHelloBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger( SampleApp.class );
    @Autowired
    private SampleHelloBean sampleHelloBean;
    @Autowired
    private String welcomeMessage;

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    private void scheduledAction() {
        System.out.println( sampleHelloBean.getMessage() );
    }

    public void run() throws InterruptedException {
        System.out.println( welcomeMessage );
        System.out.println( "Naciśnij CTRL+C, aby przerwać działanie aplikacji." );

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        } );

        while ( true ) {
            Thread.sleep( 500 );
        }
    }

    private void shutdown() {
        LOGGER.info( "Odebrano sygnał zamknięcia aplikacji." );
        // tu można zwolnić zasoby
        System.out.println( "Aplikacja zakończona." );
    }
}
