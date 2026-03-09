import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class Ventana extends JFrame {

    private JTextArea editorArea;       // donde escribes el codigo ELI
    private JTextArea consolaArea;      // mensajes, prints, errores generales
    private JTextArea outputArea;       // resultados de consultas (read)

    private File archivoActual = null;  // para guardar / abrir

    public Ventana() {
        // Configuracion basica de la ventana
        setTitle("ELI NoSQL - Editor & Interprete");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null); // centrar en pantalla
        setLayout(new BorderLayout(10, 10));

        // -------------------------------
        // BARRA DE MENU / BOTONES SUPERIORES
        // -------------------------------
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnNuevo   = new JButton("Nuevo");
        JButton btnAbrir   = new JButton("Abrir");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEjecutar= new JButton("Ejecutar");

        btnNuevo.setPreferredSize(new Dimension(100, 35));
        btnAbrir.setPreferredSize(new Dimension(100, 35));
        btnGuardar.setPreferredSize(new Dimension(100, 35));
        btnEjecutar.setPreferredSize(new Dimension(120, 35));

        btnEjecutar.setBackground(new Color(0, 150, 0));
        btnEjecutar.setForeground(Color.WHITE);
        btnEjecutar.setFont(new Font("Arial", Font.BOLD, 14));

        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnAbrir);
        panelSuperior.add(btnGuardar);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(btnEjecutar);

        add(panelSuperior, BorderLayout.NORTH);

        // -------------------------------
        // AREAS PRINCIPALES (split)
        // -------------------------------
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPrincipal.setResizeWeight(0.65); // editor mas grande

        // Editor de codigo
        editorArea = new JTextArea();
        editorArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        editorArea.setTabSize(4);
        editorArea.setLineWrap(false);
        JScrollPane scrollEditor = new JScrollPane(editorArea);
        scrollEditor.setBorder(BorderFactory.createTitledBorder("Editor de Codigo ELI"));

        splitPrincipal.setLeftComponent(scrollEditor);

        // Panel derecho: consola + output
        JPanel panelDerecho = new JPanel(new GridLayout(2, 1, 0, 10));
        panelDerecho.setBorder(new EmptyBorder(5, 5, 5, 5));

        consolaArea = new JTextArea();
        consolaArea.setEditable(false);
        consolaArea.setBackground(new Color(30, 30, 30));
        consolaArea.setForeground(new Color(200, 255, 200));
        consolaArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scrollConsola = new JScrollPane(consolaArea);
        scrollConsola.setBorder(BorderFactory.createTitledBorder("Consola / Mensajes"));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(20, 20, 40));
        outputArea.setForeground(new Color(220, 220, 255));
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scrollOutput = new JScrollPane(outputArea);
        scrollOutput.setBorder(BorderFactory.createTitledBorder("Output / Resultados de consultas"));

        panelDerecho.add(scrollConsola);
        panelDerecho.add(scrollOutput);

        splitPrincipal.setRightComponent(panelDerecho);

        add(splitPrincipal, BorderLayout.CENTER);

        // -------------------------------
        // ACCIONES DE BOTONES
        // -------------------------------
        btnNuevo.addActionListener(e -> nuevoArchivo());
        btnAbrir.addActionListener(e -> abrirArchivo());
        btnGuardar.addActionListener(e -> guardarArchivo());
        btnEjecutar.addActionListener(e -> ejecutarCodigo());

        // Mensaje inicial
        consolaArea.append(">> ELI NoSQL - Listo para usar\n");
        consolaArea.append(">> Escribe codigo ELI y presiona Ejecutar\n\n");
    }

    private void nuevoArchivo() {
        editorArea.setText("");
        archivoActual = null;
        setTitle("ELI NoSQL - Editor & Interprete *nuevo*");
        consolaArea.append(">> Nuevo archivo creado\n");
    }

    private void abrirArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos ELI / Code", "eli", "code", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = chooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivoActual))) {
                editorArea.read(br, null);
                setTitle("ELI NoSQL - " + archivoActual.getName());
                consolaArea.append(">> Archivo abierto: " + archivoActual.getName() + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos ELI", "eli", "code"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoActual = chooser.getSelectedFile();
                if (!archivoActual.getName().endsWith(".eli") && !archivoActual.getName().endsWith(".code")) {
                    archivoActual = new File(archivoActual.getAbsolutePath() + ".eli");
                }
            } else {
                return;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoActual))) {
            editorArea.write(bw);
            setTitle("ELI NoSQL - " + archivoActual.getName());
            consolaArea.append(">> Guardado en: " + archivoActual.getAbsolutePath() + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarCodigo() {
        String codigo = editorArea.getText().trim();
        if (codigo.isEmpty()) {
            consolaArea.append(">> No hay codigo para ejecutar.\n");
            return;
        }

        consolaArea.append("\n>> Ejecutando codigo ELI...\n");
        consolaArea.append("   (Analisis lexico pendiente - Dia 2)\n");
        outputArea.setText(""); // limpiar output anterior

        // Por ahora solo simulamos
        consolaArea.append("\n>> Mensaje de simulacion (Dia 1):\n");
        consolaArea.append("   Codigo recibido (" + codigo.length() + " caracteres)\n");
        consolaArea.append("   Pendiente: lexer, parser, ejecucion real\n\n");

        // Aqui Dia 2-5 conectaremos el lexer y la logica real
        outputArea.append(">> Resultado simulado (proximamente vendran consultas read reales)\n");
        outputArea.append("   Ejemplo futuro: \n   [ {nombre: \"Luis\", edad: 22}, {nombre: \"Ana\", edad: 19} ]\n");
    }
}