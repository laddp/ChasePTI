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
public class DivisionTrailer extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "DT"
        "{int:width=7} "    +  // Sequence #
        "{int:width=8} "    +  // Sales Count
        "{int:width=14} "   +  // Sales Total $
        "{int:width=8} "    +  // Returns Count
        "{int:width=14} "   +  // Returns Total $
        "{int:width=8} "    +  // Record Count
        "{int:width=8} "    +  // Merchant Count
        "{fill:width=181} ";   // filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final int  SalesCount;
    public final int  SalesTotal;
    public final int  ReturnCount;
    public final int  ReturnTotal;
    public final int  RecordCount;
    public final int  MerchantCount;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public DivisionTrailer(int lineNum, String data) throws ParseException
    {
        super(RecordType.DivsionTrailer, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        SalesCount    = (Integer) objs[2];
        SalesTotal    = (Integer) objs[3];
        ReturnCount   = (Integer) objs[4];
        ReturnTotal   = (Integer) objs[5];
        RecordCount   = (Integer) objs[6];
        MerchantCount = (Integer) objs[7];
    }
}
