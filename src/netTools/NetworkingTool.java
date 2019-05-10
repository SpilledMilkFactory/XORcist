/*
Christopher Sutton
3/26/2019
CSC201 final project
Tool inside XORcist program
Tool is responsible for using bitwise XORing to accompblish:
Function A) take a collection of IP addresses and return a valid supernet cidr notation for subnet
 */
package netTools;
import main.XORcist;
import java.util.*;
//import org.apache.commons.*;
public class NetworkingTool extends XORcist{//XOR is used in supernetting
    public ArrayList<String> ipList = new ArrayList<>();//user must include CIDR notation
    public ArrayList<String> subnetMask = new ArrayList<>();
    public ArrayList<Integer> cidrList = new ArrayList<>();
    
    //populates the IP Table and CIDR list of any NetworkingTool object
    public void populateIPTableManually(){
        String sentinel = "G";//GO
        while (!sentinel.equalsIgnoreCase("Q")){
            System.out.println("(Enter Q to quit)");
            System.out.println("Enter an IP address with CIDR notation: ");
            this.ipList.add(kbd.nextLine());
            if (containsIgnoreCase("q",this.ipList)){
                this.ipList.remove(this.ipList.size()-1);
                sentinel = "Q";
            }
            for(String s: this.ipList){
                this.cidrList.add(Integer.parseInt(s.substring(s.indexOf('/')+1)));//taking the CIDR notation from each entered IP
                for(int i = 1; i<(this.cidrList.size()-1);i++){
                    for (int j = 0; j<i; j++){
                        if(Objects.equals(this.cidrList.get(i), this.cidrList.get(j)))
                            this.cidrList.remove(j);//list populates with multiple duplicates, 
                    }//subnets will ALWAYS be the same when starting so duplicates in a supernetting scenario are redundant
                }// except 1st and last always populate but the below line takes care of the dupliate at index 0
            }
            this.cidrList.remove(0);
        }
    }
    //##########################################################################
    public void IPtoBinary(){
        String[] ipAddress = new String[ipList.size()];//stores IP addresses without CIDR
        int[][] gridIPs = new int[ipList.size()][4];//splits up IP into a grid array of decimals
        int[] dot = new int[4];//indexing values for each byte
        String[][] binaryIPs = new String[ipList.size()][4];//this is the matrix used for bitwise comparison
        
        System.out.println("\nIP Table:");
        //separates integer byte values from "." in an IP address
        for (int i=0; i<ipList.size();i++){
            ipAddress[i] = ipList.get(i).substring(0,ipList.get(i).indexOf(('/')));
            //holds each IP byte value separated by a "."
            dot[0] = ipAddress[i].indexOf(0,'.');
            dot[1] = ipAddress[i].indexOf('.',(dot[0]+1));
            dot[2] = ipAddress[i].indexOf('.',(dot[1]+1));
            dot[3] = ipAddress[i].indexOf('.',dot[2]+1);
            
            for(int j=0;j<4;j++){
                if (j==3){
                    gridIPs[i][j] = Integer.parseInt(ipAddress[i].substring(dot[3]+1));  
                }else{
                    gridIPs[i][j] = Integer.parseInt(ipAddress[i].substring(dot[j]+1,dot[j+1]));
                }
            }
            System.out.println(ipAddress[i]);
        }
        //Changes class variable ipList to binary
        for (int i=0;i<ipList.size();i++){
            ipAddress[i] ="";
            for (int j=0;j<4;j++){
                binaryIPs[i][j] = (String.format("%8s",Integer.toBinaryString(gridIPs[i][j])).replace(" ","0")+" ");
                ipAddress[i] += binaryIPs[i][j];
            }
            ipList.set(i,ipAddress[i]);
            System.out.print(ipList.get(i));
            System.out.println();
        }
        System.out.println();
    }
    //##########################################################################
    public void bitwiseCompare(){
        String[][] fullBinaryTable = new String[ipList.size()][32];
        String compareResult = "";
        int[] space = new int[4];//indexing values for each byte

        //populates ipList binary values to full binary table
        for (int i=0;i<ipList.size();i++){
            space[0] = ipList.get(i).indexOf(0,' ');
            space[1] = ipList.get(i).indexOf(' ',(space[0]+1));
            space[2] = ipList.get(i).indexOf(' ',(space[1]+1));
            space[3] = ipList.get(i).indexOf(' ',space[2]+1);
            for (int j=0;j<4;j++){
                if (j==3){
                    fullBinaryTable[i][j] = ipList.get(i).substring(space[3]+1);  
                }else{
                    fullBinaryTable[i][j] = ipList.get(i).substring(space[j]+1,space[j+1]);
                }
            }
        }
        
        System.out.println("Full Binary IP Table: ");
        String[] singleLineTable = new String[ipList.size()]; 
        for (int i=0;i<fullBinaryTable.length;i++){
            for (int j=0;j<4;j++){
                compareResult += fullBinaryTable[i][j];
            }
            singleLineTable[i] = compareResult;
            System.out.println(singleLineTable[i]);
            compareResult = "";//resets binary line after each
        }
        
        //scans each column of the full binary table
        boolean repeat = true;
        while (repeat != false){
            for(int i=0;i<singleLineTable[0].length();i++){//all lengths are the same, 
                for(int j = 1;j<ipList.size();j++){//doesn't matter which one you pick
                    if(singleLineTable[j].charAt(i) == (singleLineTable[j-1].charAt(i))){
                        compareResult += singleLineTable[j].charAt(i);
                        break;
                    }else{
                        i = singleLineTable[0].length()-1;
                        repeat = false;
                        break;
                    }
                    
                }
            }
        }
        System.out.println("\nSupernet Network Bits in Binary:\n"+compareResult);
        int supernetCIDR = compareResult.length();
        System.out.println("Supernet CIDR notation: /"+supernetCIDR);
        
        //fills in the remaining 0's to make a full 32 bit address
        if(compareResult.length() < 32){
            for(int i=supernetCIDR; i<32; i++){
                compareResult += "0";
            }
        }
        System.out.println("\nFull Binary Supernet IP:\n"+compareResult);
        
        //splits up full binary supenet address into byte values in Decimal
        int[] supernetIpBytes = new int[4];
        supernetIpBytes[0] = Integer.parseInt(compareResult.substring(0,8),2);
        supernetIpBytes[1] = Integer.parseInt(compareResult.substring(8,16),2);
        supernetIpBytes[2] = Integer.parseInt(compareResult.substring(16,24),2);
        supernetIpBytes[3] = Integer.parseInt(compareResult.substring(24,32),2);

        //display decimal supernet IP address
        System.out.println("\nSupernet IP address: ");
        for(int i=0;i<4;i++){
            if (i != 3){
                System.out.print(supernetIpBytes[i]+"."); 
            }else{
                System.out.print(supernetIpBytes[i]);
            }
            
        }
        System.out.print("/"+supernetCIDR);
        
        //populates binary value variable for CIDR notation
        String supernetSubnetMask = "";
        for(int i=1;i<=supernetCIDR;i++){
            supernetSubnetMask += "1";
        }
        //fills in the remaining 0's to make a full 32 bit address
        if(supernetSubnetMask.length() < 32){
            for(int i=supernetSubnetMask.length(); i<32; i++){
                supernetSubnetMask += "0";
            }
        }
        //converts supernet Subnet Mask from binary to decimal
        int[] supernetSubnetBytes = new int[4];
        supernetSubnetBytes[0] = Integer.parseInt(supernetSubnetMask.substring(0,8),2);
        supernetSubnetBytes[1] = Integer.parseInt(supernetSubnetMask.substring(8,16),2);
        supernetSubnetBytes[2] = Integer.parseInt(supernetSubnetMask.substring(16,24),2);
        supernetSubnetBytes[3] = Integer.parseInt(supernetSubnetMask.substring(24,32),2);
        
        
        //display decimal supernet Subnet Mask
        System.out.println("\nSupernet Subnet Mask:");
        for(int i=0;i<4;i++){
            if(i!=3){
                System.out.print(supernetSubnetBytes[i]+"."); 
            }else{
                System.out.print(supernetSubnetBytes[i]);
            }
            
        }
        System.out.println();
    }
}
        