package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidSeq;

@Deprecated
public interface DynArray extends Object, DynAny {
  Any[] get_elements();
  
  void set_elements(Any[] paramArrayOfAny) throws InvalidSeq;
}


/* Location:              D:\software\jd-gui\jd-gui-windows-1.6.3\rt.jar!\org\omg\CORBA\DynArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */