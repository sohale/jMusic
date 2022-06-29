# jMusic
By Sohail Siadatnejad.

Isfahan, Iran (1999)

jMusic was the first iteration (my first attempt) to create a real-time audio system back in Summer of 1999. jMusic was the precursor to [Mehr32](https://github.com/sohale/mehr32).

It was a later rewritten in C++ as [Mehr32](https://github.com/sohale/mehr32) in 2000.

I intend to improve the code gradually. For the original [summer 1999 version](https://github.com/sohale/jMusic/tree/as-in-1999) click on the [`as-in-1999`](https://github.com/sohale/jMusic/tree/as-in-1999) branch (pushed the 1999 files to github on 2014)


Structure:
<!-- https://docs.google.com/presentation/d/1g1bCBDmHZhPk7cXdbi13iPy7Bf795LBN8tILLNhgtpI/edit?usp=sharing -->
<!-- https://docs.google.com/presentation/d/1g1bCBDmHZhPk7cXdbi13iPy7Bf795LBN8tILLNhgtpI/edit?usp=sharing -->
<!-- https://drive.google.com/file/d/1FVnb_EE_Ed-fHh6bQRfH3-_lgcz18evU/view?usp=sharing -->


![image]( https://drive.google.com/uc?export=view&id=1FVnb_EE_Ed-fHh6bQRfH3-_lgcz18evU   "jMusic classes" )

* A `Source` is whatever that has an `.output()`
* A `Probe` is a Source that can be wired (linked) to another `Source`. It is the wiring (flexible runtime: wiring-time).
* A `Component` is a rewirable Source, that is, a Source with multiple Probes in it.
* `Probe` can be directly cascaded linked in a serial way (eg `MOCompound`).

* wiring-time, constructor-time, generation time, etc.

See Also:
* @sohale/ **[mehr32](https://github.com/sohale/mehr32)**
