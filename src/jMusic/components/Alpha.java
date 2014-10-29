package jMusic.components;

import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;

/**
 * Exponential Approch
 * y' = (1-a)*y + a*x
 * one sample per output. time in not considered.
 * (has simple feedback)
 * it' beh. is depended to sample rate
 */
class Alpha extends Component implements St
{
	Timer timer;
	Alpha()
	{
		super();
		probe = new Probe[2]; 
		probe[0] = new Probe("Input");
		probe[1]	= new Probe("Alpha");
		timer=new Timer();
		reset();
	}
	public Source copy()
	{
		return new Alpha();
	}
	public void reset()
	{
		y=0;
	}
	double y;
	public double output()
	{
		double fs=timer.getSamplingFrequency();
		double x = probe[0].output();
		double a0 = probe[1].output();
		double a = 1/(1*0+a0)/fs;
		y = (1-a)*y + (0+a)*x;
		return y;
	}
}
