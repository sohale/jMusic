package jMusic;

/**
 * a never-ending story.
 * it has many static members. so
 * We actually have only one Timer Object in our whiole run system, and it is start of Side-Effects
 * Timer objects can be "new"ed But they are actually one and connected.
 * specially, "Timer.sampleFrequencty()" is widely, directly used within all classes. so, sample rate is? a uniform? parameter of system.
 * Timer.reset must? be done before all other reset methods of St Components
 * usage: new MANY timers, then reset, then tick and sample.
 */
final class Timer implements Source,St /*?*/         // Source + some Commands (Discrete/Eventy)
{
	//previous usage: (not now) probe("Time").link( new Timer(Timer.sampleFrequency()) );

   static long tick;
   static double dt = 1.0/22050.0;	//for no specifying it
		//was: dt = 22050.0; !!!
   Timer(double samplingFrequency)//double dt
   {
      this.dt = 1/samplingFrequency;//dt;
      reset();
   }
   Timer()
   {
      reset();
   }
	final public Source copy(){return new Timer();}

   
   public void reset()
   {
	   Timer.reset_();
   }
   public static void reset_()
   {
      tick = 0;
   }
   static void tick()
   {
      tick++;
   }
   final static double sampleFrequency()  //Resolution
   {
      return 1/dt;
   }
/* static double dT()
   {
      return dt;
   }
*/
   //! jMusic.java(91,2) : error J0062: Attempt to reduce access level of member 'double Source.output()'
	public
	final //*************
	double output()
	{
		//System.out.println(dt * (double)tick);
		return dt * (double)tick;
	}
	public String toString()
	{
		return "Timer("+1.0/dt+")";
	};

}
