package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;



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
		Timer t = (Timer)probe[Time].getSource();
		return t.getSamplingFrequency();
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
