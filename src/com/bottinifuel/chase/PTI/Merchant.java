/*
 * Created on Apr 6, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI;

import java.util.ArrayList;
import java.util.List;

import com.bottinifuel.chase.PTI.records.MerchantHeader;
import com.bottinifuel.chase.PTI.records.MerchantHeader2;
import com.bottinifuel.chase.PTI.records.MerchantHeader3;
import com.bottinifuel.chase.PTI.records.MerchantHeader4;
import com.bottinifuel.chase.PTI.records.MerchantHeader5;
import com.bottinifuel.chase.PTI.records.MerchantTrailer;


/**
 * @author pladd
 *
 */
public class Merchant
{
    private MerchantContainer Parent;
    
    private MerchantHeader  Header;
    private MerchantHeader2 M2 = null;
    private MerchantHeader3 M3 = null;
    private MerchantHeader4 M4 = null;
    private MerchantHeader5 M5 = null;
    private MerchantTrailer Trailer = null;

    private int RecordCount = 0;

    private List<Batch> Batches = new ArrayList<Batch>();

    protected Merchant(MerchantContainer mc, MerchantHeader mh)
    {
        Parent = mc;
        mc.AddMerchant(this);
        Header = mh;
        RecordCount++;
    }
    
    protected void IncrRecordCount(int rc)
    {
        RecordCount += rc;
    }
    
    protected void SetTrailer(MerchantTrailer mt)
    {
        Trailer = mt;
        RecordCount++;
    }
    
    protected void AddBatch(Batch b)
    {
        Batches.add(b);
    }
    
    protected void AddRecord(MerchantHeader2 m2) throws FileFormatException
    {
        if (M2 != null)
            throw new FileFormatException("Multiple M2 com.bottinifuel.chase.PTI.records for merchant");
        M2 = m2;
        RecordCount++;
    }

    protected void AddRecord(MerchantHeader3 m3) throws FileFormatException
    {
        if (M3 != null)
            throw new FileFormatException("Multiple M3 com.bottinifuel.chase.PTI.records for merchant");
        M3 = m3;
        RecordCount++;
    }

    protected void AddRecord(MerchantHeader4 m4) throws FileFormatException
    {
        if (M4 != null)
            throw new FileFormatException("Multiple M4 com.bottinifuel.chase.PTI.records for merchant");
        M4 = m4;
        RecordCount++;
    }

    protected void AddRecord(MerchantHeader5 m5) throws FileFormatException
    {
        if (M5 != null)
            throw new FileFormatException("Multiple M5 com.bottinifuel.chase.PTI.records for merchant");
        M5 = m5;
        RecordCount++;
    }
    
    protected void Close() throws FileFormatException
    {
        if (Trailer == null)
            throw new FileFormatException("No MT seen for merchant starting on line #" + Header.SeqNum);

        if (RecordCount != Trailer.RecordCount)
            throw new FileFormatException("Merchant record count mismatch: BT=" + Trailer.RecordCount + " Saw=" + RecordCount);
        Parent.IncrRecordCount(RecordCount);
        
        if (Batches.size() != Trailer.BatchCount)
            throw new FileFormatException("Merchant batch count mismatch: BT=" + Trailer.BatchCount + " Saw=" + Batches.size());
        
        int saleCount   = 0;
        int saleCents   = 0;
        int returnCount = 0;
        int returnCents = 0;
        for (Batch b : Batches)
        {
            saleCount   += b.getSalesCount();
            saleCents   += b.getSalesCents();
            returnCount += b.getReturnCount();
            returnCents += b.getReturnCents();
        }
        
        if (saleCount != Trailer.SalesCount)
            throw new FileFormatException("Merchant sale count mismatch: BT=" + Trailer.SalesCount + " Saw=" + saleCount);
        if (saleCents != Trailer.SalesTotal)
            throw new FileFormatException("Merchant sale total mismatch: BT=" + Trailer.SalesTotal + " Saw=" + saleCents);
        if (returnCount != Trailer.ReturnCount)
            throw new FileFormatException("Merchant return count mismatch: BT=" + Trailer.ReturnCount + " Saw=" + returnCount);
        if (returnCents != Trailer.ReturnTotal)
            throw new FileFormatException("Merchant return total mismatch: BT=" + Trailer.ReturnTotal + " Saw=" + returnCents);
    }
    public int getSalesCount()  { return Trailer.SalesCount; }
    public int getSalesCents()  { return Trailer.SalesTotal; }
    public double getSalesAmount()
    {
        double temp = getSalesCents();
        return temp / 100;
    }
    
    public int getReturnCount()  { return Trailer.ReturnCount; }
    public int getReturnCents()  { return Trailer.ReturnTotal; }
    public double getReturnAmount()
    {
        double temp = getReturnCents();
        return temp / 100;
    }
    
    public List<Transaction> getAllTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Batch b : Batches)
        {
            rc.addAll(b.getAllTransactions());
        }

        return rc;
    }

    public List<Transaction> getTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Batch b : Batches)
        {
            rc.addAll(b.getTransactions());
        }

        return rc;
    }

    public List<Transaction> getReturnTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Batch b : Batches)
        {
            rc.addAll(b.getReturnTransactions());
        }

        return rc;
    }

    public List<Transaction> getSalesTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Batch b : Batches)
        {
            rc.addAll(b.getSalesTransactions());
        }

        return rc;
    }
    
    public List<Batch> getBatches()
    {
        return Batches;
    }
    
    public String getMerchantID()
    {
    	return Header.BankNum;
    }
}
