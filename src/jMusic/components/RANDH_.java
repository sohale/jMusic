package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;



/**
 * A Random Generator with narrowed BW. this is a "random access" component, but slow.
 * (["f[R]"=sampleRate/ratio],[Amplitude=1],[Time=])
 * constructor(ratio)
 * f[R] is random generation rate. I think if f[R] > sampleRate, there is no difference with this and a pure RAND.
 *    samples are holded, not interpolated
 */
final class RANDH_ extends Component implements RA
{
	private int fR=0,Amp=1,Time=2;
	private int probes = 3;
	private long privateIndex;	 //makes instances unrelated
	RANDH_(double ratio)
	{
		super();
		probe = new Probe[probes]; 

		probe[fR] = new Probe("f[R]");
		probe[Amp] = new Probe("Amplitude");
		probe[Time] = new Probe("Time");

		Timer timer=new Timer();
		probe("f[R]").linkD( new Constant((timer.getSamplingFrequency())/ratio) );
		probe("Amplitude").linkD( new Constant(1.0) );
		//probe("Time").linkD( new Timer(Timer.sampleFrequency()) );
		probe("Time").linkD( timer );

		privateIndex = (long)(Math.random()*10000+4564);
	}
	public Source copy()
	{
		return new RANDH_(1);	//not important
	}

	 private java.util.Random randBase = new java.util.Random();
	//See pages 97,98 of	 COMPUTER MUSIC,... SECOND EDITION (1997) by C.DODGE and T.A.JERSE
	public double output()
	{
		
		//long index = (long)(t/TR);
		//randBase.setSeed(index);
		//double start = t - index*TR;	//time distance from last random sample
		//long samples = t*fR /fS; // t/TR
		//int i;
		//for(i=0;i<samples;i++)
		// r += randBase.nextDouble();


		//note that : fS > fR ==> in all samples, at most we need to call nextDouble() once!
		double f = probe[fR].output();
		double TR;
			if(f==0.0)
				//TR = 0.0;
				 return randBase.nextDouble()*probe[Amp].output();	// un declared!, really too random!
			else
				TR = 1.0 / f;
		long index = (long)(probe[Time].output()/TR);
		randBase.setSeed(index + privateIndex);
		randBase.nextDouble();	//shake! for more uniformity!
		return (randBase.nextDouble()*2.0-1.0)*probe[Amp].output();

	/*
		double r = 0.0;
		double t = probe[Time].output();
		double TR = 1.0 / probe[fR].output();				// Div 0
		double lastTime = TR*(double)((int)(t/TR));
		double delt = t - lastTime;
		double Tsample = 1.0/((Timer)(probe[Time].source)).sampleFrequency();
		if(delt <= Tsample)
			r = lastRandom;
		else
			while(delt > Tsample)
			{
				delt -= Tsample;
				r += Math.random();
			}
		lastRandom = r;
		return r * probe[Amp].output();	//fR =	fS
	*/
	}
}