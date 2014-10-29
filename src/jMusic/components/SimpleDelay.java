package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;


/**
 * a Simple Delay. Very static? because it has MAXDELAY. it is St but Delay is RA?
 * what is the difference?
 * (Input,[Time=],Delay)
 * constructor(maxDelay)
 * Delay <= const maxDelay
 * maxDelay is in seconds.
 */
public final class SimpleDelay extends Component implements St	  //1:01xm sat 16aban77
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
		Timer t = (Timer)probe[Time].getSource();
		return t.getSamplingFrequency();
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
