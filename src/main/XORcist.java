/*
Christopher Sutton
5/10/2019
CSC201 final project
XORcist
Program is responsible for using bitwise XORing to:
A) take a collection of IP addresses and return a valid supernet cidr notation for subnet
B) encrypt or decrypt any given text or file through use of bitwise-XORing

PSEUDOCODE:

MAIN METHOD:
display welcome message
display menu options to user
switch choice
case 0:
    prompt user for encryptor or decryptor
    switch choice (nested switch)
        case 0:
            create new XOREncryptor object
            prompt user for input text
            store input text
            display input text in binary

            encryptor.encrypt(input text)
            call writeKeysToFile method
            call bitwise compare method
            prompt user to contniue or not
            if continue
                repeat whole program
            if quit
                exit program

        case 1:
            create new XORDecryptor object
            call readKeysFromFile method
            call decrypt method with (readCipherTextFromFile as parameter
            prompt user to contniue or not
            if continue
                repeat whole program
            if quit
                exit program

case 1:
    create new Networking Tool object
    call populateIPTableManually method
    call printIPsVertical method
    call IPtoBinary method
    call bitwise compare method
    prompt user to contniue or not
        if continue
            repeat whole program
        if quit
            exit program
 */
package main;
import netTools.NetworkingTool;
import cryptoTools.CryptographyTool.*;
import java.util.*;
import java.io.*;//TODO make reading a list of IPs from txt file available (will add later)
public class XORcist{
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
                                System.out.println("\""+inputText+"\""+" in Binary is:\n"+encryptor.inputTextToBinary(inputText));
                                String original = new String(encryptor.inputTextToBinary(inputText));
                                encryptor.encrypt(original);
                                encryptor.writeKeysToFile();
                                encryptor.bitwiseCompare();
                                System.out.println("\nPress 0 to continue or 1 to Quit");
                                switch(kbd.nextInt()){
                                    case 0:
                                        break;
                                    case 1:{
                                        System.out.println("Goodbye!");
                                        repeat = false;
                                        break;
                                        }
                                    }
                                break;
                                
                            case 1:
                                kbd.nextLine();
                                XORDecryptor decryptor = new XORDecryptor();
                                decryptor.readKeysFromFile();
                                decryptor.decrypt(decryptor.readCipherTextFromFile());
                                System.out.println("\nPress 0 to continue or 1 to Quit");
                                switch(kbd.nextInt()){
                                    case 0:
                                        break;
                                    case 1:{
                                        System.out.println("Goodbye!");
                                        repeat = false;
                                        break;
                                    }
                                }
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
                        supernetter.printIPsVertical(supernetter);
                        supernetter.IPtoBinary();
                        supernetter.bitwiseCompare();
                        System.out.println("\nPress 0 to continue or 1 to Quit");
                                switch(kbd.nextInt()){
                                    case 0:
                                        break;
                                    case 1:{
                                        System.out.println("Goodbye!");
                                        repeat = false;
                                        break;
                                    }   
                                }
                        break;
                    }
                    default:
                        System.out.println("Choice not recognized.");
                }
            }catch(InputMismatchException ime){
                System.out.println("Invalid Input");
                kbd.nextLine();
            }
        }while(repeat);
    }
}
/*
Sample Output:

Welcome to XORcist.
Press 0 to use Cryptography Tools.
Press 1 to use Networking Tools.
1
(Enter Q to quit)
Enter an IP address with CIDR notation: 
192.168.1.1/24
(Enter Q to quit)
Enter an IP address with CIDR notation: 
192.168.2.1/24
(Enter Q to quit)
Enter an IP address with CIDR notation: 
192.168.3.1/24
(Enter Q to quit)
Enter an IP address with CIDR notation: 
q

You entered the IP addresses:
192.168.1.1/24
192.168.2.1/24
192.168.3.1/24

IP Table:
192.168.1.1
192.168.2.1
192.168.3.1
11000000 10101000 00000001 00000001 
11000000 10101000 00000010 00000001 
11000000 10101000 00000011 00000001 

Full Binary IP Table: 
11000000101010000000000100000001 
11000000101010000000001000000001 
11000000101010000000001100000001 

Supernet Network Bits in Binary:
1100000010101000000000
Supernet CIDR notation: /22

Full Binary Supernet IP:
11000000101010000000000000000000

Supernet IP address: 
192.168.0.0/22
Supernet Subnet Mask:
255.255.252.0

Press 0 to continue or 1 to Quit
0
Welcome to XORcist.
Press 0 to use Cryptography Tools.
Press 1 to use Networking Tools.
0
Would you like to use the Encryptor or Decryptor: 
Press 0 for Encryptor.
Press 1 for Decryptor.
0
Enter the text to be encrypted: 
Hello!
"Hello!" in Binary is:
010010000110010101101100011011000110111100100001
The keys used to encrypt are: ((11) keys used)
000011110110101111110101001100000001101101001000
100011001000110100001101011000111110101000110000
101101011011000011110011101100110101010110100011
110100110100110010110001101010011110111100100101
100001011000011010001010010100010111111011011000
100100000000111101101011101000100100011101100000
100110010000000101111100001000001110010001100011
000011001001001001100101010110001000101100011001
001100011110101100111000001011010011011110111101
010011011110001010111010100001000100001101011010
101001100101110010001111001011111000010001100101
All data written successfully.
Ciphertext is: AAEAAAABAQEAAAAAAQEBAAEAAAEBAAABAAEAAQEBAAAAAQEBAAEAAAABAQABAAAB

Press 0 to continue or 1 to Quit
0
Welcome to XORcist.
Press 0 to use Cryptography Tools.
Press 1 to use Networking Tools.
0
Would you like to use the Encryptor or Decryptor: 
Press 0 for Encryptor.
Press 1 for Decryptor.
1
All data read from file successfully.

Cleartext in binary is:
010010000110010101101100011011000110111100100001

Cleartext is: 
Hello!

Press 0 to continue or 1 to Quit
1
Goodbye!

*/
