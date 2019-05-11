/*
Christopher Sutton
5/10/2019
CSC201 final project
Tool inside XORcist program
Tool is responsible for using bitwise XORing to accompblish:
Function B) encrypt or decrypt given text or file through use of bitwise-XORing

PSEUDOCODE:

CRYPTOGRAPHY TOOL CLASS extends XORcist:
String keys[]
create file object reference for keys file
create file object reference for cipherText

XOR BYTES(text,key(s)) METHOD:
create byte array as long as the text string
for (length of text)
convert each character to bytes and XOR them using the ^ operator
add the XOR values to a new array
return array of XOR values


XOR ENCRYPTOR CLASS:
int numChars
String binaryInput

TEXTTOBINARY METHOD:
numChars = text length
create new char array[numchars]
for #chars length
    charArray[i] = charAt i
pad binary format to 8 bits and replace any spaces with 0's
return binary string

RANDOM BINARY STRING METHOD:
for length of input text
assign a variable random # from 0-256
convert it to binary
pad it to 8 bits
return random binary string

ENCRYPT METHOD:
inumberOfKeys = random#-
keys[] = new String[numberOfKeys];
display the keys and # of keys

for # keys
    keys[i] = randomBinaryString(numChars);
    display key

WRITE KEYS TO FILE METHOD:
CREATE raf = reference to (keysFile,"rw"))
reset raf.length
write all keys to file
close file
display that all data was read

BITWISE COMPARE METHOD:
binaryInput set length
temp = binaryInput
create raf = reference to (cipherTextRecall,"rw")
for # keys
    result = encode(temp, keys)
    display result
    temp = result
write temp to file
close file
return temp

BASSE64 ENCODER (bytes[]) METHOD:
use Base64.getEncoder().encodeToString(bytes)
remove spaces

ENCODE(text, key) METHOD:
use base64Encode(xorBytes(text.getBytes(), key.getBytes

XOR DECRYPTOR CLASS:

READ KEYS FROM FILE METHOD:
create raf reference to (keysFile, "r")
for keysfile.length
temp = readUTF
keys[i] = temp
close file
display that all data was read successfully

READ CIPHERTEXT FROM FILE METHOD:
String text;
create raf reference to (cipherTextRecall,"r")
text = readUTF();
close file
return text;

DECRYPT METHOD:
String binaryClearText;
for # keys
    binaryClearText = decode(cipherText, key);

create new int array= new int[(binaryClearText.length()/8)]
int start=0;
int end=8;
display clearText
for(binaryClearText.length()/8)
    intArray[i] = Integer.parseInt(binaryClearText.substring(start,end),2);
    display character bytes
    start +=8;
    end+=8;

BASE64 DECODE METHOD:
create byte array for base64Decode(cipherText)
use Base64.getDecoder().decode(cipherText);
        
DECODE(text,key) METHOD:
return new String(xorBytes(base64Decode(text), key.getBytes()));

 */
