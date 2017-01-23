package databases;

import java.sql.*;

public class MySQL
{
    private static final int TIMEOUT = 5;
    protected Connection connection;
    private   String     hostname;
    private   String     portnmbr;
    private   String     username;
    private   String     password;
    private   String     database;
    
    public MySQL(final String hostname, final String portnmbr, final String database, final String username, final String password)
    {
        super();
        this.hostname = hostname;
        this.portnmbr = portnmbr;
        this.database = database;
        this.username = username;
        this.password = password;
        open();
    }
    
    /**
     * checks if the connection is still active
     *
     * @return true if still active
     * @throws SQLException ignored
     */
    public boolean checkConnection() throws SQLException
    {
        return !this.connection.isClosed() && this.connection.isValid(TIMEOUT);
    }
    
    /**
     * Empties a table
     *
     * @param table the table to empty
     */
    public void clearTable(final String table)
    {
        try
        {
            try (PreparedStatement statement = this.connection.prepareStatement("DELETE FROM ?"))
            {
                statement.setString(1, table);
                statement.executeUpdate();
                statement.close();
            }
        } catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * close database connection
     */
    public void close()
    {
        try
        {
            if (this.connection != null)
            {
                this.connection.close();
            }
        } catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Delete a table
     *
     * @param table the table to delete
     */
    public void deleteTable(final String table)
    {
        try (PreparedStatement statement = this.connection.prepareStatement("DROP TABLE ?"))
        {
            statement.setString(1, table);
            statement.executeUpdate();
            statement.close();
        } catch (final SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * returns the active connection
     *
     * @return Connection
     */
    
    public Connection getConnection()
    {
        return this.connection;
    }
    
    /**
     * open database connection
     */
    public final void open()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            final StringBuilder sb = new StringBuilder();
            sb.append("jdbc:mysql://");
            sb.append(this.hostname);
            sb.append(":");
            sb.append(this.portnmbr);
            sb.append("/");
            sb.append(this.database);
            sb.append("?useUnicode=true");
            sb.append("&characterEncoding=UTF-8");
            sb.append("&useServerPrepStmts=false");
            sb.append("&rewriteBatchedStatements=true");
            sb.append("&serverTimezone=UTC");
            final String url = sb.toString();
            this.connection = DriverManager.getConnection(url, this.username, this.password);
        } catch (final SQLException e)
        {
            System.out.print("Could not connect to MySQL server! ");
            System.out.println(e.getMessage());
        } catch (final ClassNotFoundException e)
        {
            System.out.println("JDBC Driver not found!");
        }
    }
}
