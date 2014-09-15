/*
 * Created on Apr 5, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI.records;

import java.text.ParseException;

import com.bottinifuel.chase.PTI.FileFormatException;
import com.ribomation.fixedwidthfield.Formatter;


/**
 * @author pladd
 *
 */
public class Detail5 extends PTIRecord
{
    private static final String FormatStr =
        "{text:width=2} "   +  // RecordID "D5"
        "{int:width=7} "    +  // Sequence #
        "{int:width=2} "    +  // Detail Addenda Type
        "{text:width=239,pad=?} ";   // Content

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public enum DetailType {
        DEBIT       ( 1),
        FLEET_FUEL  ( 2),
        PURCH_CARD  ( 3),
        EBT         ( 5),
        STORED_VAL  ( 6),
        MISC_DATA   ( 7),
        SOFT_DESC   ( 8),
        CHECK_AUTH  ( 9),
        DYN_CUR_CONV(10),
        HEALTHCARE  (11), //*4.1*
        MC_PAYPASS  (12), //*4.1*
        REVOLUTION  (13),
        POSVARCAP   (14),
        CONV_FEE    (15),
        CHECK_ECP   (16),
        MOBILE_DAT  (17);

        private final int TypeNum;
        private DetailType(int t)
        {
            TypeNum = t;
        }
        
        public static DetailType intToDT(int dt) throws FileFormatException
        {
            for (DetailType t : DetailType.values())
                if (t.TypeNum == dt)
                    return t;
            throw new FileFormatException("Invalid DetailType: " + dt);
        }
    }
    
    public final DetailType Type;
    public final String     Data;

    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public Detail5(int lineNum, String data)
        throws ParseException, FileFormatException
    {
        super(RecordType.Detail5, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        Type = DetailType.intToDT((Integer) objs[2]);
        Data = (String)objs[3];
    }
    
    public Detail5(Detail5 d5)
    {
        super(d5);
        Type = d5.Type;
        Data = d5.Data;
    }
}
