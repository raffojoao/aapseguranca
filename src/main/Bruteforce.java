package main;


import java.util.*;
import java.security.*;
public class Bruteforce
{   
    static String answer="";
    public static void main(String[] args)
    {
        Scanner in=new Scanner(System.in);
        char ar[]={ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
                '4', '5', '6', '7', '8', '9','`','~','!','@','#','$','%','^','&','*','(',')','-','_','=','+',
                '|','{','}','[',']',';',':',',','<','.','>','/','?'};
        String enc;
        System.out.println("Please don't enter anything that is not MD5 encrypted");
        System.out.print("Enter MD5 encrypted text : ");
        enc=in.nextLine();
        //HERE, 20 denotes the maximum wordlength 20
        final int MAX_WORDLENGTH = 4;//YOU JUST NEED TO CHANGE THIS TO MODIFY THE MAXIMUM WORDLENGTH
        for(int wordlength = 1; wordlength <= MAX_WORDLENGTH; wordlength++)
        {
            if(generate(wordlength,ar,enc))
            {
                System.out.print("Match found!! The decrypted string is : "+ answer);
                break;
            }
            else
            {
                System.out.println("Not a word of "+wordlength+" characters");
            }
        }
    }
    private static boolean generate(int wordlength, char[] alphabet,String enc)
    {
        final long MAX_WORDS = (long) Math.pow(alphabet.length, wordlength);
        final int RADIX = alphabet.length;
        for (long i = 0; i < MAX_WORDS; i++)
        {
            int[] indices = convertToRadix(RADIX, i, wordlength);
            char[] word = new char[wordlength];
            for (int k = 0; k < wordlength; k++)
            {
                word[k] = alphabet[indices[k]];
            }
            String ss=new String(word);
            if(compareit(encrypt(ss),enc))
            {
                answer=ss;
                return true;
            }
        }
        return false;
    }
    private static int[] convertToRadix(int radix, long number, int wordlength)
    {
        int[] indices = new int[wordlength];
        for (int i = wordlength - 1; i >= 0; i--)
        {
            if (number > 0)
            {
                int rest = (int) (number % radix);
                number /= radix;
                indices[i] = rest;
            }
            else
            {
                indices[i] = 0;
            }

        }
        return indices;
    }
    public static String encrypt(String str)
    {
            byte[] defaultBytes = str.getBytes();
            try
            {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(defaultBytes);
                byte messageDigest[] = algorithm.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++)
                {
                    hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                }
                str = hexString + "";
            } catch(NoSuchAlgorithmException e)
              {
                  e.printStackTrace();
              }
            return str;
    }
    public static boolean compareit(String s2, String s1)
    {
        String a=s1;
        if(s1.contains(s2))
            return true;
        else
        {
            /*Java often misses out some zeroes while encrypting text, so here
             * I'm removing zeroes one by one from the original string and then
             * performing the check again*/
            while(a.indexOf('0')!=-1)
            {
                a=a.substring(0,a.indexOf('0'))+a.substring(a.indexOf('0')+1,a.length());
                if(a.contains(s2))
                    return true;
            }
        }
        return false;
    }
}