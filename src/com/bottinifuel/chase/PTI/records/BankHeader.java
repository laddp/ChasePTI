/*
 * Created on Apr 5, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI.records;

import java.text.ParseException;
import java.util.Date;

import com.ribomation.fixedwidthfield.Formatter;


/**
 * @author pladd
 *
 */
public class BankHeader extends PTIRecord
{
    /** Settlement Bank Header */
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "SH"
        "{int:width=7} "    +  // Sequence #
        "{date:pattern=MMddyykkmmss} "  +  // Creation Date/Time MMDDYYHHMMSS
        "{int:width=4} "    +  // Bank #
        "{trim:width=20} "  +  // Bank name
        "{fill:width=205} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final Date   CreateDate;
    public final int    BankNum;
    public final String Name;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public BankHeader(int lineNum, String data) throws ParseException
    {
        super(RecordType.BankHeader, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        CreateDate = (Date)   objs[2];
        BankNum    = (Integer)objs[3];
        Name       = (String) objs[4];
    }
}
