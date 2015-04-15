package raspberrypi;

import com.pi4j.io.gpio.*;

public class StepperMotor
{

    private final int                  stepCount;
    private int                        currentStep;
    private final int                  pinCount;
    private long                       lastStepTime;
    private long                       stepDelay;
    private boolean                    direction;
    private boolean                    halfstep;
    private final GpioPinDigitalOutput pin1;
    private final GpioPinDigitalOutput pin2;
    private GpioPinDigitalOutput       pin3;
    private GpioPinDigitalOutput       pin4;

    public StepperMotor(final int stepsPerRotation, final GpioPin pin1, final GpioPin pin2, final GpioPin pin3, final GpioPin pin4)
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

    public StepperMotor(final int stepsPerRotation, final GpioPin pin1, final GpioPin pin2, final GpioPin pin3, final GpioPin pin4, final boolean halfstep)
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

    public StepperMotor(final int stepsPerRotation, final Pin pin1, final Pin pin2)
    {

        this.currentStep = 0;
        this.direction = false;
        this.lastStepTime = 0;
        this.stepCount = stepsPerRotation;

        this.pin1 = GpioFactory.getInstance().provisionDigitalOutputPin(pin1);
        this.pin2 = GpioFactory.getInstance().provisionDigitalOutputPin(pin2);

        this.pin1.setMode(PinMode.DIGITAL_OUTPUT);
        this.pin2.setMode(PinMode.DIGITAL_OUTPUT);

        this.pinCount = 2;
    }

    private void moveMotor(final int pos)
    {
        if (this.pinCount == 2)
        {
            switch (pos)
            {
                case 0:
                {
                    this.pin1.low();
                    this.pin2.high();
                    break;
                }
                case 1:
                {
                    this.pin1.high();
                    this.pin2.high();
                    break;
                }
                case 2:
                {
                    this.pin1.high();
                    this.pin2.low();
                    break;
                }
                case 3:
                {
                    this.pin1.low();
                    this.pin2.low();
                    break;
                }
            }
        }
        if ((this.pinCount == 4) && this.halfstep)
        {
            switch (pos)
            {
                case 0:
                {
                    this.pin1.high();
                    this.pin2.low();
                    this.pin3.high();
                    this.pin4.low();
                    break;
                }
                case 1:
                {
                    this.pin1.low();
                    this.pin2.high();
                    this.pin3.high();
                    this.pin4.low();
                    break;
                }
                case 2:
                {
                    this.pin1.low();
                    this.pin2.high();
                    this.pin3.low();
                    this.pin4.high();
                    break;
                }
                case 3:
                {
                    this.pin1.high();
                    this.pin2.low();
                    this.pin3.low();
                    this.pin4.high();
                    break;
                }
            }
        }
        if ((this.pinCount == 4) && this.halfstep)
        {
            switch (pos)
            {
                case 0:
                {
                    this.pin1.high();
                    this.pin2.low();
                    this.pin3.high();
                    this.pin4.low();
                    break;
                }
                case 1:
                {
                    this.pin1.low();
                    this.pin2.low();
                    this.pin3.high();
                    this.pin4.low();
                    break;
                }
                case 2:
                {
                    this.pin1.low();
                    this.pin2.high();
                    this.pin3.low();
                    this.pin4.high();
                    break;
                }
                case 3:
                {
                    this.pin1.low();
                    this.pin2.high();
                    this.pin3.low();
                    this.pin4.low();
                    break;
                }
                case 4:
                {
                    this.pin1.low();
                    this.pin2.high();
                    this.pin3.low();
                    this.pin4.high();
                    break;
                }
                case 5:
                {
                    this.pin1.low();
                    this.pin2.low();
                    this.pin3.low();
                    this.pin4.high();
                    break;
                }
                case 6:
                {
                    this.pin1.high();
                    this.pin2.low();
                    this.pin3.low();
                    this.pin4.high();
                    break;
                }
                case 7:
                {
                    this.pin1.high();
                    this.pin2.low();
                    this.pin3.low();
                    this.pin4.low();
                    break;
                }
            }
        }
    }

    /**
     * Sets the speed of the motor (100 steps on a 100 step motor will take 1 minute)
     *
     * @param rpm
     *            the amount of rotations per minute
     */
    public void setRPM(final long speed)
    {
        this.stepDelay = (60L * 1000L) / this.stepCount / speed;
    }

    /**
     * Moves the motor
     *
     * @param steps
     *            the amount steps to move
     */
    public void step(final int steps)
    {
        int stepsLeft = Math.abs(steps);
        if (steps < 0)
        {
            this.direction = false;
        }
        if (steps > 0)
        {
            this.direction = true;
        }
        while (stepsLeft > 0)
        {
            if ((System.currentTimeMillis() - this.lastStepTime) >= this.stepDelay)
            {
                this.lastStepTime = System.currentTimeMillis();
                if (this.direction)
                {
                    this.currentStep++;
                    if (this.currentStep == this.stepCount)
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
                    this.moveMotor(this.currentStep % 8);
                } else
                {
                    this.moveMotor(this.currentStep % 4);
                }
            }
        }
    }
}
