package jMusic.filters;

import jMusic.Component;
import jMusic.components.St;

class ButterworthBandreject //extends ButterworthTypeFilter
extends ButterworthBandpass
{
	public double output()
	{
		double fs = samplingFrequency();
		double C = Math.tan(Math.PI*probe[BW].output()/fs);
		double D = 2*Math.cos(2*Math.PI*probe[f0].output()/fs);
		double a0 = 1/(1+C);
		double a1 = -a0*D;
		double a2 = a0;
		double b1 = -a0*D;
		double b2 = a0*(1-C);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
}
