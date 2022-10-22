public String decimalToBinary(String value)
{
    // get the integer part of the given number
    int intPart = Integer.parseInt(value.split("\\.")[0]);
    
    // convert the integer part in binary
    String intPartBin = Integer.toBinaryString(intPart).toString();

    // check if the given number is a decimal number
    if(value.contains("."))
    {
        // then extract the decimal part
        double decPart = Double.parseDouble("0." + value.split("\\.")[1]);
        String decPartBin = "";

        // convert the decimal part in binary (up to five decimals)
        for(int i=0; i<5; i++)
        {
            if(decPart * 2 >= 1)
            {
                decPartBin += "1";
                decPart *= 2;
                decPart -= 1;
            }
            else
            {
                decPartBin += "0";
                decPart *= 2;
            }
        }
        
        // return the complete number converted in binary
        return intPartBin + "." + decPartBin;
    }
    else
    {
        // else return only the integer part converted in binary
        return intPartBin;
    }
}