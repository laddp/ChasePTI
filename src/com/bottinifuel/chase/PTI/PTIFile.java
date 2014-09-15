/*
 * Created on Apr 3, 2007 by Administrator
 *
 */
package com.bottinifuel.chase.PTI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bottinifuel.chase.PTI.records.BankHeader;
import com.bottinifuel.chase.PTI.records.BankTrailer;
import com.bottinifuel.chase.PTI.records.BatchHeader;
import com.bottinifuel.chase.PTI.records.BatchTrailer;
import com.bottinifuel.chase.PTI.records.Detail1;
import com.bottinifuel.chase.PTI.records.Detail2;
import com.bottinifuel.chase.PTI.records.Detail3;
import com.bottinifuel.chase.PTI.records.Detail4;
import com.bottinifuel.chase.PTI.records.Detail4_ECommerce;
import com.bottinifuel.chase.PTI.records.Detail4_PhoneOrd;
import com.bottinifuel.chase.PTI.records.Detail5;
import com.bottinifuel.chase.PTI.records.Detail5_MiscData;
import com.bottinifuel.chase.PTI.records.DivisionHeader;
import com.bottinifuel.chase.PTI.records.DivisionTrailer;
import com.bottinifuel.chase.PTI.records.FileHeader;
import com.bottinifuel.chase.PTI.records.FileTrailer;
import com.bottinifuel.chase.PTI.records.GroupHeader;
import com.bottinifuel.chase.PTI.records.GroupTrailer;
import com.bottinifuel.chase.PTI.records.MerchantHeader;
import com.bottinifuel.chase.PTI.records.MerchantHeader2;
import com.bottinifuel.chase.PTI.records.MerchantHeader3;
import com.bottinifuel.chase.PTI.records.MerchantHeader4;
import com.bottinifuel.chase.PTI.records.MerchantHeader5;
import com.bottinifuel.chase.PTI.records.MerchantTrailer;
import com.bottinifuel.chase.PTI.records.PurchCard;

/**
 * @author pladd
 *
 */
public class PTIFile
{
    private int RecordCount = 0;

    protected List<MerchantContainer> Containers = new ArrayList<MerchantContainer>();

    private FileHeader  Header  = null;
    private FileTrailer Trailer = null;

