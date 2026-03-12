public class Token {
    private String lexema, tipo;
    private int linea, columna;
    public Token(String lexema, String tipo, int linea, int columna) {
        this.lexema = lexema; this.tipo = tipo;
        this.linea = linea; this.columna = columna;
    }
    public String getLexema() { return lexema; }
    public String getTipo() { return tipo; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
}