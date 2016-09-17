package net.youtoolife.supernova.handlers;

public class RMECrypt {
	
	public RMECrypt() {
		
	}
	
	public byte[] encrypt(String text, String keyWord)
    {
        byte[] arr = text.getBytes();
        byte[] keyarr = keyWord.getBytes();
        byte[] result = new byte[arr.length];
        for(int i = 0; i< arr.length; i++)
        {
            result[i] = (byte) (arr[i] ^ keyarr[i % keyarr.length]);

        }
        return result;
    }
    public String decrypt(byte[] text, String keyWord)
    {
        byte[] result  = new byte[text.length];
        byte[] keyarr = keyWord.getBytes();
        for(int i = 0; i < text.length;i++)
        {
            result[i] = (byte) (text[i] ^ keyarr[i% keyarr.length]);
        }
        return new String(result);
    }
    
    public byte[] stringToBytesUTFCustom(String str) {
    	 char[] buffer = str.toCharArray();
    	 byte[] b = new byte[buffer.length << 1];
    	 for(int i = 0; i < buffer.length; i++) {
    	  int bpos = i << 1;
    	  b[bpos] = (byte) ((buffer[i]&0xFF00)>>8);
    	  b[bpos + 1] = (byte) (buffer[i]&0x00FF);
    	 }
    	 return b;
    	}
    public String bytesToStringUTFCustom(byte[] bytes) {
    	 char[] buffer = new char[bytes.length >> 1];
    	 for(int i = 0; i < buffer.length; i++) {
    	  int bpos = i << 1;
    	  char c = (char)(((bytes[bpos]&0x00FF)<<8) + (bytes[bpos+1]&0x00FF));
    	  buffer[i] = c;
    	 }
    	 return new String(buffer);
    	}

}
