package com.mindblots.ohipswipe;

public class HealthCard {
    public char StartSentinel;
    public char FormatCode;
    public String IssuerIdentification;
    public String HealthNumber;
    public char FieldSeparator1;
    public String Name;
    public char FieldSeparator2;
    public String ExpiryDate;
    public int InterchangeCode;
    public int ServiceCode;
    public String Sex;
    public String DateOfBirth;
    public String CardVersionNumber;
    public String FirstNameShort;
    public String IssueDate;
    public String LanguagePreference;
    public char EndSentinel;
    public String LongitudinalRedundancyCheck_Parity;

    //public Address address;
    public boolean valid;

    public HealthCard Read(String input) throws Exception {

        HealthCard ohip = new HealthCard();
        int length = input.length();
        char[] strip = new char[length];
        strip = input.toCharArray();

        ohip.StartSentinel = strip[0];
        ohip.FormatCode = strip[1];
        ohip.IssuerIdentification = input.substring(2, 6);
        ohip.HealthNumber = input.substring(8, 18);
        ohip.valid = Validation(ohip.HealthNumber);
        ohip.FieldSeparator1 = strip[18];
        ohip.Name = input.substring(19, 44);

        ohip.FieldSeparator2 = strip[45];
        String expDt = input.substring(46, 50);
        String year = expDt.substring(0, 2);
        year = ((Integer.parseInt(year)) > 0 ? 2000 + Integer.parseInt(year)
                : 1900 + Integer.parseInt(year)) + "";
        expDt = year + "-" + expDt.substring(2, 4);
        ohip.InterchangeCode = Integer.parseInt(strip[50] + "");
        ohip.ServiceCode = Integer.parseInt(input.substring(51, 52));
        //Log.i("HealthCardReader", strip[53] + "");
        ohip.Sex = strip[53] == '1' ? "M" : "F";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        year = input.substring(54, 58);
        String month = input.substring(58, 60);

        String day = input.substring(60, 62);

        ohip.DateOfBirth = year + "-" + month + "-" + day;
        year = ((Integer.parseInt(year)) > 0 ? 2000 + Integer.parseInt(year)
                : 1900 + Integer.parseInt(year)) + "";
        ohip.ExpiryDate = expDt + "-" + day;
        // ohip.ExpiryDate = (Date) formatter.parse(expDt + "-" + day);

        ohip.CardVersionNumber = input.substring(62, 64);
        ohip.FirstNameShort = input.substring(64, 68);

        year = input.substring(69, 71);
        month = input.substring(71, 73);
        day = input.substring(73, 75);
        year = ((Integer.parseInt(year)) > 0 ? 2000 + Integer.parseInt(year)
                : 1900 + Integer.parseInt(year)) + "";
        ohip.IssueDate = year + "-" + month + "-" + day;
        ohip.LanguagePreference = input.substring(75, 76).equals("01") ? "EN"
                : "FR";
        ohip.EndSentinel = strip[77];

        return ohip;
    }

    public boolean Validation(String hc) {
        //Log.i("HEALTCARD", "VALIDATION START -" + hc);
        char carry[] = hc.toCharArray();
        int iarry[] = new int[carry.length];
        String nstr = "";

        for (int i = 0; i < 9; i++) {
            iarry[i] = carry[i] - '0';

            if (i % 2 == 0) {

                //Log.i("HEALTCARD", "a[" + i + "] = " + iarry[i] + " " + nstr);
                String ni = (iarry[i] * 2) + "";
                //System.out.println(ni);
                if (ni.length() > 1) {

                    Integer result = addAll(ni);
                    nstr += result;

                    //Log.i("HEALTCARD", "sum of " + result);
                } else {
                    nstr += ni;
                }
            } else {
                nstr += iarry[i] + "";
            }
        }
        // System.out.println("HC : "+hc+ " Mode10 " +nstr);
        String sumOfAllDigits = addAll(nstr) + "";
        //System.out.println("Sum of all "+sumOfAllDigits);
        //Log.i("HEALTCARD", nstr + " all digit sum = " + sumOfAllDigits);
        // Get last digit of sum
        int lastdigit = sumOfAllDigits.charAt(1) - '0';
        // subtract unit position from 10
        int sub = 10 - lastdigit;
        //Log.i("HEALTCARD", "VALIDATION END");
        return sub == (carry[9] - '0') ? true : false;

    }

    public static int addAll(String str) {
        str = str.replaceAll("[^1-9]", "");
        if (str.length() == 0)
            return 0;
        char[] c = str.toCharArray();
        Integer result = c[0] - 48;
        // while (c.length > 1)
        {
            result = 0;
            for (int i = 0; i < c.length; i++) {
                result += c[i] - 48;
            }
            c = result.toString().toCharArray();
        }
        return result;
    }
}
