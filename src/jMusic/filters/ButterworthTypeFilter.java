package jMusic.filters;

import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;
import jMusic.components.St;

abstract class ButterworthTypeFilterDontUse extends Component implements St
{
	protected int in=0,fc=1;
	ButterworthTypeFilterDontUse()
	{
		Probe[] p = { new Probe("Input"), new Probe("fc") };
		probe = p;
		reset();
	}
	/*
	public Source copy(){
		//return new ButterworthLowpass();
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Design Error: not a ButterworthType Filter");
		}
		}
		*/
	public abstract Source copy();
	
	double y_1,y_2,x_1,x_2;
	public void reset()
	{
		y_1 = 0;
		y_2 = 0;		// ?
		x_1 = 0;
		x_2 = 0;
	}

	final static Timer timer=new Timer();
	final double samplingFrequency(){
		//Timer.sampleFrequency();
		return timer.getSamplingFrequency();
	}

	/*
	protected abstract double output();
	*/
	/*
	public double output1()
	{
		double fs = samplingFrequency();
		double C = 1/Math.tan(Math.PI*probe[fc].output()/fs);
		double a0 = 1/(1+Math.sqrt(2)*C+C*C);
		double a1 = 2*a0;
		double a2 = a0;
		double b1 = 2*a0*(1-C*C);
		double b2 = a0*(1-Math.sqrt(2)*C+C*C);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
	*/
}


