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
public class MerchantHeader3 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "       +  // RecordID "M3"
        "{int:width=7} "        +  // Sequence #
        "{text:width=15} "      +  // Discover #
        "{text:width=10} "      +  // Amex #
        "{text:width=10} "      +  // Diners #
        "{text:width=16} "      +  // JCB #
        "{text:width=1,pad=?} " +  // Discover Direct flag ' ' or 'D' or 'C' 
        "{text:width=1,pad=?} " +  // Amex Direct flag ' ' or 'D' or 'C' 
        "{fill:width=188} ";       // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String DiscoverNum;
    public final String AmexNum;
    public final String DinersNum;
    public final String JCBNum;
    public final char   DiscoverDirect;
    public final char   AmexDirect;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public MerchantHeader3(int lineNum, String data) throws ParseException
    {
        super(RecordType.MerchantHeader3, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        DiscoverNum = (String) objs[2];
        AmexNum     = (String) objs[3];
        DinersNum   = (String) objs[4];
        JCBNum      = (String) objs[5];
        DiscoverDirect = ((String) objs[6]).charAt(0);
        AmexDirect     = ((String) objs[7]).charAt(0);
    }
}
