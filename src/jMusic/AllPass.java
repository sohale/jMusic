package jMusic;
import jMusic.components.Resettable;

/**
 * All-pass network echo
 * see COMB. all probes are like COMB
 */
class AllPass extends Component implements Resettable
	//from SimpleDelay
{
	private int Input=0,Time=1,Delay=2,RevT=3;
	private int probes = 4;

	private double cache[];
	private int queueLen;
	private int queueCounter;
	private double maxDelay;
	double resolution;
	private double feebBackBuffer;

	double sampleFrequency()
	{
		Timer t = (Timer)probe[Time].source;
		return t.getSamplingFrequency();
	}

	AllPass(double maxDelay)
	{
		super();
		probe = new Probe[probes]; 
		md_4c = maxDelay;

		probe[Input] = new Probe("INPUT");
		probe[Time]	 = new Probe("Time");
		probe[Delay] = new Probe("LOOP TIME");
		probe[RevT] = new Probe("REVERB TIME");

		probe("Time").link( new Timer() );
		probe("Time").isDefault();

		resolution = sampleFrequency();
		this.maxDelay = maxDelay + 3.0/resolution;
		queueLen = (int)(resolution * this.maxDelay);	/*maxDelayItems*/
		cache = new double[queueLen];
		queueCounter = -1;
		feebBackBuffer=0;
		reset();
	}
	double md_4c;
	public Source copy(){return new AllPass(md_4c);}
	void sample(double time, double value)	//no output is needed
	{
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
		double time	= probe[Time ].output();
		double delay = probe[Delay].output();

		double t = delay;
		double T = probe[RevT].output();
		double inp = probe[Input].output();
		double g = Math.pow(0.001,t/T);
		
		//System.out.println("g="+g);

		double inp1 = inp+feebBackBuffer*g;
		sample(time,inp1);
		int n = (int)( delay*resolution );
		int i = queueSampleIndex( n );
		double out1 = cache[i];
		feebBackBuffer = out1;
		double out = out1 + inp1*(-g);
		return out;
	}
	public void reset()
	{
		System.out.println("I dont know what to do, should I propagate reset? (no)");
		queueCounter = -1;
		double val = 0;//probe[Input].output();
		for(int i=0;i<cache.length;i++)
			cache[i]=val;
		feebBackBuffer = 0;
	}
}
