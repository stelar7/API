package databases;

import java.io.File;
import java.util.Map;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class SQLite
{

    enum DataType
    {
        TEXT,
        BLOB,
        INTEGER;
    }

    private SqlJetDb   db;
    private final File dbFile;

    public SQLite(final File dataFile)
    {
        this.dbFile = dataFile;
    }

    public void close()
    {
        try
        {
            this.db.close();
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteTable(final String table)
    {
        try
        {
            this.db.beginTransaction(SqlJetTransactionMode.WRITE);
            this.db.dropTable(table);
            this.db.commit();
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
        }
    }

    public ISqlJetCursor select(final String tableName, final String indexKey, final Object... value)
    {
        try
        {
            this.db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetTable table = this.db.getTable(tableName);
            ISqlJetCursor cursor = table.lookup(indexKey, value);
            this.db.commit();
            return cursor;
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public long insert(final String tableName, final Map<String, Object> data)
    {
        try
        {
            this.db.beginTransaction(SqlJetTransactionMode.WRITE);
            ISqlJetTable table = this.db.getTable(tableName);
            long rowid = table.insertByFieldNames(data);
            this.db.commit();
            return rowid;
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public void createTable(final String table, final Map<String, String> fields)
    {

        final StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(table);
        sb.append(" (");
        for (final String s : fields.keySet())
        {
            sb.append(s);
            sb.append(" ");
            sb.append(fields.get(s));
            sb.append(", ");
        }
        sb.reverse().deleteCharAt(0).deleteCharAt(0).reverse();
        sb.append(");");
        try
        {
            this.db.beginTransaction(SqlJetTransactionMode.WRITE);
            this.db.createTable(sb.toString());
            this.db.commit();
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
        }
    }

    public void open()
    {
        try
        {
            this.db = SqlJetDb.open(this.dbFile, true);
            this.db.getOptions().setAutovacuum(true);
            this.db.beginTransaction(SqlJetTransactionMode.WRITE);
            this.db.getOptions().setUserVersion(1);
            this.db.commit();
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
        }
    }
}
