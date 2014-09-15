/*
 * Created on Apr 3, 2007 by Administrator
 *
 */
package com.bottinifuel.chase.PTI.records;

/**
 * @author pladd
 *
 */
public class PTIRecord
{
    public enum RecordType {
        FileHeader      ("FH"),
        FileTrailer     ("FT"),
        BankHeader      ("SH"),
        BankTrailer     ("ST"),
        DivisionHeader  ("DH"),
        DivsionTrailer  ("DT"),
        GroupHeader     ("GH"),
        GroupTrailer    ("GT"),
        MerchantHeader  ("MH"),
        MerchantHeader2 ("M2"),
        MerchantHeader3 ("M3"),
        MerchantHeader4 ("M4"), 
        MerchantHeader5 ("M5"),
        MerchantTrailer ("MT"),
        BatchHeader     ("BH"),
        BatchTrailer    ("BT"),
        Detail1         ("D1"),
        Detail2         ("D2"),
        Detail3         ("D3"),         
        Detail4         ("D4"),
        Detail5         ("D5"),
        PurchCard       ("P3");
        
        public final String RecordID;
        
        RecordType(String rt) { RecordID = rt; }
    }
    
    public final RecordType Type;
    public final int SeqNum;
    protected PTIRecord(RecordType rt, int lineNum, String hdr)
    {
        Type = rt;

        String checkType = hdr.substring(0, 2);
        if (!checkType.toString().equals(rt.RecordID))
            System.out.println("Error line #" + lineNum +
                               ": Record type mismatch - Expected:" + checkType +
                               " Got: " + rt.RecordID);

        SeqNum = Integer.valueOf(hdr.substring(2, 9));
        if (SeqNum != lineNum)
            System.out.println("Warning: Line/Seq number mismatch: " +
                               SeqNum + '/' + lineNum);
    }
    
    protected PTIRecord(PTIRecord p)
    {
        Type = p.Type;
        SeqNum = p.SeqNum;
    }
}
