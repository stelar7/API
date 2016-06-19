package raspberrypi;

import com.pi4j.io.gpio.*;

public class ShiftRegister
{

    enum BitOrder
    {
        MSB,
        LSB;
    }

    private final GpioPinDigitalOutput data;
    private final GpioPinDigitalOutput clock;
    private final int                  bits;
    private final boolean[]            lastOut;

    public ShiftRegister(final Pin data, final Pin clock, final int bits)
    {
        this.data = GpioFactory.getInstance().provisionDigitalOutputPin(data);
        this.clock = GpioFactory.getInstance().provisionDigitalOutputPin(clock);
        this.bits = bits;
        this.lastOut = new boolean[bits];
    }

    public void shiftOut(final long value, final BitOrder order)
    {
        for (int i = 0; i < this.bits; i++)
        {
            if (order == BitOrder.LSB)
            {
                this.data.setState((value & (1 << i)) != 0);
            } else
            {
                this.data.setState((value & (1 << ((this.bits - 1) - i))) != 0);
            }
            this.clock.high();
            this.clock.low();
            this.updateLast();
        }
    }

    private void updateLast()
    {
        for (int ii = this.lastOut.length - 1; ii >= 0; ii--)
        {
            this.lastOut[ii + 1] = this.lastOut[ii];
        }
        this.lastOut[0] = this.data.getState().isHigh();
    }

}
