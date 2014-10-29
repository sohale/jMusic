package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;


/**
 * a simple Pulse generator. (a RA Component, not phased)
 * ([Time=],Frequency,[Phase=0],[Amplitude=1],[Delay=0],[Coefficient=0.5])
 * Coefficient= ratio between up and down durations by a whole period
 */
public final class PulseGenerator extends Component implements RA
{
	private int Time=0,Freq=1,Phase=2,Amp=3,Del=4,Perc=5;
	private int probes = 6;
	PulseGenerator()
	{
		super();
		probe = new Probe[probes]; 

		probe[Time ] = new Probe("Time");
		probe[Freq]	 = new Probe("Frequency");
		probe[Phase] = new Probe("Phase");
		probe[Amp]	 = new Probe("Amplitude");
		probe[Del]	 = new Probe("Delay");
		probe[Perc]	 = new Probe("Coefficient");	//Percent of Zero

		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").linkD( new Timer() );
		probe("Amplitude").linkD( new Constant(1.0) );
		probe("Phase").linkD( new Constant(0.0) );
		probe("Delay").linkD( new Constant(0.0) );
		probe("Coefficient").linkD( new Constant(0.5) );
		//Freq
	}
	public Source copy()
	{
		return new PulseGenerator();
	}
	public double output()
	{
		double T;
		double f = probe[Freq].output();
		if(f==0.0)
			T = 0.0;
		else
			T = 1.0/f;
		//double v;
		double t = probe[Time].output() - probe[Phase].output();
		double Dt = t - T*(int)(t/T);
		//v = Dt/T;
		double r;
		if(Dt/T > probe[Perc].output())
			r = 1.0;
		else
			r = 0.0;
		return r;
	}
}