package com.roszczyk.pi4jexample.beans;

import com.pi4j.io.gpio.GpioPinDigitalInput;

public interface HardwareController {

    GpioPinDigitalInput getSensorInput();

    void pulseShort( SystemLeds led );

    void pulseLong( SystemLeds led );

    void on(SystemLeds led);

    void off(SystemLeds led);

    void setPwm( int level );

    void shutdown();

}
