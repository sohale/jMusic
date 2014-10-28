package jMusic;

/**
 * a pitch bend template for later use. my own Idea.
 * implements none!
 * (Note,Time,Range)	//Time=nothing
 */
//PitchBend Template
abstract class PitchBend extends Component		// 11 farvardin 1378 5:08 pm	testOK:	5:27pm

{
	protected int probes = 3;
	protected int Note = 0;
	protected int Time = 1;
	protected int Range = 2;

	PitchBend()
	{
		//super();?!
		probe = new Probe[probes]; 
		probe[Note] = new Probe("Note");	//Input
		probe[Time]  = new Probe("Time");	//Actually, "NoteTime"
		probe[Range] = new Probe("Range");
	}
}
/**
 * Ceiling cut pitch bend. my idea.
 * it is a note-start pbend. It could be like an e^x .?!
 * (Note,Time,Range)	//Time=nothing
 * constructor(power,bias)
 * the only constraint is bias<>1.0. bias can be: bias<0
 * out = Range * _bend_ + note
 * _bend_ = time! ^ power + bias. then, fit/fix it in [0,1]
 *  x = (1-x)/(bias-1)
 */

final class CeilingCut extends PitchBend	//11 farvardin 78		4:46 pm
{
	//of CeilinfBias
	//for use in PITCH BEND
	//see Ceiling
	double power;
	double bias;
	double scale;
	CeilingCut(double power, double bias)
	{
		super();
		this.power = 1.0/power;
		this.bias = bias;
		scale = (1-bias);
			//fix start=-1 end=0
			//the only constraint is bias<>1.0
			//can be: bias<0
	}
	final public Source copy(){return new CeilingCut(power,bias);}
	final public double output()
	{
		double n = probe[Note].output();
		double x = probe[Time].output();
		double a = probe[Range].output();

		if(x<0) a = -a;	//instead of using "s"
		x = Math.abs(x);
		x = Math.pow(x,power) + bias;
		if( x > 1.0 ) x = 1.0;
		if( x < 0.0 ) x = 0.0;

		x = (x-1)/scale;

		return n + x*a;		//*s*a
	}
}

/**
 * Step pitch bend
 * (Note,Time,Range)	//Time=nothing
 * constructor(delay)
 * delay=active period
 * out += (time<delay)*Range
 */
final class Step extends PitchBend	//11 farvardin 78	9:20 pm
{
	double delay;
	Step(double delay)
	{
		super();
		this.delay = delay;
	}
	final public Source copy(){return new Step(delay);}
	final public double output()
	{
		double n = probe[Note].output();
		double t = probe[Time].output();
		double a = probe[Range].output();

		double x;
		if(t<delay)
			x = 1;
		else
			x = 0;

		return n + x*a;
	}
}
