package liquibase.change.ext.synonym;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

/**
 *
 */
public class DropSynonymChange extends AbstractChange{

    public DropSynonymChange() {
        super("dropSynonym", "Drop a synonym", ChangeMetaData.PRIORITY_DEFAULT);
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        return new SqlStatement[0];
    }
}