package cryptoTools;
import java.io.*;
import java.util.*;
import main.XORcist;

    public class CryptographyTool extends XORcist{
        protected String[] keys;
        protected final File keysFile = new File("keys.dat");
        protected final File cipherTextRecall = new File("cipherText.dat");
    
    //##########################################################################
    public static class XOREncryptor extends CryptographyTool{
    private int numChars =0;
    private final StringBuilder binaryInput = new StringBuilder();
    
        //######################################################################
        
        //returns 8 binary bits for each character in input String without spaces
        public StringBuilder inputTextToBinary(String inputText){
            this.numChars = inputText.length();
            char[] bytes = new char[this.numChars];
            for(int i=0;i<bytes.length;i++){
                bytes[i] = inputText.charAt(i);
                this.binaryInput.append(String.format("%8s",Integer.toBinaryString(bytes[i])).replace(" ","0"));
            }
            return this.binaryInput;
        }
        //######################################################################
        //generates random binary strings to XOR with
        private String randomBinaryString(int length){
            String keyString = "";
            for(int i=0;i<length;i++){
                keyString += (String.format("%8s",Integer.toBinaryString((int)(Math.random() * 256))).replace(" ","0"));
            }
            return keyString;
        
        }
        //encrypts the message via bitwise XORing
        public void encrypt(String original){
            int numberOfKeys = (int)(Math.random() * 100);//TEMPLINE //TEMPLINE
            this.keys = new String[numberOfKeys];
            System.out.println("The keys used to encrypt are: (("+numberOfKeys+") keys used)");
            
            for(int i=0;i<numberOfKeys;i++){
                this.keys[i] = randomBinaryString(numChars);
                System.out.println(this.keys[i]);
            }
        }
        //######################################################################
        //stores keys in a file for recall during decryption
        public void writeKeysToFile() throws FileNotFoundException, IOException{
            try(RandomAccessFile raf = new RandomAccessFile(keysFile,"rw")){ //can be read from or written to
                raf.setLength(0);//overwrites keys.dat each time the user uses the program
                for(String key : this.keys){
                    raf.writeUTF(key+"\n"); //stores keys in one per line format
                }
                raf.close();
            }finally{
                System.out.println("All data written successfully.");
            }
        }
        //######################################################################
        //uses the various Base64 methods listed below in one place(here)
        public String bitwiseCompare() throws FileNotFoundException, IOException{
            this.binaryInput.setLength(numChars*8);//kept appending final binary string twice
                                                 //easier to use numChars*8 because every char will be 8 bits
            String compare = this.binaryInput.toString();
            try (RandomAccessFile raf = new RandomAccessFile(cipherTextRecall,"rw")) {
                for(int i=0;i<this.keys.length;i++){
                    String xorResult = encode(compare,this.keys[i]);
                    System.out.println("Ciphertext is: "+xorResult);
                    compare = xorResult;
                    break;
                }
            raf.writeUTF(compare);
            raf.close();
            }
            return compare; 
        }
        //######################################################################
        //converts byte array into a readable string
        private String base64Encode(byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes).replaceAll("\\s","");
        }
        
        //encodes String into byte array using bitwise XORing against keys
        private String encode(String s, String key) {
            return base64Encode(xorBytes(s.getBytes(), key.getBytes()));
        }  
    }   
    //##########################################################################
    public static class XORDecryptor extends CryptographyTool{
        
        //reads the keys from keys.dat to be able to decrypt the cipherText
        public void readKeysFromFile() throws FileNotFoundException, IOException{
           keys = new String[(int)this.keysFile.length()];
           try(RandomAccessFile raf = new RandomAccessFile(keysFile, "r");){//can only be read from
               for(int i=0;i<keysFile.length();i++){
                   String row = raf.readUTF();
                   this.keys[i] = row;
               }
               raf.close();
           }catch(EOFException eof){
               System.out.println("All data read from file successfully.");
           }
        }
        //######################################################################
        public String readCipherTextFromFile() throws FileNotFoundException, IOException{
            String text;
            try(RandomAccessFile raf = new RandomAccessFile(cipherTextRecall,"r")){
                text = raf.readUTF();
                raf.close();
            }
            return text;
        }
        
        //######################################################################
        //decrypts the cipherText using the keys stored in keys.dat
        public void decrypt(String cipherText){
            String binaryClearText = "";
            for (String key : this.keys) {
                binaryClearText = decode(cipherText, key);
                System.out.println("\nCleartext in binary is:\n" + binaryClearText);
                break;
            }
            int[] bytes = new int[(binaryClearText.length()/8)];//array with enough room for 
            int start=0;
            int end=8;
            System.out.println("\nCleartext is: ");
            for(int i=0;i<(binaryClearText.length()/8);i++){//any number of bytes
                bytes[i] = Integer.parseInt(binaryClearText.substring(start,end),2);
                System.out.print((char)bytes[i]);
                start +=8;
                end+=8;
            }
            System.out.println();
        }
        private byte[] base64Decode(String s) {
            return Base64.getDecoder().decode(s);
        }
        
        private String decode(String s, String key) {
            return new String(xorBytes(base64Decode(s), key.getBytes()));
        }
    }
    
    //##########################################################################
    //performs Bitwise XORing on bytes using keys
    protected byte[] xorBytes(byte[] a, byte[] key) {
        byte[] bytes = new byte[a.length];
        for (int i=0; i<a.length; i++) {
            bytes[i] = (byte)(a[i] ^ key[i%key.length]);
        }
        return bytes;
    }
}
    
