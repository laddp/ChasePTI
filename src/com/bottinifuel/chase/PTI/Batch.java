/*
 * Created on Apr 6, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI;

import java.util.ArrayList;
import java.util.List;

import com.bottinifuel.chase.PTI.Transaction.TransRecordType;
import com.bottinifuel.chase.PTI.records.BatchHeader;
import com.bottinifuel.chase.PTI.records.BatchTrailer;


/**
 * @author pladd
 *
 */
public class Batch
{
    private BatchHeader   Header;
    private BatchTrailer  Trailer = null;
    public final Merchant MerchantParent;
    
    private int RecordCount = 0;

    private List<Transaction> Transactions       = new ArrayList<Transaction>();
    private List<Transaction> SalesTransactions  = new ArrayList<Transaction>();
    private List<Transaction> CreditTransactions = new ArrayList<Transaction>();

    protected Batch(Merchant m, BatchHeader bh)
    {
        MerchantParent = m;
        m.AddBatch(this);
        Header = bh;
        RecordCount++;
    }

    protected void IncrRecordCount(int rc)
    {
        RecordCount += rc;
    }
    
    protected void SetTrailer(BatchTrailer bt) throws FileFormatException
    {
        if (Trailer != null)
            throw new FileFormatException("Duplicate BT");
        Trailer = bt;
        RecordCount++;
    }

    protected void Close() throws FileFormatException
    {
        if (Trailer == null)
            throw new FileFormatException("No BT seen for batch starting on line #" + Header.SeqNum);

        if (RecordCount != Trailer.RecordCount)
            throw new FileFormatException("Batch record count mismatch: BT=" + Trailer.RecordCount + " Saw=" + RecordCount);
        MerchantParent.IncrRecordCount(RecordCount);

        if (Transactions.size() != Trailer.TransCount)
            throw new FileFormatException("Batch allTrans count mismatch: BT=" + Trailer.TransCount + " Saw=" + Transactions.size());
        
        int saleCount   = 0;
        int saleCents   = 0;
        int returnCount = 0;
        int returnCents = 0;
        for (Transaction t : Transactions)
        {
            if (t.isVoid())
                continue;
            if (t.RecordType != TransRecordType.CAPTURE)
                continue;
            if (t.isReturn())
            {
                CreditTransactions.add(t);
                returnCount++;
                returnCents += t.Cents();
            }
            else
            {
                    SalesTransactions.add(t);
                    saleCount++;
                    saleCents += t.Cents();
            }
        }

        if (saleCount != Trailer.SalesCount)
            throw new FileFormatException("Batch sale count mismatch: BT=" + Trailer.SalesCount + " Saw=" + saleCount);
        if (saleCents != Trailer.SalesTotal)
            throw new FileFormatException("Batch sale total mismatch: BT=" + Trailer.SalesTotal + " Saw=" + saleCents);
        if (returnCount != Trailer.ReturnCount)
            throw new FileFormatException("Batch return count mismatch: BT=" + Trailer.ReturnCount + " Saw=" + returnCount);
        if (returnCents != Trailer.ReturnTotal)
            throw new FileFormatException("Batch return total mismatch: BT=" + Trailer.ReturnTotal + " Saw=" + returnCents);
    }

    protected void AddTransaction(Transaction t)
    {
        Transactions.add(t);
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
        return Transactions;
    }

    public List<Transaction> getTransactions()
    {
        List<Transaction> rc =
            new ArrayList<Transaction>(SalesTransactions.size()+CreditTransactions.size());
        rc.addAll(SalesTransactions);
        rc.addAll(CreditTransactions);
        return rc;
    }
    
    public List<Transaction> getReturnTransactions()
    {
        return CreditTransactions;
    }
    
    public List<Transaction> getSalesTransactions()
    {
        return SalesTransactions;
    }    
}
