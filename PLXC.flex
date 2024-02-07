import java_cup.runtime.*;

%%

%int
%cup

%%

"if"      { return new Symbol(sym.IF);   }
"else"    { return new Symbol(sym.ELSE); }
"do"      { return new Symbol(sym.DO);   }
"while"   { return new Symbol(sym.WHILE);}
"for"     { return new Symbol(sym.FOR);  }
"print"   { return new Symbol(sym.PRINT);}
"(" { return new Symbol(sym.AP);    }
")" { return new Symbol(sym.CP);    }
"[" { return new Symbol(sym.AC);    }
"]" { return new Symbol(sym.CC);    }
"{" { return new Symbol(sym.ALL);   }
"}" { return new Symbol(sym.CLL);   }
"+" { return new Symbol(sym.MAS);   }
"-" { return new Symbol(sym.MENOS); }
"*" { return new Symbol(sym.POR);   }
"/" { return new Symbol(sym.DIV);   }
"=" { return new Symbol(sym.ASIG); }
";" { return new Symbol(sym.PYC);   }
"," { return new Symbol(sym.COMA);   }
"int"   { return new Symbol(sym.INT); }
"float"   { return new Symbol(sym.FLOAT); }
"char"   { return new Symbol(sym.CHAR); }
"string"   { return new Symbol(sym.STRING); }
"boolean"   { return new Symbol(sym.BOOLEAN); }
"(int)" { return new Symbol(sym.CINT); }
"(float)" { return new Symbol(sym.CFLOAT); }
"(char)" { return new Symbol(sym.CCHAR); }
"=="    { return new Symbol(sym.IGUAL); }
"!="    { return new Symbol(sym.DISTINTO); }
"<"    { return new Symbol(sym.MENOR); }
">"    { return new Symbol(sym.MAYOR); }
"<="    { return new Symbol(sym.MENOROIGUAL); }
">="    { return new Symbol(sym.MAYOROIGUAL); }
"&&"    { return new Symbol(sym.AND); }
"||"    { return new Symbol(sym.OR); }
"!"    { return new Symbol(sym.NEG); }
"." { return new Symbol(sym.PUNTO); }
"length" { return new Symbol(sym.LENGTH); }
"true"   { return new Symbol(sym.TRUE); }
"false"   { return new Symbol(sym.FALSE); }
"-->"   { return new Symbol(sym.IMPLICA); }


0|[1-9][0-9]*                               { return new Symbol(sym.ENTERO, yytext());  }
[a-zA-Z_][a-zA-Z0-9_]*                      { return new Symbol(sym.IDENT, yytext()); }
[0-9]+\.[0-9]+([eE][\+\-]?[0-9]+)?          { return new Symbol(sym.REAL, yytext()); }
'(\\([\"'\\bfnrt]|u[0-9A-Fa-f]{4})|[^\\'])' { return new Symbol(sym.CHARTEXT, yytext());   }
\"([a-zA-Z]|[\\\"]*|u[0-9A-Fa-f]{4})+\"     { return new Symbol(sym.STRINGTEXT, yytext());   }

(\/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+\/)|(\/\/.*) {}
[\s] {}
[^] { throw new Error("ilegal char: "+yytext());}

