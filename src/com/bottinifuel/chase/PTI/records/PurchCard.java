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
public class PurchCard extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "P3"
        "{int:width=7} "    +  // Sequence #
        "{int:width=2} "    +  // Purchasing Card Addenda Type
        "{text:width=241,pad=?} ";   // filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final int    Type;
    public final String Data;

    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public PurchCard(int lineNum, String data) throws ParseException
    {
        super(RecordType.PurchCard, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        Type = (Integer) objs[2];
        Data = (String)  objs[3];
    }
}
