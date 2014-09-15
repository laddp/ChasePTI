/*
 * Created on Apr 5, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI.records;

import java.text.ParseException;

import com.ribomation.fixedwidthfield.Formatter;


/**
 * @author pladd
 *
 */
public class MerchantHeader4 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "M4"
        "{int:width=7} "    +  // Sequence #
        "{int:width=8} "    +  // FCS Number
        "{text:width=15} "  +  // Card Acceptor ID Code
        "{fill:width=218} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final int    FCSNum;
    public final String AcceptorID;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public MerchantHeader4(int lineNum, String data) throws ParseException
    {
        super(RecordType.MerchantHeader4, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        FCSNum     = (Integer) objs[2];
        AcceptorID = (String)  objs[3];
    }
}
