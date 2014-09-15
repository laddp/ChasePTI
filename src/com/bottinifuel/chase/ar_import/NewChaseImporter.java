/*
 * Created on Mar 27, 2007 by pladd
 *
 */
package com.bottinifuel.chase.ar_import;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.bottinifuel.chase.PTI.FileFormatException;
import com.bottinifuel.chase.PTI.PTIFile;
import com.bottinifuel.chase.PTI.TransCounter;
import com.bottinifuel.chase.PTI.Transaction;
import com.bottinifuel.chase.PTI.records.Detail4_ECommerce;
import com.bottinifuel.chase.PTI.records.Detail4_PhoneOrd;
import com.bottinifuel.energy.bankpost.BankpostFile;
import com.bottinifuel.energy.bankpost.LineItem;


/**
 * @author pladd
 *
 */
public class NewChaseImporter
{
    private static String MsgHeader =
        // 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 
          "Reason TransID========= Amount====== Type== Card= Date============= Order#=== Comment";
    private static NumberFormat CF = NumberFormat.getCurrencyInstance();
    
    private NewChaseImporter(int db, File bankpost,
                          List<InputStream> ptiStreams)
    {
        int errorCount = 0;

        ArrayList<PTIFile> ptiFiles = new ArrayList<PTIFile>(ptiStreams.size());        

        for (InputStream ptiStream : ptiStreams)
        {
            try {
                PTIFile f = new PTIFile(ptiStream);
                ptiFiles.add(f);
            }
            catch (FileFormatException e)
            {
                System.out.println(e);
                errorCount++;
            }
        }

        if (errorCount > 0)
        {
            System.out.println("Error: Halting after PTI file errors");
            return;
        }

        if (ptiFiles.size() < 1)
        {
            System.out.println("Error: no PTI files to process further. Halting.");
            return;
        }

        BankpostFile bp;
        FileOutputStream bpOut = null;
        try {
            if (bankpost.exists())
            {
                bp = new BankpostFile(db, new FileInputStream(bankpost));
                System.out.println("==============================================================");
                System.out.println("Appending to existing BANKPOST file: " + bankpost);
                System.out.printf ("Type   %6s   %15s\n", "Trans", "Amt");
                System.out.printf ("VISA   %6d   %15s\n",   bp.getVisaCount(),   CF.format(bp.getVisaAmount())); 
                System.out.printf ("MAST   %6d   %15s\n",   bp.getMastCount(),   CF.format(bp.getMastAmount())); 
                System.out.printf ("AMEX   %6d   %15s\n",   bp.getAmexCount(),   CF.format(bp.getAmexAmount())); 
                System.out.printf ("DISC   %6d   %15s\n",   bp.getDiscCount(),   CF.format(bp.getDiscAmount())); 
                System.out.printf ("Total  %6d   %15s\n\n", bp.getRecordCount(), CF.format(bp.getTotalAmount())); 
            }
            else
            {
                bp = new BankpostFile(db, new Date());
                bpOut = new FileOutputStream(bankpost);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Bankpost file not found: " + bankpost);
            return;
        }
        catch (com.bottinifuel.energy.bankpost.FileFormatException e)
        {
            System.out.println(e);
            System.out.println("Error processing BANKPOST file. Exiting.");
            return;
        }

        TransCounter bpTrans      = new TransCounter();
        TransCounter problemTrans = new TransCounter();
        TransCounter refundTrans  = new TransCounter();
        
        
        
        for (PTIFile pti : ptiFiles)
        {
            boolean msgHeaderPrinted = false;
            TransCounter netTrans = new TransCounter();

            for (Transaction t : pti.getReturnTransactions())
            {
//            	if (t.BatchParent.MerchantParent.getMerchantID().equals("424342551886") ||
//                		t.BatchParent.MerchantParent.getMerchantID().equals("424342786888"))
                		System.out.println(t.Cents() + "," + t.Card + "," + t.getEComm().OrderNum + "," + t.TransCode);
            }
            
            for (Transaction t : pti.getSalesTransactions())
            {
//            	if (t.BatchParent.MerchantParent.getMerchantID().equals("424342551886") ||
//            		t.BatchParent.MerchantParent.getMerchantID().equals("424342786888"))
//            	{
            		System.out.println(t.Cents() + "," + t.Card + "," + t.getEComm().OrderNum + "," + t.TransCode);
//            		continue;
//            	}
            	
                netTrans.AddTrans(t);
                try {
//                    Detail5_MiscData misc = t.getMiscData();

                    Detail4_PhoneOrd phone = t.getPhoneOrd(); 
                    Detail4_ECommerce ecom = t.getEComm();
                    
                    if (phone != null && ecom != null)
                        throw new Exception("PH&EC!");

                    int acct;
                    if (phone != null)
                        acct = Integer.valueOf(phone.OrderNum);
                    else if (ecom != null)
                        acct = Integer.valueOf(ecom.OrderNum);
                    else
                        throw new Exception("NORDER");
                    
                    if (acct > 9999999)
                        throw new Exception("BADACT");

                    switch (t.Card)
                    {
                    case VISA:
                    case MASTERCARD:
                    case AMEX:
                    case DISCOVER:
                        break;
                    default:
                        throw new Exception("CTYPE");
                    }

                    LineItem li = new LineItem(db, acct, t.Cents(), t.Card.PostingCode);
                    bp.addItem(li);
                    bpTrans.AddTrans(t);
                }
                catch (NumberFormatException e)
                {
                    problemTrans.AddTrans(t);
                    if (!msgHeaderPrinted)
                    {
                        System.out.println(MsgHeader);
                        msgHeaderPrinted = true;
                    }
                    PrintTrans("BADACT", t);
                }
                catch (Exception e)
                {
                    problemTrans.AddTrans(t);
                    if (!msgHeaderPrinted)
                    {
                        System.out.println(MsgHeader);
                        msgHeaderPrinted = true;
                    }
                    PrintTrans(e.getMessage(), t);
                }
            }
            
            for (Transaction t : pti.getReturnTransactions())
            {
                netTrans.AddTrans(t);
                refundTrans.AddTrans(t);
                if (!msgHeaderPrinted)
                {
                    System.out.println(MsgHeader);
                    msgHeaderPrinted = true;
                }
                PrintTrans("CREDIT", t);
            }
            
            System.out.println("==============================================================");
            System.out.println("Processed PTI dated: " + pti.getDate());
            System.out.printf ("Type   %6s   %15s\n", "Trans", "Amt");
            System.out.printf ("VS/MC  %6d   %15s\n",   netTrans.VSMCCount(), CF.format(netTrans.VSMCAmount())); 
            System.out.printf ("AMEX   %6d   %15s\n",   netTrans.AmexCount(), CF.format(netTrans.AmexAmount())); 
            System.out.printf ("DISC   %6d   %15s\n",   netTrans.DiscCount(), CF.format(netTrans.DiscAmount())); 
            System.out.printf ("Total  %6d   %15s\n\n", pti.getTransCount(),  CF.format(pti.getTotalAmount())); 
        }

        try {
            if (bp.getRecordCount() < 1)
            {
                System.out.println("Error: no transactions to process. Exiting.");
                return;
            }

            if (bankpost.exists())
            {
                String fn = bankpost.getAbsolutePath();
                File backup = new File(fn + ".prev");
                bankpost.renameTo(backup);
                bpOut = new FileOutputStream(bankpost);
            }
            bp.writeToFile(bpOut);
            System.out.println("==============================================================");
            System.out.println("BANKPOST file written");
            System.out.printf ("Type   %6s   %15s\n", "Trans", "Amt");
            System.out.printf ("VISA   %6d   %15s\n",   bp.getVisaCount(),   CF.format(bp.getVisaAmount())); 
            System.out.printf ("MAST   %6d   %15s\n",   bp.getMastCount(),   CF.format(bp.getMastAmount()));
            System.out.printf ("AMEX   %6d   %15s\n",   bp.getAmexCount(),   CF.format(bp.getAmexAmount()));
            System.out.printf ("DISC   %6d   %15s\n",   bp.getDiscCount(),   CF.format(bp.getDiscAmount()));
            System.out.printf ("Total  %6d   %15s\n\n", bp.getRecordCount(), CF.format(bp.getTotalAmount())); 

            System.out.println("==============================================================");
            System.out.println("PROBLEM transactions");
            System.out.printf ("Type   %6s   %15s\n", "Trans", "Amt");
            System.out.printf ("VISA   %6d   %15s\n",   problemTrans.VisaCount(),  CF.format(problemTrans.VisaAmount ())); 
            System.out.printf ("MAST   %6d   %15s\n",   problemTrans.MastCount(),  CF.format(problemTrans.MastAmount()));
            System.out.printf ("AMEX   %6d   %15s\n",   problemTrans.AmexCount(),  CF.format(problemTrans.AmexAmount()));
            System.out.printf ("DISC   %6d   %15s\n",   problemTrans.DiscCount(),  CF.format(problemTrans.DiscAmount()));
            System.out.printf ("Total  %6d   %15s\n\n", problemTrans.TotalCount(), CF.format(problemTrans.TotalAmount())); 

            System.out.println("==============================================================");
            System.out.println("REFUND transactions");
            System.out.printf ("Type   %6s   %15s\n", "Trans", "Amt");
            System.out.printf ("VISA   %6d   %15s\n",   refundTrans.VisaCount(),  CF.format(refundTrans.VisaAmount ())); 
            System.out.printf ("MAST   %6d   %15s\n",   refundTrans.MastCount(),  CF.format(refundTrans.MastAmount()));
            System.out.printf ("AMEX   %6d   %15s\n",   refundTrans.AmexCount(),  CF.format(refundTrans.AmexAmount()));
            System.out.printf ("DISC   %6d   %15s\n",   refundTrans.DiscCount(),  CF.format(refundTrans.DiscAmount()));
            System.out.printf ("Total  %6d   %15s\n\n", refundTrans.TotalCount(), CF.format(refundTrans.TotalAmount())); 
        }
        catch (IOException e)
        {
            System.out.println("Error writing to output file: " + e);
            return;
        }
    }
    
    private void PrintTrans(String msg, Transaction t)
    {
    	System.out.printf("%6s %15s %13s %6s %5s %tD %tT %9s %20s\n",
    		msg, t.TransID(), CF.format(t.Amount()),
      		t.TransCode, t.Card.Abbrev, t.Date(), t.Date(),
      		(t.getPhoneOrd()==null)? "" : t.getPhoneOrd().OrderNum,
      		(t.getMiscData()==null)? "" : t.getMiscData().CustData);
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // Version history
        // v1.3 : Added support for D4 070 ECOMMERCE record for acct #
        // v1.4 : Support v4.1 files, made version mismatch warning bigger
        // v1.5 : Support v4.7.1, blanks in D3 DeviceID & PumpID fields
        // v1.6 : Lose fixedwidthfield dependency
        System.out.println("Chase PTI to ADD Energy BANKPOST translator v1.6");
        System.out.println("Copyright (c) Patrick Ladd, Bottini Fuel");
        
        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help")))
        {
            Usage();
            return;
        }
        
        if (args.length < 3)
        {
            System.out.println("Error: Not enough arguments.");
            Usage();
            return;
        }

        int db;
        try {
         db = Integer.valueOf(args[0]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid DB number: " + args[0]);
            return;
        }

        File bankpost = new File(args[1]);

        int errorCount = 0;
        ArrayList<InputStream> ptiNames =
            new ArrayList<InputStream>(args.length - 2);

        List<String> ptiFileNames = Arrays.asList(args);
        ptiFileNames = ptiFileNames.subList(2, ptiFileNames.size());
        
        for (String ptiFileName : ptiFileNames)
        {
            try {
                ptiNames.add(new FileInputStream(ptiFileName));
            }
            catch (FileNotFoundException e)
            {
                System.out.println(e);
                errorCount++;
            }
        }

        if (errorCount > 0)
        {
            System.out.println("Error: Halting after PTI file errors");
            return;
        }
        
        @SuppressWarnings("unused")
		NewChaseImporter ci = new NewChaseImporter(db, bankpost, ptiNames);
    }

    private static void Usage()
    {
        System.out.println("ChaseImporter $DB BANKPOST.asc DL1.txt [DL2.txt]...");
        System.out.println("ChaseImporter [-h | --help]  - displays this help text");
    }
}
