package jMusic;
import jMusic.components.Resettable;

/*
	. => *
*/
/**
 * constructor(song,startTime,loop, veriables[,aliases])
 * inputs=([Time=])
 * outputs={"NoteTime", "isOn", "Duration", "l"n"N"v"T"p" "qrstuvwxyzabcd" ...}
 * .
 * N = next note
 * n = this note
 * l = last note
 * T = time
 * v = velocity
 * 
 * change:
 *		"''."		means a longer note not ".''"
 * 
 * important: in coding, use N for changing notes. because current note is processed after ".,'" s
 *		and actually we want to change next note.
 *		in MusicBox2, "n" was actually next note
 *		[] macros are changes here.
 * 
 * useless:	"p" " "
 * changes: n->N l->n L->l
 * other not-output variables:
 *		Lt lt nt
 * Yellow Note book, "EID79" 40barg
 * a Hbug fixed: getNote() must be caled for any extra-outputs
 * 
 * history: (if I chose another way I cartainly confuse)
 *		I first started with defining a previouspreviousTime and p.p.Note (names Lt,Ln)
 *		then renamed vars and rewrote getNote() then studied it.
 *		I found in MusicBox2 and 1 n was actually next note. double mistake EQ true
 * bug: ".'"  ' is NextNote
 * 
 * changing note during rest is applied,
 * changing note during sustain is not applied, because note is not played
 * 
 * 
 * use   ",,,'''."	for lengthy resty notes
 */
class MusicBox3 extends MultiOutputComponent implements Resettable
{
	final boolean silent = true;

	class Stack
	{
		int stacklength;
		double[] stackArray;
		Stack(int len)
		{
			stackArray = new double[len];
			stacklength = 0;
		};
		void push(double data)
		{
			if(stacklength+1 > stackArray.length)
				throw new Error("stack full. DIE!");
			stackArray[stacklength++] = data;
		}
		double pop()
		{
			if(stacklength<=0)
				throw new Error("UNDERFLOW");
			--stacklength;
			return stackArray[stacklength];
		}
	}

	String[][] aliases = {
		{ "[", "{N{T{v" },
		{ "]", "}v}T}N" },
		{ "D", "I2I" },
		{ ":", "#1v" }
	};
	String[][] aliases2;

	String predefs = "vNTpn l";	//was "vnTpl L";
	int
		_v=0,	//  velocity
		_N=1,	//N note (next note)
		_T=2,	//T
		_p=3,	//  param
		_l=4,	//n last note=previousNote  the note at "Time" = this.t. = the last returned in getNote()
		_pe=5,	//  param.exp
		//_s=;	//    start time=prev time?
		_L=6;	//l note before last

	String melody,variables;
	boolean loop;
	String vars_4c;
	double start;


	Stack stack;
	double[] param;
	int index;
	boolean isoff;
	char state;

	double nt,		//t		public, used by getNote() and "Duration". is copy of nt0. some thimes like "'" it is not copied. delayed value
		nt0		//t		real one, real ver. hidden value (internal use)
		//lastNoteEnd;
		,dur;
	char lc;
	double lt;		//previousTime
	double Lt;

