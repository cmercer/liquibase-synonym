package liquibase.change.ext.synonym;

import static java.util.Collections.unmodifiableSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MaxDBDatabase;
import liquibase.database.core.OracleDatabase;


public final class Constants {

    /**
     * Set of all the {@link Database} that support synonyms
     */
    public static final Set<Class<? extends Database>> SUPPORTS_SYNONYMS;

    /**
     * Set of all the {@link Database} that support public synonyms
     */
    public static final Set<Class<? extends Database>> SUPPORTS_PUBLIC;

    /**
     * Set of all the {@link Database} that support create or replace synonym syntax
     */
    public static final Set<Class<? extends Database>> SUPPORTS_REPLACE;

    static {
        Set<Class<? extends Database>> supportsSynonyms = new HashSet<Class<? extends Database>>();
        supportsSynonyms.add(OracleDatabase.class);
        supportsSynonyms.add(InformixDatabase.class);
        supportsSynonyms.add(MaxDBDatabase.class);
        supportsSynonyms.add(MSSQLDatabase.class);
        supportsSynonyms.add(DB2Database.class);
        supportsSynonyms.add(DerbyDatabase.class);
        SUPPORTS_SYNONYMS = unmodifiableSet(supportsSynonyms);

        Set<Class<? extends Database>> supportsPublic = new HashSet<Class<? extends Database>>();
        supportsPublic.add(OracleDatabase.class);
        supportsPublic.add(InformixDatabase.class);
        supportsPublic.add(MaxDBDatabase.class);
        SUPPORTS_PUBLIC = unmodifiableSet(supportsPublic);

        Set<Class<? extends Database>> supportsReplace = new HashSet<Class<? extends Database>>();
        supportsReplace.add(OracleDatabase.class);
        SUPPORTS_REPLACE = unmodifiableSet(supportsReplace);
    }
}
