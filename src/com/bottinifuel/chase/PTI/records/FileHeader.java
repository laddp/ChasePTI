/*
 * Created on Apr 4, 2007 by pladd
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
public class FileHeader extends PTIRecord
{
    /** PTIFile header */
    public static final String FormatStr =
        "{text:width=2} "       +  // RecordID "FH"
        "{int:width=7} "        +  // Sequence #
        "{date:pattern=MMddyykkmmss} "  +  // Creation Date/Time
        "{trim:width=20} "      +  // PTIFile name
        "{text:width=1} "       +  // PTIFile Type: 'A' or 'S'
        "{trim:width=3} "       +  // Version
        "{fill:width=205} ";       // space filler

    static private Formatter fmt = new Formatter();
    static {
        fmt.addFwfClass("trim", com.bottinifuel.chase.PTI.TrimTextFWF.class);
        fmt.setFields(FormatStr);
    }

    public final String FileName;
    public final char   Type;
    public final String Version;
    public final Date   CreateDate;

    /**
     * @param rt
     * @param lineNum
     */
    public FileHeader(int lineNum, String data) throws ParseException
    {
        super(RecordType.FileHeader, lineNum, data.substring(0,9));

        Object [] objs = fmt.parse(data);
        CreateDate =  (Date)  objs[2];
        FileName   =  (String)objs[3];
        Type       = ((String)objs[4]).charAt(0);
        Version    =  (String)objs[5];
    }
}
