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
public class BatchHeader extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "BH"
        "{int:width=7} "    +  // Sequence #
        "{int:width=3} "    +  // Terminal #
        "{int:width=6} "    +  // Batch #
        "{text:width=1} "   +  // Batch Type 'A' or 'T' or 'H' 
        "{int:width=2} "    +  // Upload #
        "{date:pattern=MMddyykkmmss} "  +  // Creation Date/Time MMDDYYHHMMSS
        "{fill:width=217} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final int  TerminalNum;
    public final int  BatchNum;
    public final char BatchType;
    public final int  UploadNum;
    public final Date CreateDate;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public BatchHeader(int lineNum, String data) throws ParseException
    {
        super(RecordType.BatchHeader, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        TerminalNum = (Integer) objs[2];
        BatchNum    = (Integer) objs[3];
        BatchType   = ((String) objs[4]).charAt(0);
        UploadNum   = (Integer) objs[5];
        CreateDate  = (Date)    objs[6];
    }
}
