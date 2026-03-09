public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando programa...");  // Deberías ver esto en la terminal
        javax.swing.SwingUtilities.invokeLater(() -> {
            System.out.println("Creando ventana...");  // Esto también
            Ventana ventana = new Ventana();
            ventana.setVisible(true);
            System.out.println("Ventana creada y visible.");
        });
        System.out.println("Main terminado.");  // Esto al final
    }
}