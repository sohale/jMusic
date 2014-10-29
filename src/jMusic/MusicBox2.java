package jMusic;
import jMusic.components.Resettable;

/*
	. => *
*/
/**
 * constructor(song,startTime,loop, veriables[,aliases])
 *
 * language legend:
 * 
 *		+		note++
 *		-		note--
 * 
 *		.		turnon,progress,start
 *		'		progress continue or lenthen current note
 *		,		turnoff,progress
 * 
 *		2357	T=T/3 | 5 | 7 | 2
 ***	I		T=1/T
 *		"D"		"I2I"		(T=T*2) 
 *		"1"		T=1
 * 
 ***	":"		v=1
 ***	!		v*=1.4
 ***	?		v/=1.4
 *
 ***	p
 ***	{p
 ***	}p
 ***	=p
 ***	>		fade (not implemented)
 * 
 ***	#123
 ***	#123.456
 ****   _			p=-p
 * 
 *		[		"{n{T{v"
 *		]		"v}T}n}"
 *
 * ;:_@|\ /
 * 
 * reset() is done only at start, restart() is done in loops
 * ([Time=])
 * 
 * outputs={"NoteTime", "isOn", "n" "v" "T" "p" "pqrstuvwxyzabcd" ...}
 */
class MusicBox2 extends MultiOutputComponent implements Resettable
{
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
		{ "[", "{n{T{v" },
		{ "]", "}v}T}n" },
		{ "D", "I2I" },
		//{ "1", "#1T" },		changes 0.1		to  0.#1T
		{ ":", "#1v" }
	};
	String[][] aliases2;

	String predefs = "vnTpl ";
	int
		_v=0,	//velocity
		_n=1,	//note
		_T=2,	//T
		_p=3,	//param
		_l=4,	//last note=previousNote  the note at "Time" = this.t. = the last returned in getNote()
		_pe=5;	//param.exp
		//_s=6;	//start time=prev time?

	String melody,variables;
	boolean loop;
	String vars_4c;
	double start;


	Stack stack;
	double[] param;	//values
	int index;
	double t;
	boolean isoff;
	double previousTime;
	char state;

	//double previousNote;	//used only in following function? or as a permanent return of it.

	MusicBox2(String song, double startTime, boolean loop, String vars)
	{
		this(song,startTime,loop,vars,new String[0][]);
	}
	MusicBox2(String song, double startTime, boolean loop, String vars, String[][] aliases2)
	{
		super();
		probe = new Probe[1]; 
		probe[0] = new Probe("Time");
		probe("Time").linkD( new Timer() );

		//melody = song;
		melody = alias(song,aliases2);
		melody = alias(melody,aliases);
		System.out.println(melody);
		this.loop = loop;
		vars_4c = vars;
		variables = predefs+vars;
		for(int i=0;i<variables.length();i++)
			if(
			" abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.indexOf(variables.charAt(i))<0) throw new Error();
		param = new double[variables.length()];
		//this.aliases2 = aliases2;
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
		t = start;
		previousTime=-10000000;

		index=0;
		state = ' ';
		restart();

		//initStack
		param[_v] = 1.0;
		param[_n] = 0;
		param[_T] = 1.0;		//60 beats per minute
		param[_p] = 0.0;
		param[_pe] = 1.0;
		param[_l] = param[_n];
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
		//t = start;
		//previousTime=-10000000;
		state = ' ';
	}
	
	double getNote(double time)
	{
		if(t<previousTime)
		{
			System.out.println("\nFLASH BACK\n");
			restart();
		}
		//:bug wait

		if(t>=time)
			return param[_l];	//same previousNote

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
					restart();
				else
				{
					isoff=true;		//stop at end
					break;
				}
		}
		return param[_l];
	}
/*
	boolean alias(char code,String[][] aliases)
	{
		//if(aliases==null) return false;
		for(int i=0;i<aliases.length;i++)
		{
			if(aliases[i].length !=2 ) throw new Error();
			if(aliases[i][0].length() !=1 ) throw new Error();
			if(aliases[i][0].equals(code+""))
			{
				for(int j=0;j<aliases[i][1].length();j++)
					doCode(aliases[i][1].charAt(j));
				return true;
			}
		}
		return false;
	}
			//if(alias(code,aliases2)) return;
			//if(alias(code,aliases)) return;

*/
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

	void doCode(char code)
	{
		//if(echo)
		System.out.print(code);


		//then perform standard
		if(state==' ')
		{
			/*
			//first check aliases
			for(int i=0;i<aliases.length;i++)
			{
				if(aliases[i].length !=2 ) throw new Error();
				if(aliases[i][0].length() !=1 ) throw new Error();
				if(aliases[i][0].equals(code+""))
				{
					for(int j=0;j<aliases[i][1].length();j++)
						doCode(aliases[i][1].charAt(j));
					return;
				}
			}
			*/
			//if(alias(code,aliases2)) return;
			//if(alias(code,aliases)) return;


			switch(code)
			{
				/* n */
				case '+':
					param[_n] += 1;
					return;
				case '-':
					param[_n] -= 1;
					return;

				/* T */
				//case '1':param[_T] = 1;break;
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
				/*case ':':
					param[_v] = 1;
					break;*/

				/* p */
				case '#':
					//System.out.println("processed");
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

				case '.':
					param[_l] = param[_n];
					previousTime = t;
					t = t + param[_T];
					isoff = false;
					System.out.println();
					return;
				case '\'':
					t = t + param[_T];
					return;
				case ',':
					t = t + param[_T];
					isoff = true;
					System.out.println();
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
				//System.out.println("\nHAHA="+state+"->"+code);
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
		String temp[] = new String[variables.length()+2];
		int i;
		for(i=0;i<variables.length();i++)
			temp[i] = variables.charAt(i)+"";
		temp[i++] = "NoteTime";
		temp[i++] = "isOn";
		outputNames = temp;
	}
	public double output(int port)
	{
		if(port<variables.length())
			return param[port];
		else if(port==variables.length())
		{
			double time = probe[0].output();
			getNote(time);
			return time - previousTime;
		}
		else if(port==variables.length()+1)
			return isoff ? 0.0 : 1.0 ;
		else
			throw new Error("SHAME");
	}
}
