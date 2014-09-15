/*
 * Created on Apr 6, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI;


import com.bottinifuel.chase.PTI.records.DivisionHeader;
import com.bottinifuel.chase.PTI.records.DivisionTrailer;
import com.bottinifuel.chase.PTI.records.PTIRecord;

/**
 * @author pladd
 *
 */
public class Division extends MerchantContainer
{
    private DivisionHeader  Header;
    private DivisionTrailer Trailer;

    protected Division(PTIFile f, DivisionHeader dh)
    {
        super(f);
        Header = dh;
        RecordCount++;
    }
    
    @Override
    protected void SetTrailer(PTIRecord t) throws FileFormatException
    {
        if (!t.getClass().getName().equals("com.bottinifuel.chase.PTI.records.DivisionTrailer"))
            throw new FileFormatException("Invalid trailer record type: expected ST, got " +
                                          t.getClass().getName());
        Trailer = (DivisionTrailer)t;
        RecordCount++;
    }

    @Override
    protected void Close() throws FileFormatException
    {
        if (Trailer == null)
            throw new FileFormatException("No DT seen for division starting on line #" + Header.SeqNum);

        if (RecordCount != Trailer.RecordCount)
            throw new FileFormatException("Division record count mismatch: BT=" + Trailer.RecordCount + " Saw=" + RecordCount);
        Parent.IncrRecordCount(RecordCount);
        
        if (Merchants.size() != Trailer.MerchantCount)
            throw new FileFormatException("Division Merchant count mismatch: BT=" + Trailer.MerchantCount + " Saw=" + Merchants.size());
        
        int saleCount   = 0;
        int saleCents   = 0;
        int returnCount = 0;
        int returnCents = 0;
        for (Merchant m : Merchants)
        {
            saleCount   += m.getSalesCount();
            saleCents   += m.getSalesCents();
            returnCount += m.getReturnCount();
            returnCents += m.getReturnCents();
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
    
    public int getReturnCount()  { return Trailer.ReturnCount; }
    public int getReturnCents()  { return Trailer.ReturnTotal; }
}
