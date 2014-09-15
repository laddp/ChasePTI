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
public class Detail3 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "       +  // RecordID "D3"
        "{int:width=7} "        +  // Sequence #
        "{text:width=2} "       +  // Device ID
        "{text:width=2} "       +  // Pump ID
        "{text:width=1,pad=?} " +  // Visa CAVV result
        "{text:width=1,pad=?} " +  // MC UCAF indicator
        "{text:width=8} "       +  // Canada legacy terminal ID
        "{int:width=8} "        +  // Cash back $
        "{text:width=2} "       +  // Visa Card Level Results
        "{int:width=2} "        +  // Entry Data source
        "{text:width=16} "      +  // POS Data Code
        "{text:width=1,pad=?} " +  // Store and Forward indicator
        "{date:pattern=MMddyykkmmss} "  +  // Store and Forwad orig Date/Time MMDDYYHHMMSS
        "{text:width=4} "       +  // Transaction MCC
        "{int:width=4} "        +  // Settlement Date MMDD
        "{fill:width=178} ";       // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String DeviceID;
    public final String PumpID;
    public final char   CAVVResult;
    public final char   UCAFIndic;
    public final String CanadaLegacyID;
    public final int    CashBackAmount;
    public final String VisaCardLevelResult;
    public final int    EntryDataSrc;
    public final String POSDataCode;
    public final char   StoreFwdIndic;
    public final Date   StoreFwdOrigDate;
    public final int    TransMCC;
    public final int    SettlementDate;

    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public Detail3(int lineNum, String data) throws ParseException
    {
        super(RecordType.Detail3, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        DeviceID            = (String)  objs[ 2];
        PumpID              = (String)  objs[ 3];
        CAVVResult          = ((String) objs[ 4]).charAt(0);
        UCAFIndic           = ((String) objs[ 5]).charAt(0);
        CanadaLegacyID      = (String)  objs[ 6];
        CashBackAmount      = (Integer) objs[ 7];
        VisaCardLevelResult = (String)  objs[ 8];
        EntryDataSrc        = (Integer) objs[ 9];
        POSDataCode         = (String)  objs[10];
        StoreFwdIndic       = ((String) objs[11]).charAt(0);
        StoreFwdOrigDate    = (Date)    objs[12];
        SettlementDate      = (Integer) objs[14];

        /* TransMCC was blank for a few in test file */
        String tmpMCC = (String)objs[13];
        if (tmpMCC.length() > 0)
            TransMCC = Integer.valueOf(tmpMCC);
        else
            TransMCC = 0;
    }
}
