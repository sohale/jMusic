package jMusic.oscillators;

import jMusic.Probe;
import jMusic.Source;
import jMusic.components.Constant;

/**
 * a super class for easy oscillator/generator building.
 * ? is there a problem on probe count?
 * (([Time=],Frequency,[Phase=0]),[Amplitude=1])
 */
//NOTE: RE-ABSTRACTing  ( class Generator extends Phase ? )
public abstract class Generator extends Phase	//Thursday,6esfand77 7:19pm
{
	protected int Amp=3;	//see Phase:: Time=0,Freq=1,Phase=2;
	//Problem solved. because all classes that use Amp are childs of this class

	public Generator()
	{


		Probe newprobes[] = new Probe[probe.length+1];
		System.arraycopy(probe,0, newprobes,0, probe.length);
		probe = newprobes;
		newprobes = null;

		//probe[probe.length-1] = new Constant(1.0);
		probe[Amp] = new Probe("Amplitude");
		probe("Amplitude").link( new Constant(1.0) );
		probe("Amplitude").isDefault(); //only allowed within a constructor??


		probes = probes+1;	//4
		if(probes!=4 || probes!=probe.length-1 || Amp!=probes-1)
			;//

		//Amp = 3;
	}

	private double frac(double t)
	{
		double x;
		x = t - Math.floor(t);
		//if(x<0.0) x = x+1.0;

		//return f(x);

		return x;
	}
	public double phase()
	{
		return frac(super.output());
	}
	abstract public double output();
	abstract public Source copy();
}
