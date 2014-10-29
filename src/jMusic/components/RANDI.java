package jMusic.components;

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
