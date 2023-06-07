package org.mirrors.compiler;

public class SourceLocation{
    String fileName;
    int lineNum;
    int colNum;

    public SourceLocation(String fileName, int lineNum, int colNum){
        super();
        this.fileName = fileName;
        this.lineNum = lineNum;
        this.colNum = colNum;
    }
    public SourceLocation(int lineNum, int colNum){
        this("", lineNum, colNum);
    }

    public SourceLocation(){
        this("", 0, 0);
    }

    public SourceLocation copy() {
        return new SourceLocation(this.fileName, this.lineNum, this.colNum);
    }
}
