package jMusic;

/**
 * A Source is anything that can produce a signal.
 * a Component is a Source, but a MultiOutputComponent HAS many Sources.
 * a Timer is a Source. ...
 * later, realtime input devices and motion-capture and ... will be sources? or components. yes. or probes.
 * it is very abstract. it is actually an interrface. it is one of the the back-bones of the design of this thing.
 * @see Probe
 */
abstract interface Source extends Copyable //extends Cloneable
{
    abstract double output();
	//abstract String toString();
    // /*abstract*/ static SemanticInfo semanticInfo;
    // /*abstract*/ SyntaxicInfo syntaxicInfo;
}

interface Copyable
{
	/**
	 * call this after init. [and before getting first sample?no] it may "reset" all components
	 * or this method creats a virgin infant baby Component or Source
	 * with no Attatchment = link
	 * but DEFAULT links are made. 
	 * "IT IS just like a constructor"
	 *			constructor(...)
	 *			copy()
	 *			output()
	 * it does not copy default links but it may.
	 * XXX_4c   constructor param. info for constructor-call in "copy"
	 * note: any constructed object must contain re-construction parameters. (if not, xx_4c field is added)
	 */
	public Source copy();
}
/**
 * about copying aggregations:
 * 1-prevent recopies
 * 2-copy probe connections seperately?
 * 
 */