package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * a Multi-source Multiplier. an extention for Amplifier. ( * MULT X )
 * (Source0,Source1,Source2,...)
 * constructor(#probes)
 */
public final class Multiplier extends Component implements RA
{
	//private int Src=0,Amp=1;
	//private int probes = 2;
	Multiplier(int probes)
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
		return new Multiplier(probe.length);
	}
	public double output()
	{
		double value = 1.0;
		int i;
		for(i=0;i<probe.length;i++)
			value *= probe[i].output();
		return value;
	}
}