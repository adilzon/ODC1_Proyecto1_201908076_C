import java.util.ArrayList;

%%

%class Lexer
%line
%column
%unicode
%public
%standalone

%{
    public ArrayList<Token> tokens = new ArrayList<>();
    public ArrayList<ErrorLexico> errores = new ArrayList<>();

    private void addToken(String lexema, String tipo) {
        tokens.add(new Token(lexema, tipo, yyline+1, yycolumn+1));
    }

    private void addError(String descripcion) {
        errores.add(new ErrorLexico(descripcion, yyline+1, yycolumn+1));
    }
%}

/* Palabras reservadas del PDF */
DATABASE     = database
TABLE        = table
ADD          = add
UPDATE       = update
READ         = read
FILTER       = filter
CLEAR        = clear
EXPORT       = export
STORE        = "store at"
USE          = use

/* Tipos de datos */
INT          = int
FLOAT        = float
BOOL         = bool
STRING       = string
ARRAY        = array
OBJECT       = object
NULL         = null

/* Operadores (orden correcto: largos primero) */
IGUAL        = "=="
DIFERENTE    = "!="
MAYOR_IGUAL  = ">="
MENOR_IGUAL  = "<="
MAYOR        = ">"
MENOR        = "<"

/* Operadores lógicos (símbolos del PDF) */
AND          = "&&"
OR           = "||"
NOT          = "!"

/* Signos de agrupación */
LLAVE_A      = "{"
LLAVE_C      = "}"
PARENT_A     = "("
PARENT_C     = ")"
CORCH_A      = "["
CORCH_C      = "]"

/* Otros */
COMA         = ","
DOSPUNTOS    = ":"
PUNTOCOMA    = ";"

/* Literales */
NUMERO       = [0-9]+("."[0-9]+)?
CADENA       = \"([^\\\"]|\\.)*\"
BOOLEANO     = true|false

/* Identificador */
ID           = [a-zA-Z_][a-zA-Z0-9_]*

/* Comentarios (exacto del PDF) */
COMENTARIO_LINEA = "##"[^\r\n]*
COMENTARIO_BLOQUE= "#*"([^*]|(\*+[^#]))* "*#"

/* Espacios y salto de línea */
ESPACIO      = [ \t\r\n]+

%%

{DATABASE}     { addToken(yytext(), "RESERVADA_DATABASE"); }
{TABLE}        { addToken(yytext(), "RESERVADA_TABLE"); }
{ADD}          { addToken(yytext(), "RESERVADA_ADD"); }
{UPDATE}       { addToken(yytext(), "RESERVADA_UPDATE"); }
{READ}         { addToken(yytext(), "RESERVADA_READ"); }
{FILTER}       { addToken(yytext(), "RESERVADA_FILTER"); }
{CLEAR}        { addToken(yytext(), "RESERVADA_CLEAR"); }
{EXPORT}       { addToken(yytext(), "RESERVADA_EXPORT"); }
{STORE}        { addToken(yytext(), "RESERVADA_STORE"); }
{USE}          { addToken(yytext(), "RESERVADA_USE"); }

{INT}          { addToken(yytext(), "TIPO_INT"); }
{FLOAT}        { addToken(yytext(), "TIPO_FLOAT"); }
{BOOL}         { addToken(yytext(), "TIPO_BOOL"); }
{STRING}       { addToken(yytext(), "TIPO_STRING"); }
{ARRAY}        { addToken(yytext(), "TIPO_ARRAY"); }
{OBJECT}       { addToken(yytext(), "TIPO_OBJECT"); }
{NULL}         { addToken(yytext(), "TIPO_NULL"); }

{IGUAL}        { addToken(yytext(), "OP_IGUAL"); }
{DIFERENTE}    { addToken(yytext(), "OP_DIFERENTE"); }
{MAYOR_IGUAL}  { addToken(yytext(), "OP_MAYOR_IGUAL"); }
{MENOR_IGUAL}  { addToken(yytext(), "OP_MENOR_IGUAL"); }
{MAYOR}        { addToken(yytext(), "OP_MAYOR"); }
{MENOR}        { addToken(yytext(), "OP_MENOR"); }

{AND}          { addToken(yytext(), "OP_AND"); }
{OR}           { addToken(yytext(), "OP_OR"); }
{NOT}          { addToken(yytext(), "OP_NOT"); }

{LLAVE_A}      { addToken(yytext(), "LLAVE_ABRE"); }
{LLAVE_C}      { addToken(yytext(), "LLAVE_CIERRA"); }
{PARENT_A}     { addToken(yytext(), "PARENTESIS_ABRE"); }
{PARENT_C}     { addToken(yytext(), "PARENTESIS_CIERRA"); }
{CORCH_A}      { addToken(yytext(), "CORCHETE_ABRE"); }
{CORCH_C}      { addToken(yytext(), "CORCHETE_CIERRA"); }

{COMA}         { addToken(yytext(), "COMA"); }
{DOSPUNTOS}    { addToken(yytext(), "DOS_PUNTOS"); }
{PUNTOCOMA}    { addToken(yytext(), "PUNTO_COMA"); }

{NUMERO}       { addToken(yytext(), "NUMERO"); }
{CADENA}       { addToken(yytext(), "CADENA"); }
{BOOLEANO}     { addToken(yytext(), "BOOLEANO"); }
{ID}           { addToken(yytext(), "ID"); }

{COMENTARIO_LINEA}   { /* ignorar */ }
{COMENTARIO_BLOQUE}  { /* ignorar */ }
{ESPACIO}      { /* ignorar */ }

.              { addError("Carácter inválido: '" + yytext() + "'"); }