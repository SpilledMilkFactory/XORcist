/*
Christopher Sutton
3/26/2019
CSC201 final project
XORcist
Program is responsible for using bitwise XORing to:
A) take a collection of IP addresses and return a valid supernet cidr notation for subnet
B) encrypt or decrypt any given text or file through use of bitwise-XORing
 */
package main;
import netTools.NetworkingTool;
import cryptoTools.CryptographyTool.*;
import java.util.*;
import java.io.*;//TODO make reading a list of IPs from txt file available
public class XORcist{//program name
    public static Scanner kbd = new Scanner(System.in);
    
    public static void main(String[] args)throws InputMismatchException, IOException{
        boolean repeat = true;
        do{
            try{
                System.out.println("Welcome to XORcist.");

                System.out.println("Press 0 to use Cryptography Tools.");
                System.out.println("Press 1 to use Networking Tools.");
                switch(kbd.nextInt()){
                    case 0:{
                        System.out.println("Would you like to use the Encryptor or Decryptor: ");
                        System.out.println("Press 0 for Encryptor.");
                        System.out.println("Press 1 for Decryptor.");
                        switch(kbd.nextInt()){
                            case 0:
                                kbd.nextLine();
                                XOREncryptor encryptor = new XOREncryptor();
                                System.out.println("Enter the text to be encrypted: ");
                                String inputText = kbd.nextLine();
                                System.out.println(inputText+" in Binary is:");
                                System.out.println(encryptor.inputTextToBinary(inputText));
                                String original = new String(encryptor.inputTextToBinary(inputText));
                                encryptor.encrypt(original);
                                encryptor.writeKeysToFile();
                                encryptor.bitwiseCompare();
                                
                                repeat = false;
                                break;
                            case 1:
                                kbd.nextLine();
                                XORDecryptor decryptor = new XORDecryptor();
                                decryptor.readKeysFromFile();
                                decryptor.decrypt(decryptor.readCipherTextFromFile());
                                repeat = false;
                                break;
                                
                            default:
                                System.out.println("Choice not recognized.");
                        }
                        break;
                    }
                    case 1:{
                        kbd.nextLine();
                        NetworkingTool supernetter = new NetworkingTool();
                        supernetter.populateIPTableManually();
                        printIPsVertical(supernetter);
                        supernetter.IPtoBinary();
                        supernetter.bitwiseCompare();
                        repeat = false;
                        break;
                    }
                    default:
                        System.out.println("Choice not recognized.");
                }
            }catch(InputMismatchException ime){
                System.out.println("Invalid Input");
            }
        }while(repeat != false);
        
        
        
        
        
        
        
        
        
        
    }


    //Checks full ArrayLists for a specified string
    public static boolean containsIgnoreCase(String sentinel, ArrayList<String> list){
        for(String s : list){
            if (s.equalsIgnoreCase(sentinel)){
                return true;
            }
        }
        return false;
    }
    //prints out a vertical list of IP addresses
    public static void printIPsVertical(NetworkingTool tool){
        System.out.println("\nYou entered the IP addresses:");
        for(String ip : tool.ipList){
            System.out.println(ip);
        }
        
    }
    
    
    
}
/*

        System.out.print("Enter your email address: ");
        String emailAddy = kbd.nextLine();
        
        int firstIndex = emailAddy.indexOf('@');
        int secondIndex = emailAddy.indexOf(".",firstIndex+1);
        int lastIndex = emailAddy.indexOf(secondIndex+1);
        
        String myUserName = emailAddy.substring(0, firstIndex);
        String myHostName = emailAddy.substring(firstIndex+1, secondIndex);         
        String myExtension = emailAddy.substring(secondIndex+1);
        
        
        System.out.println("The username is: "+myUserName);
        System.out.println("The hostname is: "+myHostName);
        System.out.println("The extension is: "+myExtension);
*/