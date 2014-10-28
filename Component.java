package jMusic;
/**
 * The Origin of all Components
 * the Component is implemented here, but browsing and public "reflection" methods are moved to real Component.
 * see Probe class for complete information about Components and probes and links.
 * @see Probe
 * @see Component
 */
abstract class Component0   implements Source    //a set of Probes that has an output
{
   //private 
   //Probe probe[];                 //Input probe[]
   //Source probe[];
   protected //??
   Probe probe[];


   public 
   Probe probe(String name)         //this set must have an item locator
   {
      for(int i=0;i<probe.length;i++)
      {
         //System.out.println(probe[i].getClass().getSuperclass().getName());
         if(probe[i].name.equals(name))
            return probe[i];     //returns the first
      }
      throw new Error("There is no Probe named \""+name+"\" .");
      //return null;
   }

   //public abstract
   //public double output();  //should be cached!
   
   //protected double sampleRate()...	it may be a function of time? yes.
   
}

/**
 * "reflection" methods for Component can be found here
 * see Component0
 */
public abstract class Component extends Component0		//this generation contains Browse/View methods
{
	public Probe[] getAllProbes()
	{
		return probe;   //dang.: no security, only for Browser
	}
	//very popular, public and cheap!
	public void lnk(String name, Source s)
	{
		probe(name).link(s);
	}
	public void lnkD(String name, Source s)
	{
		lnk(name,s);
		probe(name).isDefault();
	}
	public void lnk(String name, Source s, double w)
	{
		probe(name).link(w,s);
	}
/*	static void copy(Component c)
	{
		Source d = c.copy();
	}
*/
	public boolean existsProbe(String name)
	{
		for(int i=0;i<probe.length;i++)
			if(probe[i].name.equals(name))
				return true;
		return false;
	}
	java.util.Vector properties = new java.util.Vector(0);
	public boolean property(Object prop)
	{
		for(int i=0;i<properties.size();i++)
			if(properties.elementAt(i).equals(prop))
				return true;	//or: return element(i)
		return false;
	}
	public void is(Object prop)
	{
		if(property(prop)) throw new Error("propery already ... "+prop);
		properties.addElement(prop);
	}

	public Component put(String name, Component s)
	{
		lnk(name,s);
		return s;
	}
	/**
	 * 3 Shahrivar 1379, after a very long time
	 */
	//public String[] probeNames()
	public String listOfNames()
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<probe.length;i++)
		{
			sb.append(probe[i].name);
			sb.append(" ");
		}
		return sb.toString();
	}
}
