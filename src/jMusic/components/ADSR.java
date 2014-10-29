package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;


/**
 * The ADSR Envelope (a Component)
 * ([Time=timer],Rise,[Amplitude=1.0],Duration,Decay).
 * Actually it is not an ADSR, it is a Zuzanaghe.
 */
public final class ADSR extends Component implements RA	  //is an Envelope
{
	private int Time=0,Rise=1,Amp=2,Dur=3,Decay=4;
	private int probes = 5;
	public ADSR()
	{
		super();
		probe = new Probe[probes]; 

		probe[Time] = new Probe("Time");
		probe[Rise] = new Probe("Rise");		//Attack
		probe[Amp]	= new Probe("Amplitude");
		probe[Dur]	= new Probe("Duration");	//Sustain
		probe[Decay]  = new Probe("Decay");		//Decay/Release

		probe("Time").linkD( new Timer() );

		probe("Amplitude").linkD( new Constant(1.0) );
	}
	public Source copy()
	{
		return new ADSR();
	}
	double sgn1(double x)
	{
		if(x>=0) return 100;
		return -100;
	}
	public double output()
	{
		double t = probe[Time].output();
		double att = probe[Rise].output();
		double dur = probe[Dur].output();
		double rel = probe[Decay].output();

		double v = t/att;
		if(att==0) v = sgn1(t);
		if (v > 1.0) v = 1.0;
		if (v < 0.0) v = 0.0;

		double s = (t - att - dur)/rel;		//I thought its a bug.    //double s = -(t - att - dur - rel)/rel;
		if(rel==0) s = sgn1(t - att - dur);
		if (s > 1.0) s = 1.0;
		if (s < 0.0) s = 0.0;

		double a = (v - s);

		//if bypassable
		//if(a==0.0) return 0;

		return a * probe[Amp].output();
	}
}