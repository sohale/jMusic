package jMusic.filters;

import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;
import jMusic.components.St;

/**
 * Butterworth band-pass filter
 * page 216 of the book CM
 */
class ButterworthBandpass extends 
//ButterworthTypeFilter 
Component implements St
{
	protected int in=0,f0=1,BW=2;
	//protected int BW=2;
	
	ButterworthBandpass()
	{
		Probe[] p = { new Probe("Input"), new Probe("f0"), new Probe("BW") };
		probe = p;
		reset();
	}
	public Source copy(){return new ButterworthBandpass();}
	double y_1,y_2,x_1,x_2;
	public void reset()
	{
		y_1 = 0;
		y_2 = 0;		// ?
		x_1 = 0;
		x_2 = 0;
	}
	public double output()
	{
		double fs = samplingFrequency(); //runtime
		double C = 1/Math.tan(Math.PI*probe[BW].output()/fs);
		double D = 2*Math.cos(2*Math.PI*probe[f0].output()/fs);
		double a0 = 1/(1+C);
		double a1 = 0;
		double a2 = -a0;
		double b1 = -a0*C*D;
		double b2 = a0*(C-1);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
	
	final static Timer timer=new Timer();
	final double samplingFrequency(){
		//Timer.sampleFrequency();
		return timer.getSamplingFrequency();
	}
}

