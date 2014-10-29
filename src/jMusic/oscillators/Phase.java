package jMusic.oscillators;
import jMusic.*;
import jMusic.components.Constant;
import jMusic.components.Resettable;


/*final class Oscilator extends Component{	public double output()	{		return Math.sin( 			probe[Freq].output() *			probe[Time].output() *			Math.PI*2.0			+			probe[Phase].output() / Math.PI*2.0 *			Math.PI*2.0			) * probe[Amp].output();	  //return sin( probe[1].output() * probe[0].output() + probe[2].output() ) * probe[3].output();}}*/

/**
 * A Phase Component. it can work independently but it is used by oscillators. It is a simple Integrator.
 *     out=integral( dt * freq ) + "Phase"
 * ([Time=],Frequency,[Phase=0])
 */
public class Phase extends Component implements Resettable //thursday6esfand77 7:21pm.	see JMusic9.java for previous spec.
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
