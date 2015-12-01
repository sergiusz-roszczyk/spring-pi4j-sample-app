package com.roszczyk.pi4jexample.beans;

import com.pi4j.io.gpio.*;
import org.springframework.stereotype.Component;

@Component
public class HardwareControllerImpl implements HardwareController {

    private final GpioController gpioController;
    private final GpioPinPwmOutput pwmLedPin;
    private final GpioPinDigitalOutput statusLedPin;
    private final GpioPinDigitalInput sensorInput;

    public HardwareControllerImpl() {
        gpioController = GpioFactory.getInstance();
        statusLedPin = gpioController.provisionDigitalOutputPin( RaspiPin.GPIO_00, PinState.LOW );
        statusLedPin.setShutdownOptions( true, PinState.LOW, PinPullResistance.OFF );
        pwmLedPin = gpioController.provisionPwmOutputPin( RaspiPin.GPIO_01 );
        sensorInput = gpioController.provisionDigitalInputPin( RaspiPin.GPIO_02 );
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

    public void pulseShort() {
        pulse( 150 );
    }

    public void pulseLong() {
        pulse( 1000 );
    }

    public void setPwm( int level ) {
        pwmLedPin.setPwm( level );
    }

    private void pulse( long time ) {
        statusLedPin.pulse( time );
    }

}
