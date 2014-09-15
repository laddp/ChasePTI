package com.bottinifuel.chase.PTI;

import java.text.ParseException;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import com.ribomation.fixedwidthfield.formatter.AbstractFWF;

/***
 * Extension of the basic TextFWF - assumes pad character is space, trims excess spaces off edges
 * Represents an aligned text, like 'foo***'. 
 * Formatting a text value having a length greater
 * than 'width', returns a truncated value. Formatting a text value having a length
 * less than 'width' pads the rest with 'pad'. Alignment can be one of {left, center, right}.
 *
 * <table border=1 cellspacing=0>
 *  <tr bgcolor=silver> <th colspan=3>Properties</th> </tr>
 *  <tr bgcolor=silver> <th>Name</th> <th>Mandatory</th> <th>Description</th> </tr>
 *  <tr> <th>width</th> <td>yes</td> <td>Width of this FWF</td> </tr>
 *  <tr> <th>align</th> <td>no</td> <td>Alignment of this field. Must be one of {left, center, right}. Default is 'left'</td> </tr>
 * </table>
 *
 * @author @AUTHOR@
 * @version @VERSION@
 * @created Jens, 2004-mar-29 13:07:09
 */
public class TrimTextFWF extends AbstractFWF {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final Set    validAlign = new HashSet(Arrays.asList(new Object[] {"left", "center", "right"}));
    private String              align      = "left";

    /***
     * Creates a text FWF and sets the name to 'text';
     */
    public TrimTextFWF() {
        super("trim");
    }

    /***
     * Returns the alignment.
     * @return its alignment
     */
    public String getAlign() {return align;}

    /***
     * Sets the alignment. Must be one of {left, right}.
     * @param align its new alignment
     */
    public void setAlign(String align) {
        if (validAlign.contains(align) == false) {
            throw new IllegalArgumentException("Invalid align '"+align+"', must be one of "+validAlign);
        }
        this.align = align;
    }

    public String format(Object value) {
        String  txt = value.toString();
        if (txt.length() >= getWidth()) return txt.substring(0, getWidth());

        String        padding = createPadding(" ", getWidth() - txt.length());
        StringBuffer  b       = new StringBuffer(getWidth());
        if (getAlign().equals("left")) {
            b.append(txt).append(padding);
        } else if (getAlign().equals("center")) {
            int     midPos   = padding.length() / 2;
            String  padLeft  = padding.substring(0, midPos);
            String  padRight = padding.substring(midPos, padding.length());
            b.append(padLeft).append(txt).append(padRight);
        } else if (getAlign().equals("right")) {
            b.append(padding).append(txt);
        }
        return b.toString();
    }

    public Object parse(CharSequence formattedValue) throws ParseException {
        String  txt = formattedValue.toString();
        return txt.trim();
    }
}
