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
public class MerchantHeader extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "       +  // RecordID "MH"
        "{int:width=7} "        +  // Sequence #
        "{trim:width=12} "      +  // Merchant #
        "{trim:width=16}"       +  // Bank merchant #
        "{text:width=1} "       +  // Merchant type 'H' or 'T'
        "{int:width=4} "        +  // Merchant Category Code (MCC)
        "{trim:width=9} "       +  // Merchant zip code
        "{trim:width=2} "       +  // Chain ID
        "{trim:width=4} "       +  // Site ID
        "{text:width=1,pad=?} " +  // Diners Direct flag ' ' or 'D' or 'C' 
        "{trim:width=10}"       +  // Merchant phone #
        "{trim:width=10}"       +  // Cust Svc phone #
        "{text:width=1,pad=?} " +  // JCB Direct flag ' ' or 'D' or 'C' 
        "{fill:width=171} ";       // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String MerchantNum;
    public final String BankNum;
    public final char   MerchType;
    public final int    MCC;
    public final String Zip;
    public final String Chain;
    public final String Site;
    public final char   DinersDirect;
    public final String PhoneNum;
    public final String CustSvcPhoneNum;
    public final char   JCBDirect;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public MerchantHeader(int lineNum, String data) throws ParseException
    {
        super(RecordType.MerchantHeader, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        MerchantNum     = (String)  objs[ 2];
        BankNum         = (String)  objs[ 3];
        MerchType       = ((String) objs[ 4]).charAt(0);
        MCC             = (Integer) objs[ 5];
        Zip             = (String)  objs[ 6];
        Chain           = (String)  objs[ 7];
        Site            = (String)  objs[ 8];
        DinersDirect    = ((String) objs[ 9]).charAt(0);
        PhoneNum        = (String)  objs[10];
        CustSvcPhoneNum = (String)  objs[11];
        JCBDirect       = ((String) objs[12]).charAt(0);
    }
}
