
/* --------------------------Codigo de Usuario----------------------- */
package com.language.parser;

import java_cup.runtime.*;
import java.io.Reader;

%% //inicio de opciones
/* ------ Seccion de opciones y declaraciones de JFlex -------------- */

/*
    Activar el contador de lineas, variable yyline
    Activar el contador de columna, variable yycolumn
*/
%line
%column

/*
   Activamos la compatibilidad con Java CUP para analizadores
   sintacticos(parser)
*/
%cup
/*
    Cambiamos el nombre de la clase del analizador a Lexer
*/
%class Scanner

/*
    - Declaraciones
    El codigo entre %{ y %} sera copiado integramente en el
    analizador generado.
*/
%{
  /*  Generamos un java_cup.Symbol para guardar el tipo de token
      encontrado */
  Symbol newSym(int tokenId) {
   return new Symbol(tokenId, yyline, yycolumn);
  }

  /* Generamos un Symbol para el tipo de token encontrado
     junto con su valor */
  Symbol newSym(int tokenId, Object value) {
   return new Symbol(tokenId, yyline, yycolumn, value);
  }

/**
 * assumes correct representation of a long value for
 * specified radix in scanner buffer from <code>start</code>
 * to <code>end</code>
 */
private long parseLong(int start, int end, int radix) {
    long result = 0;
    long digit;

    for (int i = start; i < end; i++) {
      digit  = Character.digit(yycharat(i),radix);
      result*= radix;
      result+= digit;
    }

    return result;
  }
%}


/*
    - Macro declaraciones
    Declaramos expresiones regulares que despues usadas en las
    reglas lexicas.
*/

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} |
          {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/*" "*"+ [^/*] ~"*/"

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral = {DecIntegerLiteral} [lL]

/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%state STRING, CHARLITERAL

%%

<YYINITIAL> {

  /* keywords */
  <YYINITIAL> "Var"           { System.out.print(yytext()); return symbol(sim.Var); }
  <YYINITIAL> "Set"           { System.out.print(yytext()); return symbol(sim.Set); }
  <YYINITIAL> "Add"           { return symbol(sim.Add); }
  <YYINITIAL> "Less"          { return symbol(sim.Less); }
  <YYINITIAL> "ChangeDir"     { return symbol(sim.ChangeDir); }
  <YYINITIAL> "Place"         { return symbol(sim.Place); }
  <YYINITIAL> "High"          { return symbol(sim.High); }
  <YYINITIAL> "Put"           { return symbol(sim.Put); }
  <YYINITIAL> "Pos"           { return symbol(sim.Pos); }
  <YYINITIAL> "Keep"          { return symbol(sim.Keep); }
  <YYINITIAL> "Skip"          { return symbol(sim.Skip); }
  <YYINITIAL> "Kend"          { return symbol(sim.Kend); }
  <YYINITIAL> "For"           { return symbol(sim.For); }
  <YYINITIAL> "Id"            { return symbol(sim.Id); }
  <YYINITIAL> "Times"         { return symbol(sim.Times); }
  <YYINITIAL> "Fend"          { return symbol(sim.Fend); }
  <YYINITIAL> "When"          { return symbol(sim.When); }
  <YYINITIAL> "Then"          { return symbol(sim.Then); }
  <YYINITIAL> "Whend"         { return symbol(sim.Whend); }
  <YYINITIAL> "PosStart"      { return symbol(sim.PosStart); }
  <YYINITIAL> "Call"          { return symbol(sim.Call); }

  /* separators */
  "("                            { return symbol(sim.LPAREN); }
  ")"                            { return symbol(sim.RPAREN); }
  ";"                            { return symbol(sim.SEMICOLON); }
  "{"                            { return symbol(sim.LBRACE); }
  "}"                            { return symbol(sim.RBRACE); }
  "["                            { return symbol(sim.LBRACK); }
  "]"                            { return symbol(sim.RBRACK); }
  ","                            { return symbol(sim.COMMA); }
  "."                            { return symbol(sim.DOT); }

  /* Special Symbols */
  "=" { return symbol(sim.ASSIGN); }
  "*" { return symbol(sim.ASTERISK); }
  "@" { return symbol(sim.AT); }
  "_" { return symbol(sim.UNDERSCORE); }

  /* string literal */
  \"                             { yybegin(STRING); string.setLength(0); }

  /* character literal */
  \'                             { yybegin(CHARLITERAL); }

  /* This is matched together with the minus, because the number is too big to
     be represented by a positive integer. */
  "-2147483648"                  { return symbol(INTEGER_LITERAL, new Integer(Integer.MIN_VALUE)); }

  {DecIntegerLiteral}            { return symbol(INTEGER_LITERAL, new Integer(yytext())); }
  {DecLongLiteral}               { return symbol(INTEGER_LITERAL, new Long(yytext().substring(0,yylength()-1))); }

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */
  {Identifier}                   { return symbol(IDENTIFIER, yytext()); }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return symbol(STRING_LITERAL, string.toString()); }

  {StringCharacter}+             { string.append( yytext() ); }

  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }


  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

<CHARLITERAL> {
  {SingleCharacter}\'            { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, yytext().charAt(0)); }

  /* escape sequences */
  "\\b"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\b');}
  "\\t"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\t');}
  "\\n"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\n');}
  "\\f"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\f');}
  "\\r"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\r');}
  "\\\""\'                       { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\"');}
  "\\'"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\'');}
  "\\\\"\'                       { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, '\\'); }


  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated character literal at end of line"); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
<<EOF>> { return symbol(EOF); }
