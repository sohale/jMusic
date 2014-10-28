package jMusic;
//import java.util.Random;

//is each class: (what to do with Time)
//-jumpable
//-few skips
//-needs reset
//show these by interfaces. easy

//interface Instant,RandomAccess,Functional,		//jumpable
//interface Sequencial,Serial,					//few skips. dt may be unequal
//interface SideEffect,Manual,StartStop				//needs reset. needs serialisation.
												//dt may be unequal
					//interface Phased. (a subtype of Sequencial. for generators)

interface RA {};
interface Se {};
interface St {void reset();};	//or firstSample? 

/*class document template:

 * desc:				* a Simple Delay. Very static? because it has MAXDELAY. it is St but Delay is RA?
 * desc:				* what is the difference?
 * probes:				* (Input,[Time=],Delay)
 * constr:(optional)	* constructor(maxDelay)
 * conditions			* Delay <= const maxDelay
 * descr. for probes:	* or parameters or formulae

*/

/**
 * this class implements a Constant value. for editors, hidden constants, parameters, zeros, test values, ...
 */
final class Constant implements Source,RA //,BrowserInformation
{
   double foreverValue;
   Constant(double forever)
   {
      foreverValue = forever;
   }
   public Source copy()
   {
	   return new Constant(foreverValue);
   }
   public double output()
   {
      return foreverValue;
   }
	public String toString()
	{
		//return "Constant("+(int)(foreverValue*10000)/10000.0+")";
		return ""+(int)(foreverValue*10000)/10000.0+" const";
	};
}


///final class Oscilator extends Component

/**
 * The Amplifier, or a simple binary multiplier
 * (Source,Amplitude).
 */
final class Amplifier extends Component implements RA
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

/**
 * The Mixer=Adder (PLUS +)
 * (Source0,Source1,Source2,...)
 * constructor(#probes)
 */
final class Mixer extends Component implements RA //Adder, Bias, Mixer
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

/**
 * a Multi-source Multiplier. an extention for Amplifier. ( * MULT X )
 * (Source0,Source1,Source2,...)
 * constructor(#probes)
 */
final class Multiplier extends Component implements RA
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

/**
 * The ADSR Envelope (a Component)
 * ([Time=timer],Rise,[Amplitude=1.0],Duration,Decay).
 * Actually it is not an ADSR, it is a Zuzanaghe.
 */
final class ADSR extends Component implements RA	  //is an Envelope
{
	private int Time=0,Rise=1,Amp=2,Dur=3,Decay=4;
	private int probes = 5;
	ADSR()
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

/**
 * a Cut distorter, fixes output between [-1,+1] (a Component)
 * (Input)
 */
final class Cut extends Component implements RA	  //later: lbound,hbound
{
	private int Inp=0;
	private int probes = 1;
	Cut()
	{
		super();
		probe = new Probe[probes]; 

		probe[Inp] = new Probe("Input");
	}
	public Source copy()
	{
		return new Cut();
	}
	public double output()
	{
		double v = probe[Inp].output();
		if (v > +1.0) v = +1.0;
		if (v < -1.0) v = -1.0;
		return v;
	}
}
/**
 * a simple Pulse generator. (a RA Component, not phased)
 * ([Time=],Frequency,[Phase=0],[Amplitude=1],[Delay=0],[Coefficient=0.5])
 * Coefficient= ratio between up and down durations by a whole period
 */
final class PulseGenerator extends Component implements RA
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

		probe("f[R]").linkD( new Constant((Timer.sampleFrequency())/ratio) );
		probe("Amplitude").linkD( new Constant(1.0) );
		//probe("Time").linkD( new Timer(Timer.sampleFrequency()) );
		probe("Time").linkD( new Timer() );

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

/**
 * A Random Generator with narrowed BW. this is a "random access" component, but slow.
 * (["f[R]"=sampleRate/ratio],[Amplitude=1],[Time=])
 * f[R] is random generation rate. I think if f[R] > sampleRate, there is no difference with this and a pure RAND.
 *    samples are **interpolated linearly** between the random samples.
 * constructor(ratio)
 */
final class RANDI_ extends Component implements RA //1:47xm
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

		probe("f[R]").link( new Constant((Timer.sampleFrequency())/ratio) ); //must be less than fS(=sampleFrequency)
		probe("Amplitude").link( new Constant(1.0) );
		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );
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

