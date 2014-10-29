package jMusic.components;
import jMusic.Source;


/**
 * this class implements a Constant value. for editors, hidden constants, parameters, zeros, test values, ...
 */
public final class Constant implements Source,RA //,BrowserInformation
{
   double foreverValue;
   public Constant(double forever)
   {
      foreverValue = forever;
   }
   public Source copy()
   {
	   return new Constant(foreverValue);
   }
   public double output()
   {
      return foreverValue;
   }
	public String toString()
	{
		//return "Constant("+(int)(foreverValue*10000)/10000.0+")";
		return ""+(int)(foreverValue*10000)/10000.0+" const";
	};
}