	MusicBox3(String song, double startTime, boolean loop, String vars)
	{
		this(song,startTime,loop,vars,new String[0][]);
	}
	MusicBox3(String song, double startTime, boolean loop, String vars, String[][] aliases2)
	{
		super();
		probe = new Probe[1]; 
		probe[0] = new Probe("Time");
		probe("Time").linkD( new Timer() );

		melody = alias(song,aliases2);
		melody = alias(melody,aliases);
		if(!silent) System.out.println(melody);
		this.loop = loop;
		vars_4c = vars;
		variables = predefs+vars;
		for(int i=0;i<variables.length();i++)
			if(
			" abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.indexOf(variables.charAt(i))<0) throw new Error();
		param = new double[variables.length()];
		start = startTime;
		setOutputNames0();

		reset();
	}
	public Source copy()
	{
		return new MusicBox2(melody,start,loop,vars_4c);
	}
	public void reset()
	{
		stack = new Stack(200);
		for(int i=0;i<param.length;i++)
			param[i] = 0;
		nt0 = start;nt=nt0;dur=0;//lastNoteEnd=nt;
		lt = -10000000;
		Lt = lt;
		lc=' ';

		index=0;
		state = ' ';
		restart();

		param[_v] = 1.0;
		param[_N] = 0;
		param[_T] = 1.0;		//60 beats per minute
		param[_p] = 0.0;
		param[_pe] = 1.0;
		param[_l] = param[_N];
		param[_L] = param[_l];
		//getNote(startTime0);
	}

	public double output()
	{
		double time = probe[0].output();
		return getNote(time);
	}
	void restart()	//and continue in loop
	{
		isoff = true;
		index=0;
		state = ' ';
	}

	//returns if no more left
	boolean progress()
	{
		if(index<melody.length())
		{
			doCode(melody.charAt(index));
			index++;
		}
		else
			if(loop)
				restart();
			else
			{
				isoff=true;		//stop at end
				//break;
				return true;
			}
		return false;
	}
	void getBack()
	{
		System.out.println("\nFLASH BACK\n");
		//throw new Error("FLASH BACK");
		restart();
	}
	
	/**
	 *		Lt		lt		nt			|	time
	 *	--------------------------------+-----------------------
	 *	============					|	getBack();
	 *				*=======			|	OK
	 *						*======		|	progress()
	 *									|
	 */
	String last = "";
	double getNote(double time)
	{
		/*
		String o = "(Lt lt nt) = ("+Lt+" "+lt+" "+nt+") \n(L  l  n ) = ("+param[_L]+" "+param[_l]+" "+param[_n]+") ";
		if(!o.equals(last))
			System.out.println(o+" time="+time+"\n");
		last=o;
		*/

		//it is OK in first sample ?
		//Lt <= lt <= nt
		while(time < lt)
			getBack();

		while(time >= nt )
		{
			if(progress())
				//break;
				return param[_l];	//return last...? //no difference!
				//maybe: [_n] means that last note of "+.++" if 3.0 but it is 1.0
			//System.out.print(">");
		}

		if(nt > time && time >= lt)
			return param[_l];

		throw new Error("error ("+Lt+" "+lt+" "+nt+")   "+time);
	}


	void doCode(char code)
	{
		//if(echo)
		if(!silent) System.out.print(code);


		//then perform standard
		if(state==' ')
		{
			switch(code)
			{
				/* n */
				case '+':
					param[_N] += 1;
					return;
				case '-':
					param[_N] -= 1;
					return;

				/* T */
				case '2':
				case '3':
				case '5':
				case '7':
					param[_T] = param[_T] / (int)(code-'0');
					return;
				case 'I':
					param[_T] = 1.0/param[_T];
					return;

				/* v */
				case '!':
					param[_v] *= 1.4;
					return;
				case '?':
					param[_v] /= 1.4;
					break;

				/* p */
				case '#':
					state='#';
					param[_p] = 0;
					param[_pe] = 1;
					return;
				case '_':
					param[_p] = -param[_p];
					return;
				case '=':
					state='=';
					return;
				case '{':
					state='{';
					return;
				case '}':
					state='}';
					return;
				/*case '>':
					state='>';
					break;*/

			// ,
				case ',':
					nt0 = nt0 + param[_T];

					//lt = nt;
					//nt=nt0;
					//strt=nt;

					//lastNoteEnd=nt0;	//was ntbr=NewTimeForRest
					//nt=nothing
					//nt is not changed, no new note is here, but we need the time for duration
					
					//nt = nt0;
					//lastNoteEnd=lt;
					

					//if changepitch during rest (must, because nt must change)
					//param[_L] = param[_l];
					//param[_l] = param[_N];

					lc=',';
					isoff = true;
					if(!silent) System.out.println(); else System.out.print(",");
					return;
			// .
				case '.':

					param[_L] = param[_l];
					param[_l] = param[_N];

					//lt = nt0;

					nt0 = nt0 + param[_T];

					lt = nt;
					nt=nt0;
					if(lc!='\'')dur=0;
					dur = dur + param[_T];
					//strt=nt;
					//lastNoteEnd=nt;

					lc='.';
					isoff = false;
					if(!silent) System.out.println(); else System.out.print(".");
					return;
			// '
				case '\'':
					//if changepitch during sustain
					//has no effection
					//param[_L] = param[_l];
					//param[_l] = param[_N];

					nt0 = nt0 + param[_T];
					//if(lc=='.')dur=0;
					if(lc!='\'')dur=0;
					dur = dur + param[_T];

					//should we?
					//lt = nt;
					//
					//dont nt=nt0;
					//if this doesnt run, we need to put "lt=nt" instead of "lt=nt0" in case'.'
					//	then logically, nt0=nt.
					//	why?

					lc='\'';
					return;
				case ' ':
					return;

				default:
					param[varIndex(code)] = param[_p];
					return;
			}
		}
		else if(state=='#')
		{
			if(code<='9' && code>='0')
			{
				int dig = (int)(code-'0');
				param[_p] = param[_p]*10 + dig;
				param[_pe] = 1;
			}
			else if(code=='.')
				state='.';
			else
			{
				state=' ';
				doCode(code);
			}
			return;
		}
		else if(state=='.')
		{
			if(code<='9' && code>='0')
			{
				int dig = (int)(code-'0');
				param[_pe] = param[_pe]/10.0;
				param[_p] = param[_p] + dig*param[_pe];
			}
			else
			{
				state = ' ';
				doCode(code);
			}
			return;
		}
		else if(state=='=')
		{
			param[_p] = param[varIndex(code)];
			state=' ';
			return;
		}
		else if(state=='{')
		{
			stack.push(param[varIndex(code)]);
			state=' ';
			return;
		}
		else if(state=='}')
		{
			param[varIndex(code)] = stack.pop();
			state=' ';
			return;
		}
		else
			throw new Error();
		return;
	}
	int varIndex(char p)
	{
		int i = variables.indexOf(p);
		if(i<=-1) throw new Error();
		return i;
	}
	void setOutputNames()
	{}	
	void setOutputNames0()
	{
		String temp[] = new String[variables.length()  +  3   ];
		int i;
		for(i=0;i<variables.length();i++)
			temp[i] = variables.charAt(i)+"";
		temp[i++] = "NoteTime";
		temp[i++] = "isOn";
		temp[i++] = "Duration";
		outputNames = temp;
	}
	public double output(int port)
	{
		//a dismissed Hbug
		double time = probe[0].output();
		getNote(time);

		if(port<variables.length())
			return param[port];
		int p = port-variables.length();
		switch(p)
		{
			case 0:
				//getNote(time); a hidden bug
				return time - lt;
			case 1:
				return isoff ? 0.0 : 1.0 ;
			case 2:
				//return lastNoteEnd-lt; //ntbr-lt;	//nt-lt;		//not nt0-lt
				//return nt-strt;
				return dur;
		}
		throw new Error("no such output index");
	}

	String alias(String code,String[][] aliases)
	{
		StringBuffer result = new StringBuffer(code.length()*2);
		for(int c=0;c<code.length();c++)
		{
			String C = code.charAt(c)+"";
			boolean found=false;
			for(int i=0;i<aliases.length;i++)
			{
				if(aliases[i].length !=2 ) throw new Error();
				if(aliases[i][0].length() !=1 ) throw new Error();
				if(aliases[i][0].equals(C))
				{
					//return aliases[i][1];
					result.append(aliases[i][1]);
					found=true;
					break;
				}
			}
			if(!found)
				result.append(C);
		}
		return result.toString();
	}
}
