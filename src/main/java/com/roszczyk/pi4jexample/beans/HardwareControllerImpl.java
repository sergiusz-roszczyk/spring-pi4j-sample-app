package com.roszczyk.pi4jexample.beans;

import com.pi4j.io.gpio.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HardwareControllerImpl implements HardwareController {

    private final GpioController gpioController;
    private final GpioPinPwmOutput pwmLedPin;
    private final GpioPinDigitalOutput motionLedPin;
    private final GpioPinDigitalOutput statusLedPin;
    private final GpioPinDigitalInput sensorInput;
    private final Map<SystemLeds, GpioPinDigitalOutput> ledsMap;

    public HardwareControllerImpl() {
        ledsMap = new HashMap<>();
        gpioController = GpioFactory.getInstance();
        motionLedPin = gpioController.provisionDigitalOutputPin( RaspiPin.GPIO_00, PinState.LOW );
        motionLedPin.setShutdownOptions( true, PinState.LOW, PinPullResistance.OFF );
        pwmLedPin = gpioController.provisionPwmOutputPin( RaspiPin.GPIO_01 );
        sensorInput = gpioController.provisionDigitalInputPin( RaspiPin.GPIO_02 );
        statusLedPin = gpioController.provisionDigitalOutputPin( RaspiPin.GPIO_03, PinState.LOW );
        statusLedPin.setShutdownOptions( true, PinState.LOW, PinPullResistance.OFF );

        ledsMap.put( SystemLeds.MOTION_LED, motionLedPin );
        ledsMap.put( SystemLeds.STATUS_LED, statusLedPin );

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        } );

    }

    public GpioPinDigitalInput getSensorInput() {
        return sensorInput;
    }

    public void shutdown() {
        gpioController.shutdown();
    }

    public void pulseShort( SystemLeds led ) {
        pulse( led, 150 );
    }

    public void pulseLong( SystemLeds led ) {
        pulse( led, 1000 );
    }

    public void on( SystemLeds led ) {
        ledsMap.get( led ).high();
    }

    public void off( SystemLeds led ) {
        ledsMap.get( led ).low();
    }

    public void setPwm( int level ) {
        pwmLedPin.setPwm( level );
    }

    private void pulse( SystemLeds led, long time ) {
        ledsMap.get( led ).pulse( time );
    }

}
