package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class MySQL
{
    private String       hostname   = "";
    private String       portnmbr   = "";
    private String       username   = "";
    private String       password   = "";
    private String       database   = "";
    protected Connection connection = null;

    public MySQL(final String hostname, final String portnmbr, final String database, final String username, final String password)
    {
        super();
        this.hostname = hostname;
        this.portnmbr = portnmbr;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * checks if the connection is still active
     *
     * @return true if still active
     * @throws SQLException
     */
    public boolean checkConnection() throws SQLException
    {
        if (!this.connection.isClosed() && this.connection.isValid(5))
        {
            return true;
        }
        return false;
    }

    /**
     * Empties a table
     *
     * @param table
     *            the table to empty
     * @return true if data-removal was successful.
     *
     */
    public boolean clearTable(final String table)
    {
        Statement statement = null;
        String query = null;
        try
        {
            statement = this.connection.createStatement();
            query = "DELETE FROM " + table;
            statement.executeUpdate(query);
            return true;
        } catch (final SQLException e)
        {
            return false;
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
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Delete a table
     *
     * @param table
     *            the table to delete
     * @return true if deletion was successful.
     */
    public boolean deleteTable(final String table)
    {
        Statement statement = null;
        try
        {
            statement = this.connection.createStatement();
            statement.executeUpdate("DROP TABLE " + table);
            return true;
        } catch (final SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * returns the active connection
     *
     * @return Connection
     *
     */

    public Connection getConnection()
    {
        return this.connection;
    }

    /**
     * Insert data into a table
     *
     * @param table
     *            the table to insert data
     * @param data
     *            a Map<String, Object> of the data to insert
     *
     * @return the insert id if insertion was successful, else -1.
     */
    public int insert(final String table, final Map<String, Object> data, final boolean unique)
    {
        Statement statement = null;
        final StringBuilder sb = new StringBuilder("INSERT ");
        if (unique)
        {
            sb.append("IGNORE ");
        }
        sb.append("INTO ");
        sb.append(table);
        sb.append(" (");
        for (final String s : data.keySet())
        {
            sb.append(s);
            sb.append(", ");
        }
        sb.reverse().deleteCharAt(0).deleteCharAt(0).reverse();
        sb.append(") VALUES (");
        for (final String s : data.keySet())
        {
            if (data.get(s) instanceof Boolean)
            {
                sb.append((Boolean) data.get(s) ? 1 : 0);
            } else if (data.get(s) instanceof String)
            {
                sb.append("'");
                sb.append(data.get(s));
                sb.append("'");
            } else
            {
                sb.append(data.get(s));
            }
            sb.append(", ");
        }
        sb.reverse().deleteCharAt(0).deleteCharAt(0).reverse();
        sb.append(");");
        try
        {
            final String query = sb.toString();
            statement = this.connection.createStatement();
            statement.closeOnCompletion();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet keys = statement.getGeneratedKeys())
            {
                if (keys.first())
                {
                    final int keyz = keys.getInt(1);
                    keys.close();
                    return keyz;
                }
            }
            return -1;
        } catch (final Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Insert data into a table
     *
     * @param table
     *            the table to insert data
     * @param column
     *            a String[] of the columns to insert to
     * @param value
     *            a String[] of the values to insert into the column (value[0] goes in column[0])
     *
     * @return the insert id if insertion was successful, else -1.
     */
    @Deprecated
    public int insert(final String table, final String[] column, final String[] value)
    {
        if (column.length != value.length)
        {
            throw new IllegalArgumentException("Column and Value length do not match");
        }
        Statement statement = null;
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        for (final String s : column)
        {
            sb1.append(s + ",");
        }
        for (final String s : value)
        {
            if ((s != null) && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")))
            {
                sb2.append((s != null ? "'" : "") + (Boolean.valueOf(s) ? "1" : "0") + (s != null ? "'," : ","));
            } else
            {
                sb2.append((s != null ? "'" : "") + s + (s != null ? "'," : ","));
            }
        }
        final String columns = sb1.toString().substring(0, sb1.toString().length() - 1);
        final String values = sb2.toString().substring(0, sb2.toString().length() - 1);
        try
        {
            statement = this.connection.createStatement();
            statement.closeOnCompletion();
            final String query = "INSERT INTO " + table + "(" + columns + ") VALUES (" + values + ")";
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet keys = statement.getGeneratedKeys())
            {
                if (keys.first())
                {
                    final int keyz = keys.getInt(1);
                    keys.close();
                    return keyz;
                }
            }
            return -1;
        } catch (final Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * open database connection
     *
     */
    public Connection open()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            final String url = "jdbc:mysql://" + this.hostname + ":" + this.portnmbr + "/" + this.database + "?useUnicode=true&characterEncoding=UTF-8&useServerPrepStmts=false&rewriteBatchedStatements=true";
            this.connection = DriverManager.getConnection(url, this.username, this.password);
            return this.connection;
        } catch (final SQLException e)
        {
            System.out.print("Could not connect to MySQL server! ");
            System.out.println(e.getMessage());
        } catch (final ClassNotFoundException e)
        {
            System.out.println("JDBC Driver not found!");
        }
        return null;
    }

    /**
     * Query the database
     *
     * @param query
     *            the database query
     * @return ResultSet of the query
     *
     * @throws SQLException
     */
    public ResultSet query(final String query) throws SQLException
    {
        Statement statement = null;
        ResultSet result = null;
        try
        {
            statement = this.connection.createStatement();
            result = statement.executeQuery(query);
            return result;
        } catch (final Exception e)
        {
            if (e.getMessage().equals("Can not issue data manipulation statements with executeQuery()."))
            {
                try
                {
                    statement.executeUpdate(query);
                } catch (final SQLException ex)
                {
                    if (e.getMessage().startsWith("You have an error in your SQL syntax;"))
                    {
                        String temp = (e.getMessage().split(";")[0].substring(0, 36) + e.getMessage().split(";")[1].substring(91));
                        temp = temp.substring(0, temp.lastIndexOf("'"));
                        throw new SQLException(temp);
                    } else
                    {
                        ex.printStackTrace();
                    }
                }
            } else if (e.getMessage().startsWith("You have an error in your SQL syntax;"))
            {
                String temp = (e.getMessage().split(";")[0].substring(0, 36) + e.getMessage().split(";")[1].substring(91));
                temp = temp.substring(0, temp.lastIndexOf("'"));
                throw new SQLException(temp);
            } else
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
