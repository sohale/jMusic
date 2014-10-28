package jMusic;

/**
 * Probe, for connecting components and naming link ports.
 * it is a runtime Solution, it is very loose, slow, soft but reconfigurable and flexible and extendable? and it uses Object-Pool paradigm.
 * worst in speed and best in reliability and reuse and other SE stuf?
 * describtion: (Very Important)
 *	every Component has some probes named this.probe[probe-number] internally and
 *     named <component-name>.probe( <probe-string-name> ) from outside.
 *  U can ask a component for its probe names using "getAllProbes()".
 *  @see Component
 *  Component (has-many) Probe (has-a) "Source source" , which is linked to another source/component using Java solution.
 * usage:
 *		Component cname = new XX..Comp(...);
 *		cname.probe("probe-name-1").link( source-for-probe-1 );
 *		cname.probe("probe-name-2").link( source-for-probe-2 );
 *		cname.probe("probe-name-3").link( source-for-probe-3 );
 * probe _naming_ and _indexing_ is done in each Component's constructor. (each probe has a private index for internal use, because we have a private probe[] field)
 * some probes may be linked by default (often like "Time") but the link can be changed.
 * the links are browse/trace-able bottom-up (and top-down) by Component class.
 */

class Probe implements Source	//(constant) Weighted version
{
	String name;
	Source source=null;
	//boolean optional;
	//boolean default;
	boolean defaultSource = true;
	double weight;

	Probe(String name)
	{
		this.name = name;
	}
	void link(double w ,Source source)
    {
		if(this.source != null && !defaultSource) System.out.println("WARNING: re LINK ing probe "+name);
        this.source = source;
		weight = w;
		defaultSource = false;
    }
	void link(Source source)
    {
		link(1,source);
    }
	//only for use in components initializers
	void isDefault()
    {
		defaultSource = true;
    }
	void linkD(Source source)
    {
		link(1,source);
		isDefault();
    }
	//for debug only
	String spy=null;
	void spy(String spy)
	{
		this.spy=spy;
	}
	public double output()
	{
		if(source==null)
			System.out.println("unbound \""+name+"\"");
		//if(NaN) error
		//if(source==null) throw new Error("Probe not attatched=linked ("+name+")");
		double o = source.output();
		if(spy!=null) System.out.println(spy+o);
		return weight*o;
	}
	public Source copy()
	{
		return null;
		//Source scp = source.copy();...
	}
}

//not used
//dont use it
//never use it
class Probe00 implements Source //!! :x  8x
{

	/*private*/ String name;
	/*private*/ Source source=null;
	Probe00(String name)
	{
		this.name = name;
	}
	final public Source copy(){return null;}
	void link(Source source)
    {
        //if(this.source != null)
            //System.out.println("Warning: changing previous connection");
        this.source = source;
    }
	public double output()
	//{return value();}
	//	private final double value()	//many has been changed. why?
	{
		if(source==null)
		{
			throw new Error(name+" Must be linked.");
			//return 0.0;
		}
		else
			return source.output();
   }

/*
   String getName() //for Browsing (8.9.77)
   {
        return new String(name);
   }
   Source getSource() //(or 'GetLink') for Browsing (8.9.77)
   {
        return source;
   }
*/
};
