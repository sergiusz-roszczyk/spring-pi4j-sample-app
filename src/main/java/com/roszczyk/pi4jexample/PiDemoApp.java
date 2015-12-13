package com.roszczyk.pi4jexample;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.roszczyk.pi4jexample.beans.HardwareController;
import com.roszczyk.pi4jexample.beans.SystemLeds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PiDemoApp {

    private static final Logger LOGGER = LoggerFactory.getLogger( PiDemoApp.class );
    private static final int PWM_MAX = 1023;
    private static final int PWM_LOW = 0;
    private static final int PWM_STEP = 100;
    private static final int FADE_OUT_DELAY_SECONDS = 10;
    volatile private boolean isMotion;
    volatile private int pwmLevel;
    volatile private int targetPwmLevel;
    volatile private int secondsSinceLastMotion;

    @Autowired
    private HardwareController hardwareController;

    public void run() throws InterruptedException {
        System.out.println( "Naciśnij CTRL+C, aby przerwać działanie aplikacji." );

        pwmLevel = 0;
        setupMotionDetector( hardwareController.getSensorInput() );

        while ( true ) {
            if ( secondsSinceLastMotion > FADE_OUT_DELAY_SECONDS && targetPwmLevel != PWM_LOW ) {
                setTargetPwmLevel( PWM_LOW );
            }
            Thread.sleep( 500 );
        }
    }

    @Scheduled(fixedDelay = 80)
    private void updatePwmLevel() {
        if ( Math.abs( targetPwmLevel - pwmLevel ) < PWM_STEP ) {
            pwmLevel = targetPwmLevel;
        } else if ( pwmLevel > targetPwmLevel ) {
            pwmLevel -= PWM_STEP;
        } else if ( pwmLevel < targetPwmLevel ) {
            pwmLevel += PWM_STEP;
        }
        if ( pwmLevel > PWM_MAX ) {
            pwmLevel = PWM_MAX;
        }
        if ( pwmLevel < PWM_LOW ) {
            pwmLevel = PWM_LOW;
        }
        hardwareController.setPwm( pwmLevel );
    }

    @Scheduled(fixedDelay = 2000)
    private void heartbeatAction() {
        hardwareController.pulseShort( SystemLeds.STATUS_LED);
        printStatus();
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    private void secondsTicks() {
        if ( isMotion ) {
            clearLastMotionCounter();
        } else {
            secondsSinceLastMotion++;
        }
    }

    private void setupMotionDetector( GpioPinDigitalInput sensorInput ) {
        setMotion( false );
        clearLastMotionCounter();
        sensorInput.addListener( new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent( GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent ) {
                if ( gpioPinDigitalStateChangeEvent.getState() == PinState.HIGH ) {
                    motionDetectedAction();
                } else {
                    motionOutAction();
                }
            }
        } );
    }

    private void motionDetectedAction() {
        LOGGER.info( String.format( "%s - wykryto ruch", Instant.now() ) );
        clearLastMotionCounter();
        setMotion( true );
        hardwareController.on(SystemLeds.MOTION_LED);
        setTargetPwmLevel( PWM_MAX );
    }

    private void motionOutAction() {
        setMotion( false );
        hardwareController.off( SystemLeds.MOTION_LED );
    }

    private void setTargetPwmLevel( int pwmLevel ) {
        this.targetPwmLevel = pwmLevel;
    }

    private void setMotion( boolean motion ) {
        isMotion = motion;
    }

    private void clearLastMotionCounter() {
        secondsSinceLastMotion = 0;
    }

    private void printStatus() {
        LOGGER.info(
                String.format( "%s - pwmLevel: %d, targetPwmLevel: %d, secondsSinceLastMotion: %d, isMotion: %s",
                        Instant.now(),
                        pwmLevel,
                        targetPwmLevel,
                        secondsSinceLastMotion,
                        isMotion)
        );
    }
}
