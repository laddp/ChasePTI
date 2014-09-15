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
public class GroupHeader extends PTIRecord
{
    public static final String FormatStr =
        "{text:width=2} "   +  // RecordID "GH"
        "{int:width=7} "    +  // Sequence #
        "{date:pattern=MMddyykkmmss} "  +  // Creation Date/Time MMDDYYHHMMSS
        "{trim:width=10} "  +  // Chase assigned group name
        "{trim:width=20} "  +  // Settlment config group name
        "{fill:width=199} ";   // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final Date   CreateDate;
    public final String GroupName;
    public final String ConfigName;
    
    /**
     * @param rt
     * @param lineNum
     * @param hdr
     */
    public GroupHeader(int lineNum, String data) throws ParseException
    {
        super(RecordType.GroupHeader, lineNum, data.substring(0,9));
        
        Object [] objs = fmt.parse(data);

        CreateDate = (Date)   objs[2];
        GroupName  = (String) objs[4];
        ConfigName = (String) objs[5];
    }
}
