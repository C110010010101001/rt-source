package org.xml.sax.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamespaceSupport {
  public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
  
  public static final String NSDECL = "http://www.w3.org/xmlns/2000/";
  
  private static final Enumeration EMPTY_ENUMERATION = Collections.enumeration(new ArrayList());
  
  private Context[] contexts;
  
  private Context currentContext;
  
  private int contextPos;
  
  private boolean namespaceDeclUris;
  
  public NamespaceSupport() { reset(); }
  
  public void reset() {
    this.contexts = new Context[32];
    this.namespaceDeclUris = false;
    this.contextPos = 0;
    this.contexts[this.contextPos] = this.currentContext = new Context();
    this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
  }
  
  public void pushContext() {
    int i = this.contexts.length;
    this.contextPos++;
    if (this.contextPos >= i) {
      Context[] arrayOfContext = new Context[i * 2];
      System.arraycopy(this.contexts, 0, arrayOfContext, 0, i);
      i *= 2;
      this.contexts = arrayOfContext;
    } 
    this.currentContext = this.contexts[this.contextPos];
    if (this.currentContext == null)
      this.contexts[this.contextPos] = this.currentContext = new Context(); 
    if (this.contextPos > 0)
      this.currentContext.setParent(this.contexts[this.contextPos - 1]); 
  }
  
  public void popContext() {
    this.contexts[this.contextPos].clear();
    this.contextPos--;
    if (this.contextPos < 0)
      throw new EmptyStackException(); 
    this.currentContext = this.contexts[this.contextPos];
  }
  
  public boolean declarePrefix(String paramString1, String paramString2) {
    if (paramString1.equals("xml") || paramString1.equals("xmlns"))
      return false; 
    this.currentContext.declarePrefix(paramString1, paramString2);
    return true;
  }
  
  public String[] processName(String paramString, String[] paramArrayOfString, boolean paramBoolean) {
    String[] arrayOfString = this.currentContext.processName(paramString, paramBoolean);
    if (arrayOfString == null)
      return null; 
    paramArrayOfString[0] = arrayOfString[0];
    paramArrayOfString[1] = arrayOfString[1];
    paramArrayOfString[2] = arrayOfString[2];
    return paramArrayOfString;
  }
  
  public String getURI(String paramString) { return this.currentContext.getURI(paramString); }
  
  public Enumeration getPrefixes() { return this.currentContext.getPrefixes(); }
  
  public String getPrefix(String paramString) { return this.currentContext.getPrefix(paramString); }
  
  public Enumeration getPrefixes(String paramString) {
    ArrayList arrayList = new ArrayList();
    Enumeration enumeration = getPrefixes();
    while (enumeration.hasMoreElements()) {
      String str = (String)enumeration.nextElement();
      if (paramString.equals(getURI(str)))
        arrayList.add(str); 
    } 
    return Collections.enumeration(arrayList);
  }
  
  public Enumeration getDeclaredPrefixes() { return this.currentContext.getDeclaredPrefixes(); }
  
  public void setNamespaceDeclUris(boolean paramBoolean) {
    if (this.contextPos != 0)
      throw new IllegalStateException(); 
    if (paramBoolean == this.namespaceDeclUris)
      return; 
    this.namespaceDeclUris = paramBoolean;
    if (paramBoolean) {
      this.currentContext.declarePrefix("xmlns", "http://www.w3.org/xmlns/2000/");
    } else {
      this.contexts[this.contextPos] = this.currentContext = new Context();
      this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    } 
  }
  
  public boolean isNamespaceDeclUris() { return this.namespaceDeclUris; }
  
  final class Context {
    Map<String, String> prefixTable;
    
    Map<String, String> uriTable;
    
    Map<String, String[]> elementNameTable;
    
    Map<String, String[]> attributeNameTable;
    
    String defaultNS = null;
    
    private List<String> declarations = null;
    
    private boolean declSeen = false;
    
    private Context parent = null;
    
    Context() { copyTables(); }
    
    void setParent(Context param1Context) {
      this.parent = param1Context;
      this.declarations = null;
      this.prefixTable = param1Context.prefixTable;
      this.uriTable = param1Context.uriTable;
      this.elementNameTable = param1Context.elementNameTable;
      this.attributeNameTable = param1Context.attributeNameTable;
      this.defaultNS = param1Context.defaultNS;
      this.declSeen = false;
    }
    
    void clear() {
      this.parent = null;
      this.prefixTable = null;
      this.uriTable = null;
      this.elementNameTable = null;
      this.attributeNameTable = null;
      this.defaultNS = null;
    }
    
    void declarePrefix(String param1String1, String param1String2) {
      if (!this.declSeen)
        copyTables(); 
      if (this.declarations == null)
        this.declarations = new ArrayList(); 
      param1String1 = param1String1.intern();
      param1String2 = param1String2.intern();
      if ("".equals(param1String1)) {
        if ("".equals(param1String2)) {
          this.defaultNS = null;
        } else {
          this.defaultNS = param1String2;
        } 
      } else {
        this.prefixTable.put(param1String1, param1String2);
        this.uriTable.put(param1String2, param1String1);
      } 
      this.declarations.add(param1String1);
    }
    
    String[] processName(String param1String, boolean param1Boolean) {
      Map map;
      if (param1Boolean) {
        map = this.attributeNameTable;
      } else {
        map = this.elementNameTable;
      } 
      String[] arrayOfString = (String[])map.get(param1String);
      if (arrayOfString != null)
        return arrayOfString; 
      arrayOfString = new String[3];
      arrayOfString[2] = param1String.intern();
      int i = param1String.indexOf(':');
      if (i == -1) {
        if (param1Boolean) {
          if (param1String == "xmlns" && NamespaceSupport.this.namespaceDeclUris) {
            arrayOfString[0] = "http://www.w3.org/xmlns/2000/";
          } else {
            arrayOfString[0] = "";
          } 
        } else if (this.defaultNS == null) {
          arrayOfString[0] = "";
        } else {
          arrayOfString[0] = this.defaultNS;
        } 
        arrayOfString[1] = arrayOfString[2];
      } else {
        String str3;
        String str1 = param1String.substring(0, i);
        String str2 = param1String.substring(i + 1);
        if ("".equals(str1)) {
          str3 = this.defaultNS;
        } else {
          str3 = (String)this.prefixTable.get(str1);
        } 
        if (str3 == null || (!param1Boolean && "xmlns".equals(str1)))
          return null; 
        arrayOfString[0] = str3;
        arrayOfString[1] = str2.intern();
      } 
      map.put(arrayOfString[2], arrayOfString);
      return arrayOfString;
    }
    
    String getURI(String param1String) { return "".equals(param1String) ? this.defaultNS : ((this.prefixTable == null) ? null : (String)this.prefixTable.get(param1String)); }
    
    String getPrefix(String param1String) { return (this.uriTable == null) ? null : (String)this.uriTable.get(param1String); }
    
    Enumeration getDeclaredPrefixes() { return (this.declarations == null) ? EMPTY_ENUMERATION : Collections.enumeration(this.declarations); }
    
    Enumeration getPrefixes() { return (this.prefixTable == null) ? EMPTY_ENUMERATION : Collections.enumeration(this.prefixTable.keySet()); }
    
    private void copyTables() {
      if (this.prefixTable != null) {
        this.prefixTable = new HashMap(this.prefixTable);
      } else {
        this.prefixTable = new HashMap();
      } 
      if (this.uriTable != null) {
        this.uriTable = new HashMap(this.uriTable);
      } else {
        this.uriTable = new HashMap();
      } 
      this.elementNameTable = new HashMap();
      this.attributeNameTable = new HashMap();
      this.declSeen = true;
    }
  }
}


/* Location:              D:\software\jd-gui\jd-gui-windows-1.6.3\rt.jar!\org\xml\sax\helpers\NamespaceSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */