package jMusic;
/**
 * main sample must be sampled
 * no input line
 * tested OK. it works. see java37.
 * 11:06am 25 farvardin 79
 * 
 *      *************** this*****************************
 *      *                                               *
 *      *                                               *
 *      *              ** mc**************              *                 
 *      *              *                 *              *
 *      *              *              out-*-->--------out*
 *      *              *                 *              *
 *      *              *                 *              *
 *      *      +------P-"back0"  "feed0"-0*-->-----+----O*--- Feedback0
 *	    *      |       *                 *         |    *
 *      *      | +----P-"back1"  "feed1"-1*-->--+--|----O*--- Feedback1
 *      *      | |.    *                 *     .|  |    *     ...             
 *      *      | |.    *******************     .|  |    *
 *      *      | |.                            .|  |    *
 *      *      | |...       ...          ...    |  |    *
 *      *      | +------------------------------+  |    * 
 *      *      |                                   |    *
 *      *      +-----------------------------------+    *
 *      *                                               *
 *      *                                               *
 *      *************************************************
 */
class Feedback2 extends MultiOutputComponent implements Se
{
	double[] feedbackBuffer;
	MOCompound compound;
	int n;
	int feed[];

	Feedback2(MOCompound mc, int feedbackLines)
	{
		super();
		n = feedbackLines;
		feedbackBuffer = new double[n];
		outputNames = new String[n];
		for(int i=0;i<n;i++)
		{
			feedbackBuffer[i] = 0;
			outputNames[i] = "Feedback"+i;
		}
		probe = new Probe[0];
		//probes can be added manually in program level

		compound = mc;

		feed = new int[n];
		for(int i=0;i<n;i++)
			feed[i] = compound.outputIndex("feed"+i);

		link();
	};
	public Source copy(){return null;}

	private void link()
	{
		for(int i=0;i<n;i++)
		{
			compound.probe("back"+i).link(outputHandle("Feedback"+i));
			System.out.println("Probe "+compound+" . "+compound.probe("back"+i).name+" linked to "+ "Feedback"+i);
		}
	}
	public double output(int fbl)	//gets output from feedback lines
	{
		return feedbackBuffer[fbl];
	}
	/**
	 * HEART of feedback
	 */
	public double output()
	{
		//if [re]sample
			for(int i=0;i<n;i++)
				/** here, it gets the sample */
				feedbackBuffer[i] = compound.output(feed[i]);
		//else
		return compound.output();
	}
	void setOutputNames()
	{
		//outputNames = new String[0];
	}
}
/**
 * 2-CHECKINGS FOR REP. NAMES. also for MO/Compound
 * 3-generally, accurate error checking.
 */