/**
 * A Delay Component. needs reset.
 * I really dont know about being RA or St. is it even safe?!
 * (Input,[Time=],Delay)
 * constructor(maxDelay)
 * Delay <= const maxDelay
 */
final class Delay extends Component implements RA	  //start 11:24am sat 15aban77
{
	// cache...
	private int Input=0,Time=1,Delay=2;		//<<===---***
	private int probes = 3;

	private double cache[];
	private double sampleTime[];
	private int queueLen;
	private int queueCounter;
	private double maxDelay;

	double sampleFrequency()
	{
		Timer t = (Timer)probe[Time].source;
		return t.sampleFrequency();
	}

	Delay(double maxDelay)
	{
		super();
		maxDelay_4c = maxDelay;
		probe = new Probe[probes]; 

		probe[Input] = new Probe("Input");
		probe[Time]	 = new Probe("Time");
		probe[Delay] = new Probe("Delay");

		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );

		double resolution = sampleFrequency();
		this.maxDelay = maxDelay + 3.0/resolution;
		queueLen = (int)(resolution * this.maxDelay);	/*maxDelayItems*/
		cache = new double[queueLen];
		sampleTime = new double[queueLen];
		queueCounter = -1;	//TOoO exciting
	}
	double maxDelay_4c;
	public Source copy()
	{
		return new Delay(maxDelay_4c);
	}
	//void firstSample(double value)
	//{
	// time =;
	// value =;
	// int i;
	// for(i=0;i<queueLen;i++)
	// {
	//		cache[i] = value;
	//		sampleTime[i] = time;
	// }
	//}

	void sample(double time)	//no output is needed
	{
		//double time;
		//time	= probe[Time ].output();
		double value;
		value = probe[Input].output();
		queueCounter ++;
		if(queueCounter >= queueLen)
			queueCounter -= queueLen;
		  cache[queueCounter] = value;
		sampleTime[queueCounter] = time;
	}
	int queueSampleIndex(int number)
	{
		int i;
		i = queueCounter - number;
		if(i<0) i=i+queueLen;
		if(i<0) new Error("Too far Delay impossible");
		return i;
	}

	double resolution;	//be carefull

	public double output()
	{
		double time;
		time	= probe[Time ].output();
		double delay;
		delay = probe[Delay].output(); //on-line!

		resolution = sampleFrequency(); //Variant resolution!

		sample(time);	//we may need zero delay
		int n;
		n = (int)( (/*time -*/ delay)*resolution );			//Steppy , Alias here!
		int i;
		i = queueSampleIndex( n );
		double out = cache[i];				//Again Alias. **ALIASING CENTER**

		//sample();
		return out;
	}
/*
	private int[2] findTwoSides()
	{
		int n;
		n = (time - delay)*resolution + 1;			//Steppy , Alias here!
		int i;
		i = queueSampleIndex( n );
		double t1;
		t1 = sampleTime[i];
		double t2;
		t2 = sampleTime[i+1];
		if(t2-t1 <= resolution*1.001)
			return (int[]){i,i+1};
			
			
	}
*/
}

/**
 * a Simple Delay. Very static? because it has MAXDELAY. it is St but Delay is RA?
 * what is the difference?
 * (Input,[Time=],Delay)
 * constructor(maxDelay)
 * Delay <= const maxDelay
 * maxDelay is in seconds.
 */
final class SimpleDelay extends Component implements St	  //1:01xm sat 16aban77
{
	private int Input=0,Time=1,Delay=2;
	private int probes = 3;

	private double cache[];
	private int queueLen;
	private int queueCounter;
	private double maxDelay;
	double resolution;

	double sampleFrequency()
	{
		Timer t = (Timer)probe[Time].source;
		return t.sampleFrequency();
	}

	SimpleDelay(double maxDelay)
	{
		super();
		probe = new Probe[probes]; 
		maxDelay_4c = maxDelay;

		probe[Input] = new Probe("Input");
		probe[Time]	 = new Probe("Time");
		probe[Delay] = new Probe("Delay");

		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );

