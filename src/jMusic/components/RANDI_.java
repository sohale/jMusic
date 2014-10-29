package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;



/**
 * A Random Generator with narrowed BW. this is a "random access" component, but slow.
 * (["f[R]"=sampleRate/ratio],[Amplitude=1],[Time=])
 * f[R] is random generation rate. I think if f[R] > sampleRate, there is no difference with this and a pure RAND.
 *    samples are **interpolated linearly** between the random samples.
 * constructor(ratio)
 */
public final class RANDI_ extends Component implements RA //1:47xm
{
	//se RANDH comments
	private int fR=0,Amp=1,Time=2;
	private int probes = 3;
	private long privateIndex; //makes instances unrelated
	RANDI_(double ratio) // ratio must be > 1.0	to work correctly
	{
		super();
		probe = new Probe[probes]; 

		probe[fR] = new Probe("f[R]");
		probe[Amp] = new Probe("Amplitude");
		probe[Time] = new Probe("Time");

		Timer timer=new Timer();
		probe("f[R]").link( new Constant((timer.getSamplingFrequency())/ratio) ); //must be less than fS(=sampleFrequency)
		probe("Amplitude").link( new Constant(1.0) );
		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( timer );
		privateIndex = (long)(Math.random()*10000 + 6546);
	}
	public Source copy()
	{
		return new RANDI_(1);
	}

	 private java.util.Random randBase = new java.util.Random(); ///not static
	double random(long index)
	{
		randBase.setSeed(index + privateIndex );
		randBase.nextDouble();	//shake! for more uniformity!
		return randBase.nextDouble();
	}
	//See pages 97,98 of	 COMPUTER MUSIC,... SECOND EDITION (1997) by C.DODGE and T.A.JERSE
	public double output()
	{
		double f = probe[fR].output();
		double TR;
			if(f==0.0)
				 return randBase.nextDouble()*probe[Amp].output();	// un declared!, really too random!
			else
				TR = 1.0 / f;
		double t = probe[Time].output();
		long index = (long)(t/TR);
		double c = 1.0 - (t - index*TR)/TR;
		return (((c)*random(index)+(1.0-c)*random(index+1))*2.0-1.0)*probe[Amp].output();
	}
	//Yeaa, It fkly worked at first RUN.		Long live java ...
}