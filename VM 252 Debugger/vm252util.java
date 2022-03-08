
public class vm252util{
    private String hx = new String();
    public vm252util(String hx){
        this.hx = hx;

    } 
    public vm252util(){
        this.hx = "hello";

    } 
    public String intHex(int num){
        String hx = "";
        for(int i = 3; i>= 0; --i){
            hx = hx + String.format("%02x",(num>>8*i)&0xff);
            System.out.print(hx );
        }
        System.out.println("");
        return hx;
    }
    public String byteHex(byte[] mybyte){
        String hx = "";
        for(int i = 0; i<mybyte.length; ++i){
            hx = hx + String.format("%02x ",mybyte[i]);
        
        }
        
        System.out.println("byte to hex = ");
        System.out.print(hx );
        return hx;
    }

    

    public static void main(String[] args){
        String hx = new String();
        vm252util x = new vm252util();
        byte[] bytearray = new byte[3];
        bytearray[0] = (byte)12;
        bytearray[1] = (byte)30;
        bytearray[2] = (byte)40;

          
        hx = x.byteHex(bytearray);

        hx = x.intHex(12);
        System.out.println("hex = " + hx);
        hx = x.intHex(113827196);
        System.out.println("hex = " + hx);
        System.out.println("hex = " + x.intHex(40));
    }

}