import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Ventana extends JFrame {

    private JTextArea editorArea;
    private JTextArea consolaArea;
    private JTextArea outputArea;

    private File archivoActual = null;

    public Ventana() {

        setTitle("ELI NoSQL - Editor & Interprete");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // ---------------- PANEL SUPERIOR ----------------

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        panelSuperior.setBorder(new EmptyBorder(10,10,10,10));

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnAbrir = new JButton("Abrir");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEjecutar = new JButton("Ejecutar");

        btnNuevo.setPreferredSize(new Dimension(100,35));
        btnAbrir.setPreferredSize(new Dimension(100,35));
        btnGuardar.setPreferredSize(new Dimension(100,35));
        btnEjecutar.setPreferredSize(new Dimension(120,35));

        btnEjecutar.setBackground(new Color(0,150,0));
        btnEjecutar.setForeground(Color.WHITE);
        btnEjecutar.setFont(new Font("Arial",Font.BOLD,14));

        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnAbrir);
        panelSuperior.add(btnGuardar);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(btnEjecutar);

        add(panelSuperior,BorderLayout.NORTH);

        // ---------------- SPLIT PRINCIPAL ----------------

        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPrincipal.setResizeWeight(0.65);

        editorArea = new JTextArea();
        editorArea.setFont(new Font("Consolas",Font.PLAIN,14));
        editorArea.setTabSize(4);
        editorArea.setLineWrap(false);

        JScrollPane scrollEditor = new JScrollPane(editorArea);
        scrollEditor.setBorder(BorderFactory.createTitledBorder("Editor de Codigo ELI"));

        splitPrincipal.setLeftComponent(scrollEditor);

        JPanel panelDerecho = new JPanel(new GridLayout(2,1,0,10));
        panelDerecho.setBorder(new EmptyBorder(5,5,5,5));

        consolaArea = new JTextArea();
        consolaArea.setEditable(false);
        consolaArea.setBackground(new Color(30,30,30));
        consolaArea.setForeground(new Color(200,255,200));
        consolaArea.setFont(new Font("Consolas",Font.PLAIN,13));

        JScrollPane scrollConsola = new JScrollPane(consolaArea);
        scrollConsola.setBorder(BorderFactory.createTitledBorder("Consola / Mensajes"));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(20,20,40));
        outputArea.setForeground(new Color(220,220,255));
        outputArea.setFont(new Font("Consolas",Font.PLAIN,13));

        JScrollPane scrollOutput = new JScrollPane(outputArea);
        scrollOutput.setBorder(BorderFactory.createTitledBorder("Output / Resultados de consultas"));

        panelDerecho.add(scrollConsola);
        panelDerecho.add(scrollOutput);

        splitPrincipal.setRightComponent(panelDerecho);

        add(splitPrincipal,BorderLayout.CENTER);

        // ---------------- ACCIONES BOTONES ----------------

        btnNuevo.addActionListener(e -> nuevoArchivo());
        btnAbrir.addActionListener(e -> abrirArchivo());
        btnGuardar.addActionListener(e -> guardarArchivo());
        btnEjecutar.addActionListener(e -> ejecutarCodigo());

        consolaArea.append(">> ELI NoSQL - Listo para usar\n");
    }

    // ---------------- ARCHIVOS ----------------

    private void nuevoArchivo() {
        editorArea.setText("");
        archivoActual = null;
        setTitle("ELI NoSQL - Editor & Interprete *nuevo*");
    }

    private void abrirArchivo() {

        JFileChooser chooser = new JFileChooser();

        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){

            archivoActual = chooser.getSelectedFile();

            try(BufferedReader br = new BufferedReader(new FileReader(archivoActual))){

                editorArea.read(br,null);
                setTitle("ELI NoSQL - " + archivoActual.getName());

            }catch(IOException ex){

                JOptionPane.showMessageDialog(this,"Error al abrir");

            }
        }
    }

    private void guardarArchivo(){

        if(archivoActual == null){

            JFileChooser chooser = new JFileChooser();

            if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){

                archivoActual = chooser.getSelectedFile();

            }else{
                return;
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(archivoActual))){

            editorArea.write(bw);

        }catch(IOException ex){

            JOptionPane.showMessageDialog(this,"Error al guardar");

        }
    }

    // ---------------- EJECUCIÓN ----------------

    private void ejecutarCodigo() {
        String codigo = editorArea.getText().trim();

        if (codigo.isEmpty()) {
            consolaArea.append(">> No hay código para ejecutar.\n");
            return;
        }

        consolaArea.append("\n>> Iniciando análisis léxico con JFlex...\n");

        try {
            Lexer lexer = new Lexer(new java.io.StringReader(codigo));
            lexer.yylex();

            mostrarReporteTokens(lexer.tokens);
            mostrarReporteErrores(lexer.errores);

            consolaArea.append(">> Análisis léxico COMPLETO → Tokens: "
                    + lexer.tokens.size()
                    + " | Errores: " + lexer.errores.size() + "\n");

        } catch (Exception e) {
            consolaArea.append(">> Error en lexer: " + e.getMessage() + "\n");
        }
    }

    // ---------------- REPORTES ----------------

    private void mostrarReporteTokens(ArrayList<Token> tokens) {
        String[] columnas = {"Lexema", "Tipo", "Línea", "Columna"};
        Object[][] data = new Object[tokens.size()][4];
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            data[i] = new Object[]{t.getLexema(), t.getTipo(), t.getLinea(), t.getColumna()};
        }
        JTable tabla = new JTable(data, columnas);
        JScrollPane scroll = new JScrollPane(tabla);
        JFrame frame = new JFrame("Reporte de Tokens");
        frame.setSize(700, 500);
        frame.add(scroll);
        frame.setVisible(true);
    }

    private void mostrarReporteErrores(ArrayList<ErrorLexico> errores) {
        if (errores.isEmpty()) {
            consolaArea.append(">> No se encontraron errores léxicos 🎉\n");
            return;
        }
        String[] columnas = {"Descripción", "Línea", "Columna"};
        Object[][] data = new Object[errores.size()][3];
        for (int i = 0; i < errores.size(); i++) {
            ErrorLexico e = errores.get(i);
            data[i] = new Object[]{e.getDescripcion(), e.getLinea(), e.getColumna()};
        }
        JTable tabla = new JTable(data, columnas);
        JScrollPane scroll = new JScrollPane(tabla);
        JFrame frame = new JFrame("Reporte de Errores Léxicos");
        frame.setSize(700, 400);
        frame.add(scroll);
        frame.setVisible(true);
    }
}