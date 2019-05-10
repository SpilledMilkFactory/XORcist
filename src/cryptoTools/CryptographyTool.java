/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoTools;
import java.io.*;
import java.util.*;
import main.XORcist;
import sun.misc.*;

    public class CryptographyTool extends XORcist{
        public String type;
        public StringBuilder binaryInput;
        public int numChars;
        public String[] keys;
        final File keysFile = new File("keys.dat");
        final File cipherTextRecall = new File("cipherText.dat");
    
    //##########################################################################
    public static class XOREncryptor extends CryptographyTool{

        public XOREncryptor(){
            this.type = "Encryptor";
            this.binaryInput = new StringBuilder();
            this.numChars = 0;
            //the keys variable isn't meant to be initialized upon construction
        }
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
        public String randomBinaryString(int length){
            String keyString = "";
            for(int i=0;i<length;i++){
                keyString += (String.format("%8s",Integer.toBinaryString((int)(Math.random() * 256))).replace(" ","0"));
            }
            return keyString;
        
        }
        //encrypts the message via bitwise XORing
        public void encrypt(String original){
            int numberOfKeys = (int)(Math.random() * 10);//TEMPLINE //TEMPLINE
            this.keys = new String[numberOfKeys];
            System.out.println("The keys used to encrypt are: ");
            
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
        public static String base64Encode(byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes).replaceAll("\\s","");
        }
        
        //encodes String into byte array using bitwise XORing against keys
        public String encode(String s, String key) {
            return base64Encode(xorBytes(s.getBytes(), key.getBytes()));
        }  
    }   
    //##########################################################################
    public static class XORDecryptor extends CryptographyTool{
        
        public XORDecryptor(){
            type = "Decryptor";
            binaryInput = new StringBuilder();
            numChars = 0;
            //the other two variables aren't meant to be initialized yet
        }
        //######################################################################
        //reads the keys from keys.dat to be able to decrypt the cipherText
        public void readKeysFromFile() throws FileNotFoundException, IOException{
           String row;
           keys = new String[(int)this.keysFile.length()];
           try(RandomAccessFile raf = new RandomAccessFile(keysFile, "r");){//can only be read from
               for(int i=0;i<keysFile.length();i++){
                   row = raf.readUTF();
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
            }
            return text;
        }
        
        //######################################################################
        //decrypts the cipherText using the keys stored in keys.dat
        public void decrypt(String cipherText){
            //reversing the stored keys list for decryption
            for(int i=0; i<this.keys.length/2; i++){
                String temp = this.keys[i];
                this.keys[i] = this.keys[this.keys.length -i -1];
                this.keys[this.keys.length -i -1] = temp;
            }
            for (int i=0;i<this.keys.length;i++) {
                String decodeResults = (decode(cipherText, keys[i]));
                System.out.println("Cleartext is: " + decode(decodeResults, keys[i]));
            }
        }
        public byte[] base64Decode(String s) {
            return Base64.getDecoder().decode(s);
        }
        
        public String decode(String s, String key) {
            return new String(xorBytes(base64Decode(s), key.getBytes()));
        }
        
    }
    //##########################################################################
    //performs Bitwise XORing on bytes using keys
    public byte[] xorBytes(byte[] a, byte[] key) {
        byte[] bytes = new byte[a.length];
        for (int i=0; i<a.length; i++) {
            bytes[i] = (byte)(a[i] ^ key[i%key.length]);
        }
        return bytes;
    }
}
    