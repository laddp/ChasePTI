/*
 * Created on Apr 6, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.bottinifuel.chase.PTI.records.Detail1;
import com.bottinifuel.chase.PTI.records.Detail2;
import com.bottinifuel.chase.PTI.records.Detail3;
import com.bottinifuel.chase.PTI.records.Detail4;
import com.bottinifuel.chase.PTI.records.Detail4_ECommerce;
import com.bottinifuel.chase.PTI.records.Detail4_PhoneOrd;
import com.bottinifuel.chase.PTI.records.Detail5;
import com.bottinifuel.chase.PTI.records.Detail5_MiscData;


/**
 * @author pladd
 *
 */
public class Transaction
{
    public final Batch   BatchParent;

    private Detail1 D1;
    private Detail2 D2 = null;
    private Detail3 D3 = null;
    private Set<Detail4> Detail4s = new HashSet<Detail4>();
    private Set<Detail5> Detail5s = new HashSet<Detail5>();
    private Detail5_MiscData  MiscData = null;
    private Detail4_PhoneOrd  PhoneOrd = null;
    private Detail4_ECommerce EComm    = null;

    private int RecordCount = 0;

    public enum CardType {
        VISA         ("VI", 202),
        MASTERCARD   ("MC", 203),
        DISCOVER     ("DS", 205),
        DINERS       ("DC", 201),
        CARTEBLANCE  ("CB", 201),
        AMEX         ("AE", 204),
        JCB          ("JC", 201),
        DEBIT        ("DB", 200),
        EBT          ("EB", 200),
        CHECK_AUTH   ("CA", 200),
        BEST_PETROL  ("BE", 200),
        CLUB_CARD    ("CC", 200),
        CITIBANK     ("CF", 200),
        ENMARK       ("EN", 200),
        FLEETONE     ("FO", 200),
        DISNEY       ("DY", 200),
        GASCARD      ("GC", 200),
        GATE_PETROL  ("GT", 200),
        INGOODTASTE  ("IG", 200),
        IRVING_OIL   ("IR", 200),
        MOTOMART     ("MM", 200),
        RAI          ("RA", 200),
        SMARTCARTE   ("SC", 200),
        SHELL        ("SH", 200),
        SEARS_CANADA ("SR", 200),
        TCH          ("TH", 200),
        TCRS         ("TS", 200),
        VOYAGER      ("VY", 200),
        WRIGHT_XPRESS("WX", 200),
        STORED_VALUE ("PS", 200),
        N_A          ("",   200);

        public final String Abbrev;
        public final int    PostingCode;
        private CardType(String ct, int pc)
        {
            Abbrev = ct;
            PostingCode = pc;
        }
        
        public static CardType strToCardType(String s) throws FileFormatException
        {
            for (CardType ct : CardType.values())
                if (ct.Abbrev.equals(s))
                    return ct;
            throw new FileFormatException("Invalid CardType: " + s);
        }
    }

    public enum TransRecordType {
        CAPTURE       (1),
        AUTHORIZATION (2),
        ERROR_DECLINE (3),
        MERCH_CONTROL (4),
        NTX_AMEX_PIP  (5);
        
        public final int TypeCode;
        private TransRecordType(int trt)
        {
            TypeCode = trt;
        }
        
        public static TransRecordType intToTRT(int trt) throws FileFormatException
        {
            for (TransRecordType t : TransRecordType.values())
                if (t.TypeCode == trt)
                    return t;
            throw new FileFormatException("Invalid TransRecordType: " + trt);
        }
    }

    public enum TransType {
        INVALID         (0),
        SALE            (1),
        AUTH            (2),
        PRIOR_AUTH_SALE (3),
        NO_SHOW         (4),
        MAIL_PHONE_SALE (5),
        RETURN          (6),
        CASH_ADVANCE    (7),
        BATCH_INQUIRY   (8),
        BATCH_RELEASE   (9),
        BALANCE_INQUIRY (10);
        
        public final int TransCode;
        private TransType(int tc)
        {
            TransCode = tc;
        }
        
        public static TransType intToTransType(int tt) throws FileFormatException
        {
            for (TransType t : TransType.values())
                if (t.TransCode == tt)
                    return t;
            throw new FileFormatException("Invalid TransType: " + tt);
        }
    }

    public final TransRecordType RecordType;
    public final TransType       TransCode;
    public final CardType        Card;
    
    protected Transaction(Batch p, Detail1 d1) throws FileFormatException
    {
        BatchParent = p;
        D1 = d1;
        RecordCount++;
        
        p.AddTransaction(this);

        RecordType = TransRecordType.intToTRT(D1.Type);
        TransCode  = TransType.intToTransType(D1.Code);
        Card       = CardType.strToCardType(D1.CardType);
    }
    
    protected void AddRecord(Detail2 d2) throws FileFormatException
    {
        if (D2 != null)
            throw new FileFormatException("Multiple D2 com.bottinifuel.chase.PTI.records for transaction");
        D2 = d2;
        RecordCount++;
    }

    protected void AddRecord(Detail3 d3) throws FileFormatException
    {
        if (D3 != null)
            throw new FileFormatException("Multiple D3 com.bottinifuel.chase.PTI.records for transaction");
        D3 = d3;
        RecordCount++;
    }

    protected void AddRecord(Detail4 d4)
    {
        Detail4s.add(d4);
        RecordCount++;
    }

    protected void AddRecord(Detail5 d5)
    {
        Detail5s.add(d5);
        RecordCount++;
    }

    protected void AddRecord(Detail5_MiscData d5) throws FileFormatException
    {
        if (MiscData != null)
            throw new FileFormatException("More than one D5 MISC_DATA record");
        MiscData = d5;
        RecordCount++;
    }

    protected void AddRecord(Detail4_PhoneOrd d4) throws FileFormatException
    {
        if (PhoneOrd != null)
            throw new FileFormatException("More than one D4 MAILPHONE record");
        PhoneOrd = d4;
        RecordCount++;
    }

    protected void AddRecord(Detail4_ECommerce d4) throws FileFormatException
    {
        if (EComm != null)
            throw new FileFormatException("More than one D4 ECOMMERCE record");
        EComm = d4;
        RecordCount++;
    }

    protected void Close() throws FileFormatException
    {
        if (D2 == null)
            throw new FileFormatException("Transaction missing D2 record");
        if (D3 == null)
            throw new FileFormatException("Transaction missing D3 record");
        BatchParent.IncrRecordCount(RecordCount);
    }

    public int Cents()
    {
        return D1.Amount;
    }
    
    public double Amount()
    {
        double temp = D1.Amount;
        return temp / 100;
    }
    
    public boolean isVoid()
    {
        if (D1.TransVoid == ' ')
            return false;
        else
            return true;
    }

    public boolean isReturn()
    {
        switch (TransCode) {
        case SALE:
        case PRIOR_AUTH_SALE:
        case MAIL_PHONE_SALE:
            return false;
        case RETURN:
        case CASH_ADVANCE:
            return true;
        }
        return false;
    }

    public Detail5_MiscData getMiscData()
    {
        return MiscData;
    }
    
    public Detail4_PhoneOrd getPhoneOrd()
    {
        return PhoneOrd;
    }
    
    public Detail4_ECommerce getEComm()
    {
        return EComm;
    }
    
    public String TransID()
    {
        return D2.QualificationData;
    }
    
    public Date Date()
    {
        return D1.Date;
    }
}
