package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;
import jMusic.Timer;


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
