package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;


/**
 * see SimpleDelay
 * it can handle non integer delay samples
 */
public final class AntiAliasDelay extends Component implements Resettable
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
		Timer t = (Timer)probe[Time].getSource();
		return t.getSamplingFrequency();
		//return Timer.sampleFrequency();
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
