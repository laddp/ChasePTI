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
public class Detail5_MiscData extends Detail5
{
    private static final String FormatStr =
        "{trim:width=30} "  +  // Cust Data
        "{trim:width=32} "  +  // Ref #
        "{trim:width=99} "  +  // Merch Specific Info
        "{fill:width=78} ";    // Content

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String CustData;
    public final String RefNum;
    public final String MerchInfo;

    /**
     * @param lineNum
     * @param data
     * @throws ParseException
     * @throws FileFormatException
     */
    public Detail5_MiscData(Detail5 d5)
        throws ParseException, FileFormatException
    {
        super(d5);
        if (d5.Type != DetailType.MISC_DATA)
            throw new FileFormatException("Not D5 MISC_DATA subtype");

        Object [] objs = fmt.parse(d5.Data);
        CustData  = (String)objs[0];
        RefNum    = (String)objs[1];
        MerchInfo = (String)objs[2];
    }
}
