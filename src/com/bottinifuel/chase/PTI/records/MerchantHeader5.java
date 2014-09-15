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
public class MerchantHeader5 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "M5"
        "{int:width=7} "    +  // Sequence #
        "{text:width=2} "   +  // Custom Card 1 Type 
        "{text:width=1} "   +  // Custom Card 1 Settlement Flag 'D' or 'C'
        "{text:width=16} "  +  // Custom Card 1 Merchant ID
        "{text:width=2} "   +  // Custom Card 2 Type 
        "{text:width=1} "   +  // Custom Card 2 Settlement Flag 'D' or 'C'
        "{text:width=16} "  +  // Custom Card 2 Merchant ID
        "{text:width=2} "   +  // Custom Card 3 Type 
        "{text:width=1} "   +  // Custom Card 3 Settlement Flag 'D' or 'C'
        "{text:width=16} "  +  // Custom Card 3 Merchant ID
        "{fill:width=184} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String CardType1;
    public final char   SettleFlag1;
    public final String MerchID1;
    public final String CardType2;
    public final char   SettleFlag2;
    public final String MerchID2;
    public final String CardType3;
    public final char   SettleFlag3;
    public final String MerchID3;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public MerchantHeader5(int lineNum, String data) throws ParseException
    {
        super(RecordType.MerchantHeader5, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        CardType1   = (String)  objs[ 2];
        SettleFlag1 = ((String) objs[ 3]).charAt(0);
        MerchID1    = (String)  objs[ 4];
        CardType2   = (String)  objs[ 5];
        SettleFlag2 = ((String) objs[ 6]).charAt(0);
        MerchID2    = (String)  objs[ 7];
        CardType3   = (String)  objs[ 8];
        SettleFlag3 = ((String) objs[ 9]).charAt(0);
        MerchID3    = (String)  objs[10];
    }
}
