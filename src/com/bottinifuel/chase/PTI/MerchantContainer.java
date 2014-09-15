/*
 * Created on Apr 7, 2007 by Administrator
 *
 */
package com.bottinifuel.chase.PTI;

import java.util.ArrayList;
import java.util.List;

import com.bottinifuel.chase.PTI.records.PTIRecord;


public abstract class MerchantContainer
{
    protected int RecordCount = 0;

    protected PTIFile Parent; 
    
    protected List<Merchant> Merchants = new ArrayList<Merchant>();

    protected MerchantContainer(PTIFile f)
    {
        Parent = f;
        f.AddMerchantContainer(this);
    }

    protected void IncrRecordCount(int rc)
    {
        RecordCount += rc;
    }

    protected void AddMerchant(Merchant m)
    {
        Merchants.add(m);
    }

    protected abstract void SetTrailer(PTIRecord t) throws FileFormatException;
    protected abstract void Close() throws FileFormatException;

    abstract public int getSalesCount();
    abstract public int getSalesCents();
    public double getSalesAmount()
    {
        double temp = getSalesCents();
        return temp / 100;
    }
    
    abstract public int getReturnCount();
    abstract public int getReturnCents();
    public double getReturnAmount()
    {
        double temp = getReturnCents();
        return temp / 100;
    }

    public List<Transaction> getAllTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Merchant m : Merchants)
        {
            rc.addAll(m.getAllTransactions());
        }

        return rc;
    }

    public List<Transaction> getTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Merchant m : Merchants)
        {
            rc.addAll(m.getTransactions());
        }

        return rc;
    }

    public List<Transaction> getCreditTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Merchant m : Merchants)
        {
            rc.addAll(m.getReturnTransactions());
        }

        return rc;
    }

    public List<Transaction> getSalesTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (Merchant m : Merchants)
        {
            rc.addAll(m.getSalesTransactions());
        }

        return rc;
    }
    
    public List<Merchant> getMerchants()
    {
        return Merchants;
    }
}
