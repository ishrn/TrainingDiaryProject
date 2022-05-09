package com.training.diary.utilities;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

public class StringUtils {
    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String getClobContent(Clob clob) throws IOException, SQLException {
        if (clob == null) {
            return null;
        }

        Reader r = clob.getCharacterStream();
        StringBuffer buffer = new StringBuffer();
        int ch;
        while ((ch = r.read()) != -1) {
            buffer.append("" + (char) ch);
        }
        return buffer.toString();
    }
}
