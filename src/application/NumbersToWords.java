package application;

public class NumbersToWords {
    
   /* String unitsMap[] = { "zero", "one", "two", "three", "four", "five","six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };
    String tensMap[] = { "zero", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };

   public String convert(int number)
    {
     if (number == 0)
        return "zero";

    if (number < 0)
        return "minus " + convert(-number);

    String words = "";

    if ((number / 1000000000) > 0)
    {
        words += convert(number / 1000000000) + " billion ";
        number %= 1000000000;
    }

    if ((number / 1000000) > 0)
    {
        words += convert(number / 1000000) + " million ";
        number %= 1000000;
    }

    if ((number / 1000) > 0)
    {
        words += convert(number / 1000) + " thousand ";
        number %= 1000;
    }

    if ((number / 100) > 0)
    {
        words += convert(number / 100) + " hundred ";
        number %= 100;
    }

    if (number > 0)
    {
        if (number < 20)
            words += unitsMap[number];
        else
        {
            words += tensMap[number / 10];
            if ((number % 10) > 0)
                words += "-" + unitsMap[number % 10];
        }
    }

    return words;
     }
    */
   
	String[] units = { "", "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
            "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
            "Eighteen", "Nineteen" };

     String[] tens = { 
            "",         // 0
            "",     // 1
            "Twenty",   // 2
            "Thirty",   // 3
            "Forty",    // 4
            "Fifty",    // 5
            "Sixty",    // 6
            "Seventy",  // 7
            "Eighty",   // 8
            "Ninety"    // 9
    };

    public String convert( int n) {
        if (n < 0) {
            return "minus " + convert(-n);
        }

        if (n < 20) {
            return units[n];
        }

        if (n < 100) {
            return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
        }

        if (n < 1000) {
            return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
        }

        if (n < 1000000) {
            return convert(n / 1000) + " Thousand" + ((n % 1000 != 0) ? " " : "") + convert(n % 1000);
        }

        if (n < 1000000000) {
            return convert(n / 1000000) + " Million" + ((n % 1000000 != 0) ? " " : "") + convert(n % 1000000);
        }

        return convert(n / 1000000000) + " Billion"  + ((n % 1000000000 != 0) ? " " : "") + convert(n % 1000000000);
    }

   /* public static void main(final String[] args) {

        int n;

        n = 5;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 16;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 50;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 78;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 456;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 1000;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 99999;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 199099;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");

        n = 110005000;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");
        
        n = 1010005054;
        System.out.println(NumberFormat.getInstance().format(n) + "='" + convert(n) + "'");
    }
    
*/
}
