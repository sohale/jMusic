package jMusic.filters;

import jMusic.Source;
import jMusic.Timer;

/**
 * This filter outputs 1.0 at f0.
 * page 211 of CM book
 */
class Filter2 extends Filter1
{
	public double output()
	{
		double fs = samplingFrequency();
		double b2 = Math.exp(-2*Math.PI*probe[BW].output()/fs);
		double b1 = (-4*b2)/(1+b2)*Math.cos(2*Math.PI*probe[f0].output()/fs);
		double a0 = (1.0-b2)*Math.sqrt(1-b1*b1/(4*b2));
		double x = probe[in].output();
		double y = a0*x - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		return y;
	}
	public Source copy(){return new Filter2();}
	
	
}
