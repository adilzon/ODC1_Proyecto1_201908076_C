import java.util.*;

public class DatabaseManager {
    private Map<String, Table> tables = new HashMap<>();
    private String dbName = "";

    public void createDatabase(String name) {
        dbName = name;
        tables.clear();
    }

    public void createTable(String tableName, Map<String, String> schema) {
        tables.put(tableName, new Table(schema));
    }

    public void addRecord(String tableName, Map<String, Object> record) {
        Table t = tables.get(tableName);
        if (t != null) t.addRecord(record);
    }

    public List<Map<String, Object>> read(String tableName, String filterField, Object filterValue) {
        Table t = tables.get(tableName);
        if (t == null) return new ArrayList<>();
        return t.read(filterField, filterValue);
    }

    public Map<String, Table> getTables() { return tables; }
}

class Table {
    private Map<String, String> schema;
    private List<Map<String, Object>> records = new ArrayList<>();

    public Table(Map<String, String> schema) {
        this.schema = schema;
    }

    public void addRecord(Map<String, Object> record) {
        records.add(record);
    }

    public List<Map<String, Object>> read(String field, Object value) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> r : records) {
            Object val = r.get(field);
            if (val != null && val.toString().equals(value.toString())) {
                result.add(r);
            }
        }
        return result;
    }
}