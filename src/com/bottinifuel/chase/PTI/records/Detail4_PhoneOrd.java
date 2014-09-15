/*
 * Created on Apr 7, 2007 by Administrator
 *
 */
package com.bottinifuel.chase.PTI.records;

import java.text.ParseException;

import com.bottinifuel.chase.PTI.FileFormatException;
import com.ribomation.fixedwidthfield.Formatter;

/**
 * @author Administrator
 *
 */
public class Detail4_PhoneOrd extends Detail4
{
    private static final String FormatStr =
        "{trim:width=9} "       +  // Order Num
        "{text:width=1,pad=?} " +  // Mail order indicator
        "{fill:width=228} ";       // Filler 

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String OrderNum;
    public final char   MailOrderIndic;

    /**
     * @param lineNum
     * @param data
     * @throws ParseException
     * @throws FileFormatException
     */
    public Detail4_PhoneOrd(Detail4 d4)
        throws ParseException, FileFormatException
    {
        super(d4);
        if (d4.Type != DetailType.MAILPHONE)
            throw new FileFormatException("Not D4 MAILPHONE subtype");

        Object [] objs = fmt.parse(d4.Data);
        OrderNum       = (String)objs[0];
        MailOrderIndic = ((String)objs[1]).charAt(0);
    }
}
