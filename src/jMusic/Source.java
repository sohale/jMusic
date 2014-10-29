package jMusic;

/**
 * A Source is anything that can produce a signal.
 * a Component is a Source, but a MultiOutputComponent HAS many Sources.
 * a Timer is a Source. ...
 * later, realtime input devices and motion-capture and ... will be sources? or components. yes. or probes.
 * it is very abstract. it is actually an interrface. it is one of the the back-bones of the design of this thing.
 * @see Probe
 * @see Copyable
 */
public abstract interface Source extends Copyable //extends Cloneable
{
    abstract double output();
	//abstract String toString();
    // /*abstract*/ static SemanticInfo semanticInfo;
    // /*abstract*/ SyntaxicInfo syntaxicInfo;
}
