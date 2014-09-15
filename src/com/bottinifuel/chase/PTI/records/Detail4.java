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
public class Detail4 extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "D4"
        "{int:width=7} "    +  // Sequence #
        "{int:width=3} "    +  // Industry Addenda Type
        "{text:width=238,pad=?} ";   // Content

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public enum DetailType {
        LODGING   (10),
        AUTORENT1 (20),
        AUTORENT2 (21),
        MAILPHONE (30),
        PETROLEUM (40),
        PETROLEUM1(41),
        PETROLEUM2(42),
        PETROLEUM3(43),
        PETROLEUM4(44),
        PETROLEUM5(45),
        PETROLEUM6(46),
        PETROLEUM7(47),
        PETROLEUM8(48),
        PETROLEUM9(49),
        RESTAURANT(50),
        RETAIL    (60),
        ECOMMERCE (70),
        STORED_VAL(80);

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
    public Detail4(int lineNum, String data) throws ParseException, FileFormatException
    {
        super(RecordType.Detail4, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        Type = DetailType.intToDT((Integer)objs[2]);
        Data = (String)  objs[3];
    }


    public Detail4(Detail4 d4)
    {
        super(d4);
        Type = d4.Type;
        Data = d4.Data;
    }
}
