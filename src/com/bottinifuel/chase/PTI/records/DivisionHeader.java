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
public class DivisionHeader extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "DH"
        "{int:width=7} "    +  // Sequence #
        "{date:pattern=MMddyykkmmss} "  +  // Creation Date/Time MMDDYYHHMMSS
        "{int:width=4} "    +  // Client #
        "{trim:width=5}  "  +  // Chase assigned Divison name
        "{trim:width=20} "  +  // Settlment config client/division name
        "{fill:width=200} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final Date   CreateDate;
    public final int    ClientNum;
    public final String DivisionNum;
    public final String DivisionName;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public DivisionHeader(int lineNum, String data) throws ParseException
    {
        super(RecordType.DivisionHeader, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        CreateDate   = (Date)   objs[2];
        ClientNum    = (Integer)objs[3];
        DivisionNum  = (String) objs[4];
        DivisionName = (String) objs[5];
    }
}
