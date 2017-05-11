package liquibase.ext.ora.generate;

import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.structure.DatabaseObject;

/**
 * テスト用に強引に書き換え
 */
public class TestDatabaseSnapshot extends DatabaseSnapshot {
//    private DatabaseObject[] examples;
    private Database database;
    public TestDatabaseSnapshot(DatabaseObject[] examples, Database database) throws DatabaseException, InvalidExampleException {
        //ここでexamplesをnullにしておかないとDB接続処理が発生する。
        super(null, database);
//        this.examples = examples;
        this.database = database;
    }
    public Database getDatabase() {
        return this.database;
    }
}
