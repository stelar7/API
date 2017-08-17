package databases;

import org.tmatesoft.sqljet.core.*;
import org.tmatesoft.sqljet.core.table.*;

import java.io.File;
import java.util.Map;

public class SQLite
{
    
    
    private final File     dbFile;
    private       SqlJetDb db;
    
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
    
    public long insert(final String tableName, final Map<String, Object> data)
    {
        try
        {
            this.db.beginTransaction(SqlJetTransactionMode.WRITE);
            final ISqlJetTable table = this.db.getTable(tableName);
            final long         rowid = table.insertByFieldNames(data);
            this.db.commit();
            return rowid;
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
            return -1;
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
    
    public ISqlJetCursor select(final String tableName, final String indexKey, final Object... value)
    {
        try
        {
            this.db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            final ISqlJetTable  table  = this.db.getTable(tableName);
            final ISqlJetCursor cursor = table.lookup(indexKey, value);
            this.db.commit();
            return cursor;
        } catch (final SqlJetException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
