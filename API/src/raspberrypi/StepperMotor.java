package raspberrypi;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;

public class StepperMotor
{

    private int     stepCount;
    private int     currentStep;
    private int     pinCount;
    private long    lastStepTime;
    private long    stepDelay;
    private boolean direction;
    private boolean halfstep;
    private GpioPinDigitalOutput pin1, pin2, pin3, pin4;

    public StepperMotor(int stepsPerRotation, GpioPin pin1, GpioPin pin2)
    {

        this.currentStep = 0;
        this.direction = false;
        this.lastStepTime = 0;
        this.stepCount = stepsPerRotation;

        this.pin1 = GpioFactory.getInstance().provisionDigitalOutputPin(pin1.getPin());
        this.pin2 = GpioFactory.getInstance().provisionDigitalOutputPin(pin2.getPin());

        this.pin1.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin2.setMode(PinMode.DIGITAL_OUTPUT);

        this.pinCount = 2;
    }

    public StepperMotor(int stepsPerRotation, GpioPin pin1, GpioPin pin2, GpioPin pin3, GpioPin pin4)
    {
        this.currentStep = 0;
        this.direction = false;
        this.lastStepTime = 0;
        this.stepCount = stepsPerRotation;

        this.pin1 = GpioFactory.getInstance().provisionDigitalOutputPin(pin1.getPin());
        this.pin2 = GpioFactory.getInstance().provisionDigitalOutputPin(pin2.getPin());
        this.pin3 = GpioFactory.getInstance().provisionDigitalOutputPin(pin3.getPin());
        this.pin4 = GpioFactory.getInstance().provisionDigitalOutputPin(pin4.getPin());

        this.pin1.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin2.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin3.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin4.setMode(PinMode.DIGITAL_OUTPUT);

        this.pinCount = 4;
    }

    public StepperMotor(int stepsPerRotation, GpioPin pin1, GpioPin pin2, GpioPin pin3, GpioPin pin4, boolean halfstep)
    {
        this.halfstep = halfstep;

        this.currentStep = 0;
        this.direction = false;
        this.lastStepTime = 0;
        this.stepCount = stepsPerRotation;

        this.pin1 = GpioFactory.getInstance().provisionDigitalOutputPin(pin1.getPin());
        this.pin2 = GpioFactory.getInstance().provisionDigitalOutputPin(pin2.getPin());
        this.pin3 = GpioFactory.getInstance().provisionDigitalOutputPin(pin3.getPin());
        this.pin4 = GpioFactory.getInstance().provisionDigitalOutputPin(pin4.getPin());

        this.pin1.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin2.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin3.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin4.setMode(PinMode.DIGITAL_OUTPUT);

        this.pinCount = 4;
    }

    /**
     * Sets the speed of the motor (100 steps on a 100 step motor will take 1 minute)
     * 
     * @param rpm
     *            the amount of rotations per minute
     */
    public void setRPM(long speed)
    {
        this.stepDelay = 60L * 1000L / this.stepCount / speed;
    }

    /**
     * Moves the motor
     * 
     * @param steps
     *            the amount steps to move
     */
    public void step(int steps)
    {
        int stepsLeft = Math.abs(steps);
        if (steps < 0) direction = false;
        if (steps > 0) direction = true;
        while (stepsLeft > 0)
        {
            if (System.currentTimeMillis() - this.lastStepTime >= this.stepDelay)
            {
                this.lastStepTime = System.currentTimeMillis();
                if (direction)
                {
                    currentStep++;
                    if (currentStep == this.stepCount)
                    {
                        this.currentStep = 0;
                    }
                } else
                {
                    if (this.currentStep == 0)
                    {
                        this.currentStep = this.stepCount;
                    }
                    this.currentStep--;
                }
                stepsLeft--;
                if (this.halfstep)
                {
                    moveMotor(this.currentStep % 8);
                } else
                {
                    moveMotor(this.currentStep % 4);
                }
            }
        }
    }

    private void moveMotor(int pos)
    {
        if (this.pinCount == 2)
        {
            switch (pos)
            {
                case 0:
                {
                    pin1.low();
                    pin2.high();
                    break;
                }
                case 1:
                {
                    pin1.high();
                    pin2.high();
                    break;
                }
                case 2:
                {
                    pin1.high();
                    pin2.low();
                    break;
                }
                case 3:
                {
                    pin1.low();
                    pin2.low();
                    break;
                }
            }
        }
        if (this.pinCount == 4 && this.halfstep)
        {
            switch (pos)
            {
                case 0:
                {
                    pin1.high();
                    pin2.low();
                    pin3.high();
                    pin4.low();
                    break;
                }
                case 1:
                {
                    pin1.low();
                    pin2.high();
                    pin3.high();
                    pin4.low();
                    break;
                }
                case 2:
                {
                    pin1.low();
                    pin2.high();
                    pin3.low();
                    pin4.high();
                    break;
                }
                case 3:
                {
                    pin1.high();
                    pin2.low();
                    pin3.low();
                    pin4.high();
                    break;
                }
            }
        }
        if (this.pinCount == 4 && this.halfstep)
        {
            switch (pos)
            {
                case 0:
                {
                    pin1.high();
                    pin2.low();
                    pin3.high();
                    pin4.low();
                    break;
                }
                case 1:
                {
                    pin1.low();
                    pin2.low();
                    pin3.high();
                    pin4.low();
                    break;
                }
                case 2:
                {
                    pin1.low();
                    pin2.high();
                    pin3.low();
                    pin4.high();
                    break;
                }
                case 3:
                {
                    pin1.low();
                    pin2.high();
                    pin3.low();
                    pin4.low();
                    break;
                }
                case 4:
                {
                    pin1.low();
                    pin2.high();
                    pin3.low();
                    pin4.high();
                    break;
                }
                case 5:
                {
                    pin1.low();
                    pin2.low();
                    pin3.low();
                    pin4.high();
                    break;
                }
                case 6:
                {
                    pin1.high();
                    pin2.low();
                    pin3.low();
                    pin4.high();
                    break;
                }
                case 7:
                {
                    pin1.high();
                    pin2.low();
                    pin3.low();
                    pin4.low();
                    break;
                }
            }
        }
    }
}
