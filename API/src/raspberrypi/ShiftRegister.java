package raspberrypi;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

public class ShiftRegister
{

    enum BitOrder
    {
        MSB,
        LSB;
    }

    private GpioPinDigitalOutput data;
    private GpioPinDigitalOutput clock;
    private int                  bits;
    private boolean[]            lastOut;

    public ShiftRegister(Pin data, Pin clock, int bits)
    {
        this.data = GpioFactory.getInstance().provisionDigitalOutputPin(data);
        this.clock = GpioFactory.getInstance().provisionDigitalOutputPin(clock);
        this.bits = bits;
        lastOut = new boolean[bits];
    }

    public void shiftOut(long value, BitOrder order)
    {
        for (int i = 0; i < bits; i++)
        {
            if (order == BitOrder.LSB)
            {
                data.setState((value & (1 << i)) != 0);
            } else
            {
                data.setState((value & (1 << ((bits - 1) - i))) != 0);
            }
            clock.high();
            clock.low();
            updateLast();
        }
    }

    public boolean[] getLastShitOut()
    {
        return lastOut;
    }

    private void updateLast()
    {
        for (int ii = this.lastOut.length - 1; ii >= 0; ii--)
        {
            lastOut[ii + 1] = lastOut[ii];
        }
        lastOut[0] = data.getState().isHigh();
    }

}
