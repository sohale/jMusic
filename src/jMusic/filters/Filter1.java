package jMusic.filters;

import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;
import jMusic.components.St;




/**
 * This filter is better for noisy inputs and standard for that book.
 * page 212 of the book CM
 */
class Filter1 extends Component implements St //page 211,212 for section 6.3
{
	protected int in=0,f0=1,BW=2;
	Filter1()
	{
		Probe[] p = { new Probe("Input"), new Probe("f0"), new Probe("BW") };
		probe = p;
		reset();
	}
	public Source copy(){return new Filter1();}
	double y_1,y_2;
	public void reset()
	{
		y_1 = 0;
		y_2 = 0;		// ?
	}
	public double output()
	{
		double fs = samplingFrequency();
		double b2 = Math.exp(-2*Math.PI*probe[BW].output()/fs);
		double b1 = (-4*b2)/(1+b2)*Math.cos(2*Math.PI*probe[f0].output()/fs);
		//double a0 = (1.0-b2)*Math.sqrt(1-b1*b1/(4*b2));
		double a0 = Math.sqrt((1-b2)/(1+b2)*((1+b2)*(1+b2)-b1*b1));
		double x = probe[in].output();
		double y = a0*x - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		return y;
	}
	final static Timer timer=new Timer();
	final double samplingFrequency(){
		return timer.getSamplingFrequency();
	}
}
