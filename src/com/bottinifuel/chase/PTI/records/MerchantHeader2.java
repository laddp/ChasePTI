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
public class MerchantHeader2 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "M2"
        "{int:width=7} "    +  // Sequence #
        "{trim:width=20} "  +  // Merchant Name
        "{trim:width=27} "  +  // Merchant Address
        "{trim:width=15} "  +  // Merchant City
        "{text:width=2} "   +  // Merchant State
        "{int:width=3} "    +  // Merchant Country Code
        "{fill:width=174} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String Name;
    public final String Address;
    public final String City;
    public final String State;
    public final int    Country;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public MerchantHeader2(int lineNum, String data) throws ParseException
    {
        super(RecordType.MerchantHeader2, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        Name    = (String) objs[2];
        Address = (String) objs[3];
        City    = (String) objs[4];
        State   = (String) objs[5];
        Country = (Integer)objs[6];
    }
}
