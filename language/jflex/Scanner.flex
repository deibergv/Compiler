
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
  private Symbol newSym(int tokenId) {
   return new Symbol(tokenId, yyline, yycolumn);
  }

  /* Generamos un Symbol para el tipo de token encontrado
     junto con su valor */
  private Symbol newSym(int tokenId, Object value) {
   return new Symbol(tokenId, yyline, yycolumn, value);
  }
%}

/*
    - Macro declaraciones
    Declaramos expresiones regulares que despues seran usadas en las
    reglas lexicas.
*/
Letter          = [A-Za-z]
Digit           = [0-9]
AlphaNumeric    = {Letter}|{Digit}
Other_id_char   = [_]|[@]|[*]
Number          = ({Digit})+
Identifier      = {Letter}({AlphaNumeric}|{Other_id_char})*
LineTerminator  = \n|\r|\r\n
InputCharacter  = [^\r\n]
Comment         = "//" {InputCharacter}* {LineTerminator}?
ESPACIO         = " "

%%
/* LEXICAL RULES: */
/* Comments */
{Comment}       { /* ignore */ }
{ESPACIO}       { /* ignore */ }
{LineTerminator} { /* ignore */ }
"Var"           { return newSym(sym.VAR); }
"Set"           { return newSym(sym.SET); }
"Add"           { return newSym(sym.ADD); }
"Less"          { return newSym(sym.LESS); }
"ChangeDir"     { return newSym(sym.CHANGEDIR); }
"Place"         { return newSym(sym.PLACE); }
"High"          { return newSym(sym.HIGH); }
"Put"           { return newSym(sym.PUT); }
"Pos"           { return newSym(sym.POS); }
"Keep"          { return newSym(sym.KEEP); }
"Skip"          { return newSym(sym.SKIP); }
"Kend"          { return newSym(sym.KEND); }
"For"           { return newSym(sym.FOR); }
"Id"            { return newSym(sym.ID); }
"Times"         { return newSym(sym.TIMES); }
"Fend"          { return newSym(sym.FEND); }
"When"          { return newSym(sym.WHEN); }
"Then"          { return newSym(sym.THEN); }
"Whend"         { return newSym(sym.WHEND); }
"PosStart"      { return newSym(sym.POSSTART); }
"Call"          { return newSym(sym.CALL); }
/* Separators */
"("             { return newSym(sym.LPAREN); }
")"             { return newSym(sym.RPAREN); }
";"             { return newSym(sym.PUNTOCOMA); }
/* Special Symbols */
"="             { return newSym(sym.ASSIGN); }
"+"             { return newSym(sym.AGGREGATE); }
"-"             { return newSym(sym.REDUCED); }
/* Identifiers */
{Identifier}    { return newSym(sym.IDENTIFIER, yytext()); }
{Number}        { return newSym(sym.NUMBER, new Integer(yytext())); }

/* Si el token contenido en la entrada no coincide con ninguna regla
    entonces se marca un token ilegal */
[^]     { throw new Error("Ilegal character <"+yytext()+"> "+
         "[Ln "+(yyline+1)+", "+"Col "+(yycolumn+1)+"]"); }
