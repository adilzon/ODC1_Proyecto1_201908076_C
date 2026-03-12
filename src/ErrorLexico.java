public class ErrorLexico {
    private String descripcion;
    private int linea, columna;
    public ErrorLexico(String desc, int linea, int columna) {
        this.descripcion = desc;
        this.linea = linea; this.columna = columna;
    }
    public String getDescripcion() { return descripcion; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
}