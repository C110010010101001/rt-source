package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.portable.IDLEntity;

public final class NameValuePair implements IDLEntity {
  public String id = null;
  
  public Any value = null;
  
  public NameValuePair() {}
  
  public NameValuePair(String paramString, Any paramAny) {
    this.id = paramString;
    this.value = paramAny;
  }
}


/* Location:              D:\software\jd-gui\jd-gui-windows-1.6.3\rt.jar!\org\omg\DynamicAny\NameValuePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */