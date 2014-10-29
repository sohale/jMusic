
package jMusic;

import jMusic.components.Se;

//Miraculus Feedback
//LEFT: test for equality and redundancy

/**
 * This is sensitive to number of Samples got from it
 * several feedback lines.
 * One input
 * outputs
 * limitations:
 *		1- now, only one input.
 *		2- speed? (again?)
 * A very good instance solution to ALL above is,
 * not having any Probe (probes=0).
 * but:
 *		3-only one seperate output.
 * OH! solved, 10 seconds after problem spec. !
 * 
 * 
 * 
 *      *************** this**************************
 *      *                                            *
 *      P"Input"--+                                  *
 *      *         |                                  *
 *      *         |    ** mc***********              *                 
 *      *         |    *              *              *
 *      *         +->--P-"Input"  out-*-->--------out*
 *      *              *              *              *
 *      *              *              *              *
 *      *      +-------P-"Line0"  ""-0*--->-----+----O*--- Feedback0
 *	    *      |       *              *         |    *
 *      *      | +-----P-"Line1"  ""-1*--->--+--|----O*--- Feedback1
 *      *      | |.    *              *     .|  |    *     ...             
 *      *      | |.    ****************     .|  |    *
 *      *      | |.                         .|  |    *
 *      *      | |...                 ...    |  |    *
 *      *      | +---------------------------+  |    * 
 *      *      |                                |    *
 *      *      +--------------------------------+    *
 *      *                                            *
 *      *                                            *
 *      *                                            *
 *      **********************************************
 */
class Feedback extends MultiOutputComponent implements Se
{
	double[] feedbackBuffer;
	MOCompound compound;
	int n;

	Feedback(/*Source s,*/ MOCompound mc, int feedbackLines)
	{
		super();
		/*feedbackLines*/
		//n = mc.getAllProbes().length - 1;
		n = feedbackLines;
		feedbackBuffer = new double[n];
		outputNames = new String[n];
		//int n = feedbackBuffer.length;
		for(int i=0;i<n;i++)
		{
			feedbackBuffer[i] = 0;
			outputNames[i] = "Feedback"+i;
		}
		probe = new Probe[1];
		probe[0] = new Probe("Input");
		
		compound = mc;

		link();
	};
	public Source copy(){/*return new Feedback(compound,n);*/return null;}

	private void link()
	{
		//int n = feedbackBuffer.length;
		for(int i=0;i<n;i++)
		{
			//mc.probe[1] = 
			compound.probe("Line"+i).link(outputHandle("Feedback"+i));
			System.out.println("Probe "+compound+" . "+compound.probe("Line"+i).name+" linked to "+ "Feedback"+i);
		}
		compound.probe("Input").link(probe[0]);	//is it OK?
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
				feedbackBuffer[i] = compound.output(i);
		//else
		return compound.output();
	}
	void setOutputNames()
	{
		//outputNames = new String[0];
	}
}
/**
 * 1-NO output NAME for compound is checked. it is arbitrary. it is used by index. (output(i))
 * 2-CHECKINGS FOR REP. NAMES. also for MO/Compound
 * 3-generally, accurate error checking.
 */