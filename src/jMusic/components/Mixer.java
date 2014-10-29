package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * The Mixer=Adder (PLUS +)
 * (Source0,Source1,Source2,...)
 * constructor(#probes)
 */
public final class Mixer extends Component implements RA //Adder, Bias, Mixer
{
	//private int Src=0,Amp=1;
	//private int probes = 2;
	Mixer(int probes)
	{
		super();
		//this.probes
		probe = new Probe[probes]; 

		int i;
		for(i=0;i<probes;i++)
			probe[i] = new Probe("Source"+i);
	}
	public Source copy()
	{
		return new Mixer(probe.length);
	}
	public double output()
	{
		double value = 0.0;
		int i;
		for(i=0;i<probe.length;i++)
			value += probe[i].output();
		return value;
	}
}