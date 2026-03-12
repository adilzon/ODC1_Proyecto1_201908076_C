import java.io.*;
import java.util.*;

public class JSONHandler {
    public static void saveDatabase(String filename, DatabaseManager db) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("{\n");
            writer.write("  \"database\": \"" + db.getTables().keySet() + "\",\n");
            writer.write("  \"tables\": {}\n"); // Simplificado por ahora
            writer.write("}");
            System.out.println(">> Guardado en: " + filename);
        } catch (Exception e) {
            System.out.println("Error al guardar JSON");
        }
    }
}