# Especificación de la Gramática - ELI NoSQL

## 1. Analizador Léxico (Tokens)
El lenguaje es insensible a mayúsculas y minúsculas (Case-insensitive).

- **Palabras Reservadas:** CREATE, DROP, TABLE, DATABASE, READ, ADD, UPDATE, DELETE, WHERE, STORE, AT, TO, EXPORT, AS, JSON, HTML, ASC, DESC, FIND.
- **Tipos de Datos:** STRING, INTEGER, FLOAT, BOOLEAN, ARRAY, OBJECT.
- **Operadores Relacionales:** ==, !=, >=, <=, >, <.
- **Operadores Lógicos:** AND, OR, NOT.
- **Signos:** {, }, [, ], (, ), ",", :, "->".
- **Comentarios:** `##` para una sola línea y `/* ... */` para múltiples líneas.

## 2. Analizador Sintáctico (BNF)
A continuación se presenta la gramática principal en formato Backus-Naur Form:

<inicio> ::= <lista_instrucciones>

<lista_instrucciones> ::= <lista_instrucciones> <instruccion> 
                        | <instruccion>

<instruccion> ::= <crear_db> 
                | <eliminar_db> 
                | <crear_tabla> 
                | <operaciones_crud> 
                | <persistencia>

<crear_tabla> ::= "CREATE" "TABLE" ID "{" <lista_campos> "}"

<lista_campos> ::= <campo> "," <lista_campos> 
                 | <campo>

<campo> ::= ID ":" <tipo_dato>

<consulta_read> ::= "READ" ID "FIND" <condiciones> "ORDER" "BY" ID <orden>

<orden> ::= "ASC" | "DESC"