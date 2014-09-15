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
public class Detail2 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "       + // RecordID "D2"
        "{int:width=7} "        + // Sequence #
        "{text:width=3} "       + // Communications type
        "{int:width=4} "        + // Server error code
        "{text:width=6} "       + // Server name
        "{text:width=1,pad=?} " + // AVS Response
        "{text:width=1,pad=?} " + // AVS Requested
        "{text:width=2} "       + // Downgrade reason
        "{text:width=2} "       + // Authorization response code
        "{text:width=1,pad=?} " + // Authorization characteristics
        "{text:width=15} "      + // Qualification data
        "{text:width=4} "       + // Validation code
        "{text:width=1,pad=?} " + // Authorization source
        "{int:width=8} "        + // Auth $
        "{int:width=8} "        + // Total Auth $
        "{text:width=1,pad=?} " + // Auth Type
        "{text:width=1,pad=?} " + // Credit Prepaid Indicator
        "{text:width=1,pad=?} " + // MSDI Inidicator
        "{int:width=2} "        + // Duration
        "{text:width=2} "       + // CPS Industry
        "{text:width=1,pad=?} " + // Recurring payments indicator
        "{text:width=1,pad=?} " + // CTI flag
        "{text:width=1,pad=?} " + // Cardholder verif data flag
        "{text:width=1,pad=?} " + // CVV response
        "{int:width=3} "        + // Currency code
        "{text:width=1,pad=?} " + // POS reversal reason code
        "{fill:width=170} ";      // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String CommType;
    public final int    ServerErrorCode;
    public final String ServerName;
    public final char   AVSResponse;
    public final char   AVSRequested;
    public final String DowngradeReason;
    public final String AuthResponseCode;
    public final char   AuthCharacteristics;
    public final String QualificationData;
    public final String ValidationCode;
    public final char   AuthSource;
    public final int    AuthAmount;
    public final int    TotalAuthAmount;
    public final char   AuthType;
    public final char   CreditPrepaidInd;
    public final char   MSDIInd;
    public final int    Duration;
    public final String CPSIndustry;
    public final char   RecurringInd;
    public final char   CTIFlag;
    public final char   CrdhldVerifDataFlag;
    public final char   CVVResponse;
    public final int    CurrencyCode;
    public final String POSRvrslReasonCode;

    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public Detail2(int lineNum, String data) throws ParseException
    {
        super(RecordType.Detail2, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        CommType            = (String)  objs[ 2];
        ServerErrorCode     = (Integer) objs[ 3];
        ServerName          = (String)  objs[ 4];
        AVSResponse         = ((String) objs[ 5]).charAt(0);
        AVSRequested        = ((String) objs[ 6]).charAt(0);
        DowngradeReason     = (String)  objs[ 7];
        AuthResponseCode    = (String)  objs[ 8];
        AuthCharacteristics = ((String) objs[ 9]).charAt(0);
        QualificationData   = (String)  objs[10];
        ValidationCode      = (String)  objs[11];
        AuthSource          = ((String) objs[12]).charAt(0);
        AuthAmount          = (Integer) objs[13];
        TotalAuthAmount     = (Integer) objs[14];
        AuthType            = ((String) objs[15]).charAt(0);
        CreditPrepaidInd    = ((String) objs[16]).charAt(0);
        MSDIInd             = ((String) objs[17]).charAt(0);
        Duration            = (Integer) objs[18];
        CPSIndustry         = (String)  objs[19];
        RecurringInd        = ((String) objs[20]).charAt(0);
        CTIFlag             = ((String) objs[21]).charAt(0);
        CrdhldVerifDataFlag = ((String) objs[22]).charAt(0);
        CVVResponse         = ((String) objs[23]).charAt(0);
        CurrencyCode        = (Integer) objs[24];
        POSRvrslReasonCode  = (String)  objs[25];
    }
}
