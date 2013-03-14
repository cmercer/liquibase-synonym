package liquibase.change.ext.synonym.structure;

import liquibase.database.Database;
import liquibase.database.structure.DatabaseObject;
import liquibase.database.structure.Schema;

/**
 *
 */
public class Synonym implements DatabaseObject, Comparable<Synonym>{

    private final String name;
    private final Database database;
    private final Schema schema;

    public Synonym(Database database, Schema schema, String name) {
        this.database = database;
        this.schema = schema;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Database getDatabase() {
        return database;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        if(schema == null ) {
            return new DatabaseObject[]{
                    database
            };
        } else {
            return new DatabaseObject[]{schema, database};
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Synonym synonym = (Synonym) o;

        return name.equals(synonym.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Synonym o) {
        return this.name.compareTo(o.name);
    }
}
