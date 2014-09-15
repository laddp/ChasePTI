package com.ribomation.fixedwidthfield.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * Formats and parses a date/time value.
 * The width is implicit set to <code>format( new Date() ).length()</code>.
 *
 * <table border=1 cellspacing=0>
 *  <tr bgcolor=silver> <th colspan=3>Properties</th> </tr>
 *  <tr bgcolor=silver> <th>Name</th> <th>Mandatory</th> <th>Description</th> </tr>
 *  <tr> <th>pattern</th> <td>no</td> <td>A java.text.SimpleDateFormat pattern. Default is 'yyyyMMdd'</td> </tr>
 * </table>
 *
 * @author @AUTHOR@
 * @version @VERSION@
 * @created Jens, 2004-mar-30 11:56:49
 */
public class DateFWF extends AbstractFWF {
    private SimpleDateFormat    fmt = new SimpleDateFormat("yyyyMMdd");

     public DateFWF() {
        super("date");
    }

     public int getWidth() {
        return format( new Date() ).length();
    }

     public void     setPattern(String dateTimePattern) {
        fmt = new SimpleDateFormat(dateTimePattern);
    }

    /***
     * Formats a java.util.Date value.
     * @param value     a date
     * @return formatted date
     */
    public String format(Object value) {
        if (value instanceof Date) {
           return fmt.format((Date) value);
        }
        throw new IllegalArgumentException("Expected java.util.Date, got "+value.getClass().getName());
    }

    /***
     * Parses an input text into a java.util.Date value.
     * @param formattedValue    input data
     * @return a date
     * @throws ParseException   if the string couldn't be parsed
     */
     public Object parse(CharSequence formattedValue) throws ParseException {
        return fmt.parse(formattedValue.toString());
     }
}
