package jMusic.components;

import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * positive( A * "Source0" + B * "Source1" + C )
 * constructor(A,B,C)
 */
class Linear2 extends Component implements RA
{
	double[] a = new double[2];
	double c;
	Linear2(double A, double B, double C)
	{
		super();
		probe = new Probe[2]; 
		for(int i=0;i<probe.length;i++)
			probe[i] = new Probe("Source"+i);
		//for(int i=0;i<probe.length;i++)
		//	a[i] =
		a[0]=A;
		a[1]=B;
		c=C;
	}
	public Source copy()
	{
		return new Linear2(a[0],a[1],c);
	}
	public double output()
	{
		double value = c;
		for(int i=0;i<probe.length;i++)
			value += probe[i].output() * a[i];
		if(value<0) value=0;
		return value;
	}
}