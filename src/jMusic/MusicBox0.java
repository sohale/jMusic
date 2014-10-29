package jMusic;

//import jMusic.Component;		//not "import Component;"
import jMusic.components.Resettable;

/**
 * used in MusicBox0
 */
	class State
	{
		//all fields: PUBLIC
		//double t;
		public double note;
		public double T;		//1/4*measure time	(black note duration)

		State(State old)
		{
			//t = old.t;
			note = old.note;
			T = old.T;
		}
		State(double nn, double TT)		//All fields
		{
			//t = 0;
			note = 0;
			T = 0;

			note = nn;
			T = TT;
		}
	}

/**
 * used in MusicBox0
 */
	class StateStack
	{
		int stacklength;
		State[] stackArray = new State[50];		// ? are these null at first?
		StateStack()
		{
			stacklength = 0;
		};
		void push(State data)
		{
			if(stacklength+1 > stackArray.length)
			{
				//stackArray = ...;
				//error("stack full. DIE!");
				throw new Error("stack full. DIE!");
				/*
				try enlarging it
				if(null)
					error(notpossible...)
				*/
			}
			stackArray[stacklength++] = new State(data);
		}
		State pop()
		{
			if(stacklength<=0)
				throw new Error("UNDERFLOW");
				//error("UNDERFLOW");
			--stacklength;
			State r = stackArray[stacklength];
			stackArray[stacklength] = null;
			return r;
		}
	}


/**
 * constructor(song,startTime,loop)
 * in MusicBox0
 * every sample must come after the prev. one, it must be reseted to be on a sooner Time.
 * lang. legend:
 *		+		note++
 *		-		note--
 *		.		turnon,progress,start
 *		'		progress?  (may be I meant continue the note)
 *		,		turnoff,progress
 *		/		T=1/T
 *		1		T=1
 *		2357	T=T/3 or 5 or 7 or 2
 *		D		T=T*2		= "/2/"
 *		[]		push,pop (note,T)
 * reset() is OK now.
 * no input.
 * ()
 * constructor(song,startTime,loop)
 * outputs:
 * { "NoteTime", "isOn" }
 *		[0]="NoteTime"=time from last note {started/triggered/pressed/...}
 *		[1]="isOn"=is on. (later=velocity.)
 */
final	//?
class MusicBox0 extends MultiOutputComponent implements Resettable				//jMusic.Component
{
	//double[]	tune	= new double[60*12];
	//int[]		key		= new int[60*7];

	//STACK STATE	*{NOTE STATE}
	StateStack stack;	//ini.t in constructor

	//NOTE STATE
	State current;

	//STROKE state
	double t=0;		//current time
	boolean isoff;

	/*	BAD WRONG
	final void set_t(double newt)	//inline
	{
		previousTime = t;
		t = newt;
	}
	*/

	void doCode(char code)
	{
		System.out.print(code);
		switch(code)
		{
			case '+':
				current.note = current.note + 1;
				break;
			case '-':
				current.note = current.note - 1;
				break;

			case '.':	//this char is for (may be) NEXT note
				previousNote = current.note;	//moved from end of "getNote". this source needs a "Variable/state" revolution. but its soon.	TOOMUCH COMPLEXITY HERE
				/* is it wrong?	NO*/
				previousTime = t;
				t = t + current.T;

				//apply(note,t);	//must be usefull lonely. for future :polypholy		/*case ';':apply(note,t);break;				no polyphony	another...:	t-=T;			*/
				isoff = false;
				break;
			case '\'':
					//never "previousTime = t"
				t = t + current.T;
				//isoff = false;
				//no trig
				break;
			case ',':
				//???? previousTime = t;	no because we want? last note to be continued if necessary?
				t = t + current.T;
				//note = -infinite;
				isoff = true;
				break;

			case '/':
				current.T = 1/current.T;
				break;
			case '1':
				current.T = 1;
				break;
			case '2':
			case '3':
			case '5':
			case '7':
				current.T = current.T / (int)(code-'0');		//education
				break;
			case 'D':
				current.T = current.T * 2.0;	//really hurry! no think at all. DIRTY
				break;
			case '[':
				stack.push(current);
				break;
			case ']':
				current = stack.pop();
				break;
			//else
			default:
				;
		}
	}

	double previousTime;	//thEsE are for outputS?
	//no it must be beside "t"

	double previousNote;	//used only in following function? or as a permanent return of it.
							//wrong!
	int index;
	void initNote()	//init returning note
	{
		previousNote = current.note;
		index = 0;
	}
	double getNote(double time)
	{
		//if(isoff)
		//	...

		//System.out.println("time="+time + "  t="+t);

		if(t>=time)
			return previousNote;	//same previousNote

		//System.out.println("t<time");

		// getNextNote
		while(t<time)
		{
			if(index<melody.length())
			{
				doCode(melody.charAt(index));
				index++;
			}
			else
				if(loop)
				{
					index = 0;
					restart();
				}
				else
				{	//no loop. play once
					isoff=true;
					break;	//break the while
				}
		}

		//THIS LINE WAS ABUG. 9 farvardin 1378	(6:36XM)
		//previousNote = current.note;
		//moved to  doCode(

		return previousNote;
	}


	private final int Inp=0;
	private int probes = 1;

	//Object constant state
	private String melody;
	boolean loop;

	void restart()
	{
		isoff=true;		//here or in constructor?

 		stack = new StateStack();

		current = new State(0,1);	//note 0, 60bpm

		initNote();

		//getNote(startTime);	?
	}
	final public Source copy(){return new MusicBox0(melody,startTime0,loop);}
	MusicBox0(String song, double startTime, boolean loop)		//Constant Start time...? or is it a PROBE?
	{
		super();
		probe = new Probe[probes]; 
		probe[Inp] = new Probe("Time");

		melody = song;	//no new
		this.loop = loop;

		//probe("Time").link( new Timer(22050) );
		//probe("Time").link( new Timer(Timer.sampleFrequency()) );
		probe("Time").link( new Timer() );
		probe("Time").isDefault();


		startTime0=startTime;
		/*
		t = startTime;
		previousTime = -1000000;	//startTime;	//?

		//isoff=true;		//in restart() or here?

		restart();
		getNote(startTime);		//?
		*/
		reset();

	}
	double startTime0;	//for resetting
	public void reset()
	{
		t=startTime0;
		previousTime=-10000000;
		restart();
		getNote(startTime0);		//?
	}
	void setOutputNames()
	{
		String temp[] = { "NoteTime", "isOn" };
		outputNames = temp;
	}
	public double output()
	{
		double time = probe[Inp].output();
		return getNote(time);	//60 beats per minute
	}
	public double output(int port)
	{
		switch(port)
		{
			case 0:
				double time = probe[Inp].output();			//SPEED: f.
				getNote(time);
				//System.out.println(time - previousTime);
				return time - previousTime;

			case 1:
				return isoff ? 0.0 : 1.0 ;

			default:
				throw new Error("SHAME!");
		}
	}
}
/*
final class Rest extends Amplifier
{	//! sarkary!
}
*/
/* Instancing the Components

*************void static::MakeInnstance(Component);*
abstract class InstancableComponent extends Component
{
	abstract public State freeze();
	public abstract void set(State);
}

	-----------------
	serialize
	...
*/
