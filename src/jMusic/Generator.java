package jMusic;

/*final class Oscilator extends Component{	public double output()	{		return Math.sin( 			probe[Freq].output() *			probe[Time].output() *			Math.PI*2.0			+			probe[Phase].output() / Math.PI*2.0 *			Math.PI*2.0			) * probe[Amp].output();	  //return sin( probe[1].output() * probe[0].output() + probe[2].output() ) * probe[3].output();}}*/

/**
 * A Phase Component. it can work independently but it is used by oscillators. It is a simple Integrator.
 *     out=integral( dt * freq ) + "Phase"
 * ([Time=],Frequency,[Phase=0])
 */
class Phase extends Component implements St //thursday6esfand77 7:21pm.	see JMusic9.java for previous spec.
{
	//no Amplitude.	because it only makes curroptions.
	//but MAX may be resonable

	protected int Time=0,Freq=1,Phase=2;
	protected int probes = 3;

	Phase()
	{
		super();
		probe = new Probe[probes]; 
		probe[Time] = new Probe("Time");
		probe[Freq] = new Probe("Frequency");
		probe[Phase] = new Probe("Phase");

		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );
		probe("Phase").link( new Constant(0.0) );
		probe("Time").isDefault();
		probe("Phase").isDefault();


		//lastTime=probe[Time].output();
		//lastIntegral=0.0;	//i am sure.
		reset();
	}
	public Source copy(){return new Phase();}
	public void reset()
	{
		lastTime=probe[Time].output();		//is it true?
		lastIntegral=0.0;
	}

	double lastTime;
	double lastIntegral;

	public double output()
	{
		double t = probe[Time].output();
		double dt = t - lastTime;
		lastTime = t;
		//must? be equal? to one sample life cycle!

		lastIntegral = lastIntegral + dt * probe[Freq].output();
		//System.out.println(toString()+"	   	   "+lastIntegral);

		return	probe[Phase].output() + lastIntegral;

		//OLD LINEAR: probe[Freq].output() * probe[Time].output() + probe[Phase].output();
		//[no]fk! integral! 
		//YES!	integral!
	}
}
/**
 * a super class for easy oscillator/generator building.
 * ? is therea problem on probe count?
 * (([Time=],Frequency,[Phase=0]),[Amplitude=1])
 */
//NOTE: RE-ABSTRACTing
public abstract class Generator extends Phase	//Thursday,6esfand77 7:19pm
{
	protected int Amp=3;	//see Phase:: Time=0,Freq=1,Phase=2;
	//Problem solved. because all classes that use Amp are childs of this class

	public Generator()
	{


		Probe newprobes[] = new Probe[probe.length+1];
		System.arraycopy(probe,0, newprobes,0, probe.length);
		probe = newprobes;
		newprobes = null;

		//probe[probe.length-1] = new Constant(1.0);
		probe[Amp] = new Probe("Amplitude");
		probe("Amplitude").link( new Constant(1.0) );
		probe("Amplitude").isDefault();


		probes = probes+1;	//4
		if(probes!=4 || probes!=probe.length-1 || Amp!=probes-1)
			;//

		//Amp = 3;
	}

	private double frac(double t)
	{
		double x;
		x = t - Math.floor(t);
		//if(x<0.0) x = x+1.0;

		//return f(x);

		return x;
	}
	public double phase()
	{
		return frac(super.output());
	}
	abstract public double output();
	abstract public Source copy();
}
/**
 * a Sin oscillator
 * (([Time=],Frequency,[Phase=0]),[Amplitude=1])
 */
final class /*Oscilator*/ SinOscillator extends Generator
{
	public double output()
	{
		return Math.sin( super.phase() * Math.PI*2.0) * probe[Amp].output();
	}
	//what a long evolution trip! see Jmusic0 and compare to this!
	public Source copy(){return new SinOscillator();}
}
/**
 * a SawTooth oscillator. starts at -1, comes up to +1, then restarts from -1.
 * (([Time=],Frequency,[Phase=0]),[Amplitude=1])
 */
final class SawtoothOscillator extends Generator
{
	public double output()
	{
		return ( super.phase() * 2.0 - 1.0 ) * probe[Amp].output();
	}
	public Source copy(){return new SawtoothOscillator();}
}

final class Pulse extends Generator
{
	public double output()
	{
		return ( (super.phase() >=0.5) ? 0.999:-0.999 ) * probe[Amp].output();
	}
	public Source copy(){return new Pulse();}
}
