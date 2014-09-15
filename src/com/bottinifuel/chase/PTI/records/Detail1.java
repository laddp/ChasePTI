/*
 * Created on Apr 5, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI.records;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.ribomation.fixedwidthfield.Formatter;


/**
 * @author pladd
 *
 */
public class Detail1 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "       +  // RecordID "D1"
        "{int:width=7} "        +  // Sequence #
        "{int:width=6} "        +  // Trans Sequence #
        "{int:width=1} "        +  // Record Type
        "{int:width=2} "        +  // Trans Code
        "{text:width=2} "       +  // Card Type 
        "{text:width=19} "      +  // Card #
        "{date:pattern=MMddyykkmmss} "  +  // Trans Date/Time MMDDYYHHMMSS
        "{int:width=8} "        +  // Trans $
        "{text:width=6} "       +  // Auth # 
        "{int:width=4} "        +  // Card Exp Date MMYY
        "{fill:width=1} "       +  // PIN entry cap *4.1*
        "{text:width=1,pad=?} " +  // POS Capability  
        "{text:width=2} "       +  // POS mode  
        "{text:width=1,pad=?} " +  // Cardholder ID method
        "{text:width=1,pad=?} " +  // Card activated terminal
        "{text:width=1,pad=?} " +  // Transaction void
        "{text:width=1,pad=?} " +  // ACK Received
        "{text:width=1,pad=?} " +  // Discount flag 
        "{text:width=1} "       +  // Settle flag
        "{text:width=1,pad=?} " +  // Batch release type
        "{int:width=6} "        +  // TransProcCode *4.1*
        "{int:width=6} "        +  // SysTraceAuditNum *4.1*
        "{int:width=2} "        +  // TrackCondition *4.1*
        "{text:width=1} "       +  // PatrialShipment *4.1*
        "{text:width=2} "       +  // CardTypeInd *4.1*
        "{fill:width=153}";        // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    protected static Map<Integer, String> RecordTypes;
    
    public final int    TransSeqNum;
    public final int    Type;
    public final int    Code;
    public final String CardType;
    public final String CardNum;
    public final Date   Date;
    public final int    Amount;
    public final String AuthNum;
    public final int    ExpDate;
    public final char   POSCapability;
    public final String POSMode;
    public final char   IDMethod;
    public final char   CardActTerm;
    public final char   TransVoid;
    public final char   ACK;
    public final char   Discount;
    public final char   SettleFlag;
    public final char   BatchReleaseType;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public Detail1(int lineNum, String data) throws ParseException
    {
        super(RecordType.Detail1, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        TransSeqNum      = (Integer) objs[ 2];
        Type             = (Integer) objs[ 3];
        Code             = (Integer) objs[ 4];
        CardType         = (String)  objs[ 5];
        CardNum          = (String)  objs[ 6];
        Date             = (Date)    objs[ 7];
        Amount           = (Integer) objs[ 8];
        AuthNum          = (String)  objs[ 9];
        ExpDate          = (Integer) objs[10];
        // skip 10 for null filler
        POSCapability    = ((String) objs[12]).charAt(0);
        POSMode          = (String)  objs[13];
        IDMethod         = ((String) objs[14]).charAt(0);
        CardActTerm      = ((String) objs[15]).charAt(0);
        TransVoid        = ((String) objs[16]).charAt(0);
        ACK              = ((String) objs[17]).charAt(0);
        Discount         = ((String) objs[18]).charAt(0);
        SettleFlag       = ((String) objs[19]).charAt(0);
        BatchReleaseType = ((String) objs[20]).charAt(0);
    }
}
