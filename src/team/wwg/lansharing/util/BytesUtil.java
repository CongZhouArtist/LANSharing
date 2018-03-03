package team.wwg.lansharing.util;

public class BytesUtil {

	
	/**
	 * ��intתΪ bytes����  
	 * ��λ����λ
	 * @param integer �����int 
	 * @return
	 */
	public static byte[] intToByteArray (int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
		byteArray[3 - n] = (byte) (integer >>> (n * 8));
		
		return (byteArray);
		}
	
	
	/**
	 * ��λ����λ
	 * 
	 * @param b ����� bytes����
	 * 
	 * @return int ����
	 */
	public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
 }
	
	
	//long����ת��byte���� 
	  public static byte[] longToByte(long number) { 
	        long temp = number; 
	        byte[] b = new byte[8]; 
	        for (int i = 0; i < b.length; i++) { 
	            b[i] = new Long(temp & 0xff).byteValue();// �����λ���������λ 
	            temp = temp >> 8; // ������8λ 
	        } 
	        return b; 
	    } 
	    
	    //byte����ת��long 
	    public static long byteToLong(byte[] b) { 
	        long s = 0; 
	        long s0 = b[0] & 0xff;// ���λ 
	        long s1 = b[1] & 0xff; 
	        long s2 = b[2] & 0xff; 
	        long s3 = b[3] & 0xff; 
	        long s4 = b[4] & 0xff;// ���λ 
	        long s5 = b[5] & 0xff; 
	        long s6 = b[6] & 0xff; 
	        long s7 = b[7] & 0xff; 
	 
	        // s0���� 
	        s1 <<= 8; 
	        s2 <<= 16; 
	        s3 <<= 24; 
	        s4 <<= 8 * 4; 
	        s5 <<= 8 * 5; 
	        s6 <<= 8 * 6; 
	        s7 <<= 8 * 7; 
	        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7; 
	        return s; 
	    } 
	
	
	
	
	
	
}