		resolution = sampleFrequency();
		this.maxDelay = maxDelay + 3.0/resolution;
		queueLen = (int)(resolution * this.maxDelay);	/*maxDelayItems*/
		cache = new double[queueLen];
		queueCounter = -1;
		reset();
	}
	double maxDelay_4c;
	public Source copy()
	{
		return new SimpleDelay(maxDelay_4c);
	}
	void sample(double time)	//no output is needed
	{
		double value;
		value = probe[Input].output();
		queueCounter ++;
		if(queueCounter >= queueLen)
			queueCounter -= queueLen;
		  cache[queueCounter] = value;
		//sampleTime[queueCounter] = time;		//??????????????????????????? I'M not SURE
	}
	int queueSampleIndex(int number)
	{
		int i;
		i = queueCounter - number;
		if(i<0) i=i+queueLen;
		if(i<0) new Error("Too far Delay impossible");
		return i;
	}

	public double output()
	{
		double time;
		time	= probe[Time ].output();
		double delay;
		delay = probe[Delay].output();

		sample(time);

		int n;
		n = (int)( delay*resolution );

		int i;
		i = queueSampleIndex( n );
		double out = cache[i];				//**ALIASING**
		return out;
	}
	public void reset()
	{
		System.out.println("I dont know what to do, should I propagate reset? (no)");
		queueCounter = -1;
		double val = /*probe[Input].output()*/0;
		for(int i=0;i<cache.length;i++)
			cache[i]=val;
	}
}

/**
 * a note to frerquency filter. uses 12 notes in octav!! base freq=440Hz
 * (Note)
 */
final class NoteFrequency extends Component implements RA		//jMusic9.java: a bit to music: 8.9.77.sat07:37pm
{
	static double baseFrequency = 440.0;
	static double notesPerOctave = 12;			// ! !
	private int Note=0;
	private int probes = 1;
	NoteFrequency()
	{
		super();
		probe = new Probe[probes]; 
		probe[Note] = new Probe("Note");
	}
	public Source copy()
	{
		return new NoteFrequency();
	}
	//and another constructor with "base freq"
	public double output()
	{
		double v = probe[Note].output();
		return baseFrequency*Math.pow( 2.0 , v / notesPerOctave );
	}
}

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



/*
A COMPONENT DEFINITION TEMPLATE
--------------------------------
class XXXX extends Component	{
	private final int Inp=0;
	*...
	private int probes = 4;
	XXXX()	{
		super();
		probe = new Probe[probes]; 

		probe[Inp] = new Probe("Input");	//define probes
		*...

		//probe("Time").link( new Timer(22050) );
		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );


	}
	public double output()	{
		double v = probe[Inp].output();
		return v;
	}
}
*/

final class StomperEnvelope extends Component implements RA
{
	double t0,t1,v0,v1,a;

	private int Time=0;
	StomperEnvelope(double t0, double t1, double v0, double v1, double a)
	{
		super();
		probe = new Probe[1]; 

		probe[Time] = new Probe("Time");
		probe("Time").linkD( new Timer() );
		this.t0 = t0;
		this.t1 = t1;
		this.v0 = v0;
		this.v1 = v1;
		this.a = a;
	}
	public Source copy()
	{
		return new StomperEnvelope(t0,t1,v0,v1,a);
	}
	public double output()
	{
		double t = probe[Time].output();
		double x = (t-t0)/(t1-t0);
		if(x<0) return 0;
		if(x>1) return 0;
		//double y = 1-Math.pow(1-x,a);
		double y = Math.pow(x,a);
		//System.out.println(y);
		return v0+(v1-v0)*y;
	}
}

class Divider extends Component implements RA
{
	Divider()
	{
		super();
		probe = new Probe[2]; 
		probe[0] = new Probe("Source0");
		probe[1]	= new Probe("Source1");
	}
	public Source copy()
	{
		return new Divider();
	}
	public double output()
	{
		return probe[0].output() / probe[1].output();
	}
}

/**
 * Exponential Approch
 * y' = (1-a)*y + a*x
 * one sample per output. time in not considered.
 * (has simple feedback)
 * it' beh. is depended to sample rate
 */
class Alpha extends Component implements St
{
	Alpha()
	{
		super();
		probe = new Probe[2]; 
		probe[0] = new Probe("Input");
		probe[1]	= new Probe("Alpha");
		reset();
	}
	public Source copy()
	{
		return new Alpha();
	}
	public void reset()
	{
		y=0;
	}
	double y;
	public double output()
	{
		double x = probe[0].output();
		double a0 = probe[1].output();
		double a = 1/(1*0+a0)/Timer.sampleFrequency();
		y = (1-a)*y + (0+a)*x;
		return y;
	}
}

