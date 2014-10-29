package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * The Amplifier, or a simple binary multiplier
 * (Source,Amplitude).
 */
public final class Amplifier extends Component implements RA
{
	private int Src=0,Amp=1;
	private int probes = 2;
	Amplifier()
	{
		super();
		probe = new Probe[probes]; 

		probe[Src] = new Probe("Source");
		probe[Amp]	= new Probe("Amplitude");
	}
	public Source copy()
	{
		return new Amplifier();
	}
	public double output()
	{
		return probe[Src].output() * probe[Amp].output();
		/*
		*
		* Amplitude is often simpler?
		*
		double a = probe[Amp].output();
		if(a==0.0) return 0;
		return probe[Src].output()*a;
		*/
	}
}
