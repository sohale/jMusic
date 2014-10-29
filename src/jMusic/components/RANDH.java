package jMusic.components;

import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;

//-----------------------------------------------------------------
/**
 * without Interpolation
 * see @RANDH_
 * ([fR],[Amplitude],[Time])
 */
class RANDH extends Component implements St
{
	protected int fR=0,Amp=1,Time=2;
	protected int probes = 3;
	RANDH()
	{
		super();
		probe = new Probe[probes]; 

		probe[fR] = new Probe("fR");
		probe[Amp] = new Probe("Amplitude");
		probe[Time] = new Probe("Time");

		int ratio=6;
		//fixed fs
		Timer timer=new Timer(); 
		probe("fR").link( new Constant((timer.getSamplingFrequency())/ratio) );
		probe("Amplitude").link( new Constant(1.0) );
		probe("Time").link( new Timer() );
		
		probe("fR").isDefault();
		probe("Amplitude").isDefault();
		probe("Time").isDefault();
		
		reset();
		
	}
	public Source copy(){return new RANDH();}

	protected java.util.Random randBase;
	//double lastt;
	double lastSrt;	//last random Sample [remained] time
	double lastRSample;
	
	public double output()
	{
		double t = probe[Time].output();
		double fr = probe[fR].output();
		double between = 1.0/fr;
		if(t-lastSrt>=between)
		{
			//lastSrt = t-between;		//or t ?
			lastSrt  = t;
			lastRSample = randBase.nextDouble();
		}
		return (lastRSample*2.0-1.0)*probe[Amp].output();
	}

	public void reset()
	{
		//the following line can be in constructor.
		randBase = new java.util.Random();	//this means no "random" seed

		lastSrt=probe[Time].output();
		lastRSample = randBase.nextDouble();
	}
}
