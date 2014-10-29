package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * =RAND?=a simple random. fR=sampleRate (fR=fS).
 * ([Amplitude=1],[Bias=0])
 */
//11 farvardin 1378 1:2..xm	- 1:35xm	2:00xm:Bias	zerofix 2:08xm(-0+)		"Stomper"-like is possible
//no timer, no freq., ...
final class SimpleRandom extends Component
{
	private int probes = 2;
	private final int Amp=0;	//just an easier
	private final int Bias=1;	//just an easier
	double preAmp;
	SimpleRandom(double preAmp)
	{
		super();
		probe = new Probe[probes]; 
		probe[Amp] = new Probe("Amplitude");
		probe[Bias] = new Probe("Bias");

		this.preAmp = preAmp;
		probe("Amplitude").link( new Constant(1.0) );
		probe("Bias").link( new Constant(0.0) );
	}
	public Source copy()
	{
		return new SimpleRandom(preAmp);
	}
	
	private java.util.Random randomGenerator = new java.util.Random();

	public double output()	{
		double v = probe[Amp].output();
		double b = probe[Bias].output();
		return v*preAmp* (2*randomGenerator.nextDouble()-1) + b;
	}
}