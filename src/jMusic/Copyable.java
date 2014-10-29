package jMusic;
/**
 * @see Source
 */
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