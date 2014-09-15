/*
 * Created on Apr 25, 2007 by pladd
 *
 */
package com.bottinifuel.chase.PTI;

/**
 * @author pladd
 *
 */
public class TransCounter
{
    private int visaCents  = 0;
    private int mastCents  = 0;
    private int amexCents  = 0;
    private int discCents  = 0;
    private int otherCents = 0;

    private int visaCnt  = 0;
    private int mastCnt  = 0;
    private int amexCnt  = 0;
    private int discCnt  = 0;
    private int otherCnt = 0;
    
    public int VisaCents () { return visaCents;  }
    public int MastCents () { return mastCents;  }
    public int AmexCents () { return amexCents;  }
    public int DiscCents () { return discCents;  }
    public int OtherCents() { return otherCents; }
    public int VSMCCents () { return visaCents + mastCents; }
    public int TotalCents() { return visaCents + mastCents + amexCents + discCents + otherCents; }
    
    public double VisaAmount () { double temp = visaCents;  return temp / 100; }
    public double MastAmount () { double temp = mastCents;  return temp / 100; }
    public double AmexAmount () { double temp = amexCents;  return temp / 100; }
    public double DiscAmount () { double temp = discCents;  return temp / 100; }
    public double OtherAmount() { double temp = otherCents; return temp / 100; }
    public double VSMCAmount () { double temp = visaCents + mastCents; return temp / 100; }
    public double TotalAmount() { double temp = visaCents + mastCents + amexCents + discCents + otherCents; return temp /100; }

    public int VisaCount () { return visaCnt;  }
    public int MastCount () { return mastCnt;  }
    public int AmexCount () { return amexCnt;  }
    public int DiscCount () { return discCnt;  }
    public int OtherCount() { return otherCnt; }
    public int VSMCCount () { return visaCnt + mastCnt; }
    public int TotalCount() { return visaCnt + mastCnt + amexCnt + discCnt + otherCnt; }

    public void AddVisa (int cents) { visaCnt ++; visaCents  += cents; }
    public void AddMast (int cents) { mastCnt ++; mastCents  += cents; }
    public void AddAmex (int cents) { amexCnt ++; amexCents  += cents; }
    public void AddDisc (int cents) { discCnt ++; discCents  += cents; }
    public void AddOther(int cents) { otherCnt++; otherCents += cents; }

    public void AddTrans(Transaction t)
    {
        int cents;
        if (t.isReturn())
            cents = -t.Cents();
        else
            cents = t.Cents();

        switch (t.Card)
        {
        case VISA:       AddVisa (cents); break;
        case MASTERCARD: AddMast (cents); break;
        case AMEX:       AddAmex (cents); break;
        case DISCOVER:   AddDisc (cents); break;
        default:         AddOther(cents); break;            
        }
    }
}
