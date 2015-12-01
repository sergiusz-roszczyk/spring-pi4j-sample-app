package com.roszczyk.pi4jexample.beans;

import com.pi4j.io.gpio.GpioPinDigitalInput;

public interface HardwareController {

    GpioPinDigitalInput getSensorInput();

    void pulseShort();

    void pulseLong();

    void setPwm( int level );

    void shutdown();

}