/**
 * positive( A * "Source0" + B * "Source1" + C )
 * constructor(A,B,C)
 */
class Linear2 extends Component implements RA
{
	double[] a = new double[2];
	double c;
	Linear2(double A, double B, double C)
	{
		super();
		probe = new Probe[2]; 
		for(int i=0;i<probe.length;i++)
			probe[i] = new Probe("Source"+i);
		//for(int i=0;i<probe.length;i++)
		//	a[i] =
		a[0]=A;
		a[1]=B;
		c=C;
	}
	public Source copy()
	{
		return new Linear2(a[0],a[1],c);
	}
	public double output()
	{
		double value = c;
		for(int i=0;i<probe.length;i++)
			value += probe[i].output() * a[i];
		if(value<0) value=0;
		return value;
	}
}

/**
 * see SimpleDelay
 * it can handle non integer delay samples
 */
final class AntiAliasDelay extends Component implements St
{
	private int Input=0,Time=1,Delay=2;
	private int probes = 3;

	private double cache[];
	private int queueLen;
	private int queueCounter;
	private double maxDelay;
	double resolution;
	
	double dither;		//NEW

	double sampleFrequency()
	{
		Timer t = (Timer)probe[Time].source;
		return t.sampleFrequency();
	}

	AntiAliasDelay(double maxDelay)
	{
		super();
		probe = new Probe[probes]; 
		maxDelay_4c = maxDelay;

		probe[Input] = new Probe("Input");
		probe[Time]	 = new Probe("Time");
		probe[Delay] = new Probe("Delay");

		probe("Time").linkD( new Timer() );

		resolution = sampleFrequency();
		this.maxDelay = maxDelay + 3.0/resolution;
		queueLen = (int)(resolution * this.maxDelay);
		cache = new double[queueLen];
		queueCounter = -1;
		dither=0;
		//is("output Single Sample");
		reset();
	}
	double maxDelay_4c;
	public Source copy()
	{
		return new SimpleDelay(maxDelay_4c);
	}
	//int l=-3;
	void sample(double time)	//no output is needed
	{
		double value;
		value = probe[Input].output();
		queueCounter ++;
		if(queueCounter >= queueLen)
			queueCounter -= queueLen;
		  cache[queueCounter] = value;
		//sampleTime[queueCounter] = time;		//??????????????????????????? I'M not SURE
		/* seq test
		if(queueCounter!=l+1)
		{
			//for(int i=0;i<cache.length/4;i++)
			//	System.out.print(cache[i]+" ");
			System.out.println("was "+l+" is "+queueCounter+"  "+value);
			//NaN
		}
		l=queueCounter;
		 */ 
	}
	int queueSampleIndex(int number)
	{
		int i;
		i = queueCounter - number;
		if(i<0) i=i+queueLen;
		if(i<0) new Error("Too far Delay impossible");
		return i;
	}

	int l;
	boolean y=false;
	public double output()
	{
		double time;
		time	= probe[Time ].output();
		double delay;
		delay = probe[Delay].output();

		sample(time);

		int n = (int)( delay*resolution );
		double f = delay*resolution - n;
		dither+=f;
		if(dither>1)
		{
			n = n + (int)dither;
			dither = dither - (int)dither;
		}

		int i;
		i = queueSampleIndex( n );
		//System.out.println(i);
		double out = cache[i];				//**ALIASING**
/*
		if((!(out>-2) && !(out<1)) 
		// ||y 
		)
		{
			System.out.println("n="+n);
			System.out.println(i);
			System.out.println("l="+l);
			System.out.println(out);
			System.out.println("t="+
							   //new Timer().output()  resets timer
							   Timer.tick);
			try{System.in.read();}catch(Exception e){}
			y=true;
		}
		l=i;
		*/
		return out;
	}
	public void reset()
	{
		//System.out.println("I dont know what to do, should I propagate reset? (no)");
		queueCounter = -1;
		double val = /*probe[Input].output()*/0;
		for(int i=0;i<cache.length;i++)
			cache[i]=val;
		dither=0;
	}
}
