package jMusic.filters;

import jMusic.Source;

//do not inherit from ButterworthTypeFilter
class ButterworthHighpass 
//extends ButterworthTypeFilter
extends ButterworthLowpass
{
	public double output()
	{
		double fs = samplingFrequency();
		double C = Math.tan(Math.PI*probe[fc].output()/fs);
		double a0 = 1/(1+Math.sqrt(2)*C+C*C);
		double a1 = -2*a0;
		double a2 = a0;
		double b1 = 2*a0*(C*C-1);
		double b2 = a0*(1-Math.sqrt(2)*C+C*C);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
	
	public Source copy(){return new ButterworthHighpass();}
}



