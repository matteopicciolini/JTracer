package org.mirrors.compiler;

public class SourceLocation extends Token {
    public String filename;
    public int linenum;
    public int colnum;

    public SourceLocation(){
         filename="";
         linenum=0;
         colnum=0;
    }
}
