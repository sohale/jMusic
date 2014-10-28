package jMusic;
//class IIR

/**
 * a general purpose filter.
 * page 174
 *	ULTIMATE
 *	FROM 29 ESFAND 1378 to 1 FARVARDIN 1379
 * I feel power.
 */

//class IdealLowpass	extends Component implements St		//Filter1		page 203

/**
 * This filter is better for noisy inputs and standard for that book.
 * page 212
 */
class Filter1 extends Component implements St //page 211,212 for section 6.3
{
	protected int in=0,f0=1,BW=2;
	Filter1()
	{
		Probe[] p = { new Probe("Input"), new Probe("f0"), new Probe("BW") };
		probe = p;
		reset();
	}
	public Source copy(){return new Filter1();}
	double y_1,y_2;
	public void reset()
	{
		y_1 = 0;
		y_2 = 0;		// ?
	}
	public double output()
	{
		double fs = Timer.sampleFrequency();
		double b2 = Math.exp(-2*Math.PI*probe[BW].output()/fs);
		double b1 = (-4*b2)/(1+b2)*Math.cos(2*Math.PI*probe[f0].output()/fs);
		//double a0 = (1.0-b2)*Math.sqrt(1-b1*b1/(4*b2));
		double a0 = Math.sqrt((1-b2)/(1+b2)*((1+b2)*(1+b2)-b1*b1));
		double x = probe[in].output();
		double y = a0*x - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		return y;
	}
}

/**
 * This filter outputs 1.0 at f0.
 * page 211
 */
class Filter2 extends Filter1
{
	public double output()
	{
		double fs = Timer.sampleFrequency();
		double b2 = Math.exp(-2*Math.PI*probe[BW].output()/fs);
		double b1 = (-4*b2)/(1+b2)*Math.cos(2*Math.PI*probe[f0].output()/fs);
		double a0 = (1.0-b2)*Math.sqrt(1-b1*b1/(4*b2));
		double x = probe[in].output();
		double y = a0*x - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		return y;
	}
	public Source copy(){return new Filter2();}
}


/**
 * Butterworth band-pass filter
 * page 216
 */
class ButterworthBandpass extends Component implements St
{
	protected int in=0,f0=1,BW=2;
	ButterworthBandpass()
	{
		Probe[] p = { new Probe("Input"), new Probe("f0"), new Probe("BW") };
		probe = p;
		reset();
	}
	public Source copy(){return new ButterworthBandpass();}
	double y_1,y_2,x_1,x_2;
	public void reset()
	{
		y_1 = 0;
		y_2 = 0;		// ?
		x_1 = 0;
		x_2 = 0;
	}
	public double output()
	{
		double fs = Timer.sampleFrequency();
		double C = 1/Math.tan(Math.PI*probe[BW].output()/fs);
		double D = 2*Math.cos(2*Math.PI*probe[f0].output()/fs);
		double a0 = 1/(1+C);
		double a1 = 0;
		double a2 = -a0;
		double b1 = -a0*C*D;
		double b2 = a0*(C-1);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
}

class ButterworthBandreject extends ButterworthBandpass
{
	public double output()
	{
		double fs = Timer.sampleFrequency();
		double C = Math.tan(Math.PI*probe[BW].output()/fs);
		double D = 2*Math.cos(2*Math.PI*probe[f0].output()/fs);
		double a0 = 1/(1+C);
		double a1 = -a0*D;
		double a2 = a0;
		double b1 = -a0*D;
		double b2 = a0*(1-C);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
}

class ButterworthLowpass extends Component implements St
{
	protected int in=0,fc=1;
	ButterworthLowpass()
	{
		Probe[] p = { new Probe("Input"), new Probe("fc") };
		probe = p;
		reset();
	}
	public Source copy(){return new ButterworthLowpass();}
	double y_1,y_2,x_1,x_2;
	public void reset()
	{
		y_1 = 0;
		y_2 = 0;		// ?
		x_1 = 0;
		x_2 = 0;
	}
	public double output()
	{
		double fs = Timer.sampleFrequency();
		double C = 1/Math.tan(Math.PI*probe[fc].output()/fs);
		double a0 = 1/(1+Math.sqrt(2)*C+C*C);
		double a1 = 2*a0;
		double a2 = a0;
		double b1 = 2*a0*(1-C*C);
		double b2 = a0*(1-Math.sqrt(2)*C+C*C);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
}
class ButterworthHighpass extends ButterworthLowpass
{
	public double output()
	{
		double fs = Timer.sampleFrequency();
		double C = Math.tan(Math.PI*probe[fc].output()/fs);
		double a0 = 1/(1+Math.sqrt(2)*C+C*C);
		double a1 = -2*a0;
		double a2 = a0;
		double b1 = 2*a0*(C*C-1);
		double b2 = a0*(1-Math.sqrt(2)*C+C*C);
		double x = probe[in].output();
		double y = a0*x + a1*x_1 + a2*x_2 - b1*y_1 - b2*y_2;
		y_2 = y_1;
		y_1 = y;
		x_2 = x_1;
		x_1 = x;
		return y;
	}
	public Source copy(){return new ButterworthHighpass();}
}



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
		probe("fR").link( new Constant((Timer.sampleFrequency())/ratio) );
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
/**
 * with Interpolation
 * ([fR],[Amplitude],[Time])
 */
class RANDI extends RANDH
{
	double nextRSample;
	public void reset()
	{
		super.reset();
		nextRSample = randBase.nextDouble();
	}
	public double output()
	{
		double t = probe[Time].output();
		double fr = probe[fR].output();
		double between = 1.0/fr;
		//System.out.println(t-lastSrt);
		if(t-lastSrt>=between)
		{
			//lastSrt = t-between;		//or t ?
			lastSrt  = t;
			lastRSample = nextRSample;
			nextRSample = randBase.nextDouble();
		}
		double x = (t-lastSrt)/between;
		double s = (1.0-x)*lastRSample + (x-0.0)*nextRSample;
		return (s*2.0-1.0)*probe[Amp].output();
	}
}
