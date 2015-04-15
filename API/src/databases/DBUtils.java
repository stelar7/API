package databases;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils
{
    public static String parseResultSetToJSON(final ResultSet rs) throws SQLException
    {
        final StringBuilder sb = new StringBuilder();

        sb.append("{");
        while (rs.next())
        {
            sb.append("\"").append(rs.getRow()).append("\":{");

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
            {
                sb.append("\"").append(rs.getMetaData().getColumnName(i)).append("\":");
                sb.append("\"").append(rs.getString(i)).append("\",");
            }

            sb.reverse().deleteCharAt(0).reverse();

            sb.append("},");
        }
        sb.reverse().deleteCharAt(0).reverse();

        sb.append("}");

        return sb.toString();
    }
}
