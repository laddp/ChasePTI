/*
 * Created on Apr 7, 2007 by Administrator
 *
 */
package com.bottinifuel.chase.PTI.records;

import java.text.ParseException;

import com.bottinifuel.chase.PTI.FileFormatException;
import com.ribomation.fixedwidthfield.Formatter;

/**
 * @author Administrator
 *
 */
public class Detail4_ECommerce extends Detail4
{
    private static final String FormatStr =
        "{trim:width=16} "      +  // Order Num
        "{text:width=2,pad=?} " +  // E Commerce indicator
        "{text:width=2,pad=?} " +  // E Commerce goods
        "{fill:width=218} ";       // Filler 

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String OrderNum;
    public final String ECommIndicator;
    public final String GoodsIndicator; 

    /**
     * @param lineNum
     * @param data
     * @throws ParseException
     * @throws FileFormatException
     */
    public Detail4_ECommerce(Detail4 d4)
        throws ParseException, FileFormatException
    {
        super(d4);
        if (d4.Type != DetailType.ECOMMERCE)
            throw new FileFormatException("Not D4 ECOMMERCE subtype");

        Object [] objs = fmt.parse(d4.Data);
        OrderNum       = (String)objs[0];
        ECommIndicator = (String)objs[1];
        GoodsIndicator = (String)objs[2];
    }
}