    public PTIFile(InputStream is) throws FileFormatException
    {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
        
        int errorCount = 0;
        
        @SuppressWarnings("unused")
		int BH = 0;
        @SuppressWarnings("unused")
        int BT = 0;
        @SuppressWarnings("unused")
        int D1 = 0;
        @SuppressWarnings("unused")
        int D2 = 0;
        @SuppressWarnings("unused")
        int D3 = 0;
        @SuppressWarnings("unused")
        int D4 = 0;
        @SuppressWarnings("unused")
        int D4M= 0;
        @SuppressWarnings("unused")
        int D4E= 0;
        @SuppressWarnings("unused")
        int D5 = 0;
        @SuppressWarnings("unused")
        int D5M= 0;
        @SuppressWarnings("unused")
        int DH = 0;
        @SuppressWarnings("unused")
        int DT = 0;
        @SuppressWarnings("unused")
        int FH = 0;
        @SuppressWarnings("unused")
        int FT = 0;
        @SuppressWarnings("unused")
        int GH = 0;
        @SuppressWarnings("unused")
        int GT = 0;
        @SuppressWarnings("unused")
        int M2 = 0;
        @SuppressWarnings("unused")
        int M3 = 0;
        @SuppressWarnings("unused")
        int M4 = 0;
        @SuppressWarnings("unused")
        int M5 = 0;
        @SuppressWarnings("unused")
        int MH = 0;
        @SuppressWarnings("unused")
        int MT = 0;
        @SuppressWarnings("unused")
        int P3 = 0;
        @SuppressWarnings("unused")
        int SH = 0;
        @SuppressWarnings("unused")
        int ST = 0;

        MerchantContainer currentMerchantContainer = null;

        Merchant    currentMerchant    = null;
        Batch       currentBatch       = null;
        Transaction currentTransaction = null;
        
        try {
            String inputLine = reader.readLine();
            while (inputLine != null && !inputLine.equals(""))
            {
                try {
                    char recordType[] = new char[2];
                    recordType[0] = inputLine.charAt(0);
                    recordType[1] = inputLine.charAt(1);
                    int seqNum = Integer.valueOf(inputLine.substring(2, 9));
                    if (seqNum != reader.getLineNumber())
                        System.out.println("Warning: Line/Seq number mismatch: " +
                                           seqNum + '/' + reader.getLineNumber());

                    switch (recordType[0])
                    {
                    case 'B':
                        switch (recordType[1]) {
                        case 'H':
                            BatchHeader bh = new BatchHeader(reader.getLineNumber(), inputLine);
                            if (currentBatch != null)
                                throw new FileFormatException("BH without closing previous batch");
                            if (currentMerchant == null)
                                throw new FileFormatException("BH with no active merchant");

                            currentBatch = new Batch(currentMerchant, bh);

                            BH++;
                            break;
                        case 'T':
                            BatchTrailer bt = new BatchTrailer(reader.getLineNumber(), inputLine);
                            if (currentBatch == null)
                                throw new FileFormatException("BT with no active batch");

                            currentTransaction.Close();
                            currentTransaction = null;

                            currentBatch.SetTrailer(bt);
                            currentBatch.Close();
                            currentBatch = null;

                            BT++;
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    case 'D':
                        switch (recordType[1]) {
                        case '1':
                            Detail1 d1 = new Detail1(reader.getLineNumber(), inputLine);
                            if (currentBatch == null)
                                throw new FileFormatException("D1 with no active batch");
                            if (currentTransaction != null)
                                currentTransaction.Close();
                            currentTransaction = new Transaction(currentBatch, d1);
                            D1++;
                            break;
                        case '2':
                            Detail2 d2 = new Detail2(reader.getLineNumber(), inputLine);
                            if (currentTransaction == null)
                                throw new FileFormatException("D2 with no active transaction");
                            currentTransaction.AddRecord(d2);
                            D2++;
                            break;
                        case '3':
                            Detail3 d3 = new Detail3(reader.getLineNumber(), inputLine);
                            if (currentTransaction == null)
                                throw new FileFormatException("D3 with no active transaction");
                            currentTransaction.AddRecord(d3);
                            D3++;
                            break;
                        case '4':
                            Detail4 d4 = new Detail4(reader.getLineNumber(), inputLine);
                            if (currentTransaction == null)
                                throw new FileFormatException("D4 with no active transaction");
                            if (d4.Type == Detail4.DetailType.MAILPHONE)
                            {
                                Detail4_PhoneOrd d4m = new Detail4_PhoneOrd(d4);
                                D4M++;
                                currentTransaction.AddRecord(d4m);
                            }
                            else if (d4.Type == Detail4.DetailType.ECOMMERCE)
                            {
                                Detail4_ECommerce d4e = new Detail4_ECommerce(d4);
                                D4E++;
                                currentTransaction.AddRecord(d4e);
                            }
                            else
                            {
                                D4++;
                                currentTransaction.AddRecord(d4);
                            }
                            break;
                        case '5':
                            Detail5 d5 = new Detail5(reader.getLineNumber(), inputLine);
                            if (currentTransaction == null)
                                throw new FileFormatException("D5 with no active transaction");
                            if (d5.Type == Detail5.DetailType.MISC_DATA)
                            {
                                Detail5_MiscData d5m = new Detail5_MiscData(d5);
                                D5M++;
                                currentTransaction.AddRecord(d5m);
                            }
                            else
                            {
                                D5++;
                                currentTransaction.AddRecord(d5);
                            }
                            break;
                        case 'H':
                            if (currentMerchantContainer != null)
                                throw new FileFormatException("DH with active SH/DH/GH");
                            DivisionHeader dh = new DivisionHeader(reader.getLineNumber(), inputLine);
                            currentMerchantContainer = new Division(this, dh);
                            DH++;
                            break;
                        case 'T':
                            if (currentMerchantContainer == null)
                                throw new FileFormatException("DT with no active DH");
                            if (!currentMerchantContainer.getClass().getName().equals("com.bottinifuel.chase.PTI.Division"))
                                throw new FileFormatException("DT with active SH or GH");
                            DivisionTrailer dt = new DivisionTrailer(reader.getLineNumber(), inputLine);

                            currentMerchantContainer.SetTrailer(dt);
                            currentMerchantContainer.Close();
                            currentMerchantContainer = null;
                            
                            if (currentMerchant != null)
                                throw new FileFormatException("DT with active merchant");
                            DT++;
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    case 'F':
                        switch (recordType[1]) {
                        case 'H':
                            if (Header != null)
                                throw new FileFormatException("FH with active file");
                            if (currentMerchantContainer != null)
                                throw new FileFormatException("FH with active SH/DH/GH");
                            Header = new FileHeader(reader.getLineNumber(), inputLine);
                            FH++;
                            RecordCount++;
                            if (!Header.Version.equals("4.9"))
                            {
                                System.out.println("***********************************************\n" +
                                                   "* Warning: PTI file version=" + Header.Version +
                                                   " expecting=4.9 *\n" +
                                                   "* Contact Patrick if there are any problems   *\n" +
                                                   "***********************************************\n");
                            }
                            break;
                        case 'T':
                            if (Header == null)
                                throw new FileFormatException("FT with no active file");
                            if (currentMerchantContainer != null)
                                throw new FileFormatException("FT with active SH/DH/GH");
                            Trailer = new FileTrailer(reader.getLineNumber(), inputLine);
                            FT++; 
                            RecordCount++;
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    case 'G':
                        switch (recordType[1]) {
                        case 'H':
                            if (currentMerchantContainer != null)
                                throw new FileFormatException("SH with active SH/DH/GH");
                            GroupHeader gh = new GroupHeader(reader.getLineNumber(), inputLine);
                            currentMerchantContainer = new Group(this, gh);
                            GH++;
                            break;
                        case 'T':
                            if (currentMerchantContainer == null)
                                throw new FileFormatException("GT with no active GH");
                            if (!currentMerchantContainer.getClass().getName().equals("com.bottinifuel.chase.PTI.Group"))
                                throw new FileFormatException("GT with active SH or DH");
                            GroupTrailer gt = new GroupTrailer(reader.getLineNumber(), inputLine);

                            currentMerchantContainer.SetTrailer(gt);
                            currentMerchantContainer.Close();
                            currentMerchantContainer = null;
                            
                            if (currentMerchant != null)
                                throw new FileFormatException("GT with active merchant");
                            GT++;
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    case 'M':
                        switch (recordType[1]) {
                        case '2':
                            MerchantHeader2 mh2 = new MerchantHeader2(reader.getLineNumber(), inputLine);
                            if (currentMerchant == null)
                                throw new FileFormatException("M2 with no active merchant");
                            currentMerchant.AddRecord(mh2);
                            M2++;
                            break;
                        case '3':
                            MerchantHeader3 mh3 = new MerchantHeader3(reader.getLineNumber(), inputLine);
                            if (currentMerchant == null)
                                throw new FileFormatException("M3 with no active merchant");
                            currentMerchant.AddRecord(mh3);
                            M3++;
                            break;
                        case '4':
                            MerchantHeader4 mh4 = new MerchantHeader4(reader.getLineNumber(), inputLine);
                            if (currentMerchant == null)
                                throw new FileFormatException("M4 with no active merchant");
                            currentMerchant.AddRecord(mh4);
                            M4++;
                            break;
                        case '5':
                            MerchantHeader5 mh5 = new MerchantHeader5(reader.getLineNumber(), inputLine);
                            if (currentMerchant == null)
                                throw new FileFormatException("M5 with no active merchant");
                            currentMerchant.AddRecord(mh5);
                            M5++;
                            break;
                        case 'H':
                            MerchantHeader mh = new MerchantHeader(reader.getLineNumber(), inputLine);
                            MH++;
                            if (currentMerchant != null)
                                throw new FileFormatException("MH without closing previous merchant");
                            if (currentMerchantContainer == null)
                                throw new FileFormatException("MH with no active merchant container");
                            currentMerchant = new Merchant(currentMerchantContainer, mh);
                            break;
                        case 'T':
                            MerchantTrailer mt = new MerchantTrailer(reader.getLineNumber(), inputLine);
                            if (currentMerchant == null)
                                throw new FileFormatException("MT with no active merchant");

                            currentMerchant.SetTrailer(mt);
                            currentMerchant.Close();
                            currentMerchant = null;

                            if (currentBatch != null)
                                throw new FileFormatException("MT with active batch");

                            MT++;
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    case 'P':
                        switch (recordType[1]) {
                        case '3':
                            @SuppressWarnings("unused")
							PurchCard pc = new PurchCard(reader.getLineNumber(), inputLine);
                            P3++;
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    case 'S':
                        switch (recordType[1]) {
                        case 'H':
                            if (currentMerchantContainer != null)
                                throw new FileFormatException("SH with active SH/DH/GH");
                            BankHeader sh = new BankHeader(reader.getLineNumber(), inputLine);
                            currentMerchantContainer = new Bank(this, sh);
                            SH++; 
                            break;
                        case 'T':
                            if (currentMerchantContainer == null)
                                throw new FileFormatException("ST with no active SH");
                            if (!currentMerchantContainer.getClass().getName().equals("com.bottinifuel.chase.PTI.Bank"))
                                throw new FileFormatException("ST with active GH or DH");
                            BankTrailer st = new BankTrailer(reader.getLineNumber(), inputLine);

                            currentMerchantContainer.SetTrailer(st);
                            currentMerchantContainer.Close();
                            currentMerchantContainer = null;
                            
                            if (currentMerchant != null)
                                throw new FileFormatException("ST with active merchant");
                            ST++; 
                            break;
                        default:
                            throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                        }
                        break;
                    default:
                        throw new FileFormatException("Unknown record type '" + recordType.toString() + "'");
                    }
                }
                catch (ParseException e)
                {
                    errorCount++;
                    System.out.println("Line #" + reader.getLineNumber() + ": " + e);
                }
                catch (FileFormatException e)
                {
                    errorCount++;
                    System.out.println("Line #" + reader.getLineNumber() + ": PTIFile format problem: " + e);
                }

                inputLine = reader.readLine();
            }
        }
        catch (IOException e)
        {
            errorCount++;
            System.out.println(e);
        }

        if (Trailer == null)
        {
            errorCount++;
            System.out.println("No FT seen for file starting on line #" + Header.SeqNum);
        }

        if (RecordCount != Trailer.RecordCount)
        {
            errorCount++;
            System.out.println("PTIFile record count mismatch: FT=" + Trailer.RecordCount + " Saw=" + RecordCount);
        }
        
        if (Containers.size() != Trailer.BankDivCount)
        {
            errorCount++;
            System.out.println("PTIFile container count mismatch: FT=" + Trailer.BankDivCount + " Saw=" + Containers.size());
        }
        
        int saleCount   = 0;
        int saleCents   = 0;
        int returnCount = 0;
        int returnCents = 0;
        for (MerchantContainer mc : Containers)
        {
            saleCount   += mc.getSalesCount();
            saleCents   += mc.getSalesCents();
            returnCount += mc.getReturnCount();
            returnCents += mc.getReturnCents();
        }

        if (saleCount != Trailer.SalesCount)
        {
            errorCount++;
            System.out.println("PTIFile sale count mismatch: FT=" + Trailer.SalesCount + " Saw=" + saleCount);
        }
        if (saleCents != Trailer.SalesTotal)
        {
            errorCount++;
            System.out.println("PTIFile sale total mismatch: FT=" + Trailer.SalesTotal + " Saw=" + saleCents);
        }
        if (returnCount != Trailer.ReturnCount)
        {
            errorCount++;
            System.out.println("PTIFile return count mismatch: FT=" + Trailer.ReturnCount + " Saw=" + returnCount);
        }
        if (returnCents != Trailer.ReturnTotal)
        {
            errorCount++;
            System.out.println("PTIFile return total mismatch: FT=" + Trailer.ReturnTotal + " Saw=" + returnCents);
        }
        
        if (errorCount > 0)
            throw new FileFormatException("Processing halted after errors");
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

    public int getTransCount()
    {
        return Trailer.ReturnCount + Trailer.SalesCount;
    }
    public int getTotalCents()
    {
        return Trailer.SalesTotal - Trailer.ReturnTotal;
    }
    public double getTotalAmount()
    {
        double temp = getTotalCents();
        return temp / 100;
    }
    
    
    protected void IncrRecordCount(int rc)
    {
        RecordCount += rc;
    }

    protected void AddMerchantContainer(MerchantContainer c)
    {
        Containers.add(c);
    }

    public List<Transaction> getAllTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (MerchantContainer m : Containers)
        {
            rc.addAll(m.getAllTransactions());
        }

        return rc;
    }

    public List<Transaction> getTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (MerchantContainer m : Containers)
        {
            rc.addAll(m.getTransactions());
        }

        return rc;
    }

    public List<Transaction> getReturnTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (MerchantContainer m : Containers)
        {
            rc.addAll(m.getCreditTransactions());
        }

        return rc;
    }

    public List<Transaction> getSalesTransactions()
    {
        List<Transaction> rc = new ArrayList<Transaction>();

        for (MerchantContainer m : Containers)
        {
            rc.addAll(m.getSalesTransactions());
        }

        return rc;
    }

    public List<MerchantContainer> getContainers()
    {
        return Containers;
    }

    public Date getDate()
    {
        return Header.CreateDate;
    }
    
    static public void main(String [] args)
    {
        try {
//            PTIFile f1 = new PTIFile(new FileInputStream("x:/wappingers/data/Patrick/Credit Cards/Chase/PTIS0250.txt"));
            PTIFile f1 = new PTIFile(new FileInputStream("C:\\Documents and Settings\\administrator\\Desktop\\PTIS0250.txt"));
//            PTIFile f2 = new PTIFile(new FileInputStream("C:\\Documents and Settings\\administrator\\Desktop\\PTIS0250.txt"));
            
            List<Transaction> c1 = f1.getReturnTransactions();
            List<Transaction> s1 = f1.getSalesTransactions();
            
            @SuppressWarnings("unused")
			int sc1 = c1.size();
            @SuppressWarnings("unused")
			int rc1 = s1.size();
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        catch (FileFormatException e)
        {
            System.out.println(e);
        }
    }
}
