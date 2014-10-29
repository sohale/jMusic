package jMusic.components;

//import java.util.Random;

//is each class: (what to do with Time)
//-jumpable
//-few skips
//-needs reset
//show these by interfaces. easy

//interface Instant,RandomAccess,Functional,		//jumpable
//interface Sequencial,Serial,					//few skips. dt may be unequal
//interface SideEffect,Manual,StartStop				//needs reset. needs serialisation.
												//dt may be unequal
					//interface Phased. (a subtype of Sequencial. for generators)




/*class document template:

 * desc:				* a Simple Delay. Very static? because it has MAXDELAY. it is St but Delay is RA?
 * desc:				* what is the difference?
 * probes:				* (Input,[Time=],Delay)
 * constr:(optional)	* constructor(maxDelay)
 * conditions			* Delay <= const maxDelay
 * descr. for probes:	* or parameters or formulae

*/


///final class Oscilator extends Component








/*
A COMPONENT DEFINITION TEMPLATE
--------------------------------
class XXXX extends Component	{
	private final int Inp=0;
	*...
	private int probes = 4;
	XXXX()	{
		super();
		probe = new Probe[probes]; 

		probe[Inp] = new Probe("Input");	//define probes
		*...

		//probe("Time").link( new Timer(22050) );
		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );


	}
	public double output()	{
		double v = probe[Inp].output();
		return v;
	}
}
*/


