package com.roszczyk.pi4jexample;

import com.roszczyk.pi4jexample.beans.SampleHelloBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleApp {

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
        System.out.println( System.getProperty( "line.separator" ) + "Odebrano sygnał zamknięcia aplikacji." );
        // tu można zwolnić zasoby
        System.out.println( "Aplikacja zakończona." );
    }
}
