package bxm.svn.utils;

import java.io.DataInputStream;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils
{
	private static String key= null;
	private static String _cipherAlgorithm= "TripleDES";
	
	static
	{
		InputStream inStream= null;
		try
		{
			inStream= Thread.currentThread().getContextClassLoader().getResourceAsStream( EncryptionUtils.class.getName().replace( '.', '/')+ ".class");
			DataInputStream din= new DataInputStream( inStream);
			int keyvalue= din.readInt();
			key= Integer.toHexString( keyvalue)+ "s;5je*sl&2kb-0s3";
		}
		catch( Throwable th){}
		finally
		{
			if( inStream!= null) try{ inStream.close();}catch( Exception e){}
		}
	}
	
	public static String encrypt( String plain)
	{
		try
		{
			SecretKeySpec ks= new SecretKeySpec( key.getBytes(), _cipherAlgorithm);
	        Cipher cipher= Cipher.getInstance( _cipherAlgorithm);
	        cipher.init( Cipher.ENCRYPT_MODE, ks);
	        byte[] encd= cipher.doFinal( plain.getBytes());
	        String encdString= new String( new sun.misc.BASE64Encoder().encode( encd));
	        return encdString;				
		}
		catch( Exception e)
		{
			System.out.println( e.getMessage());
			return plain;
		}
	}
	
	public static String decrypt( String encrypted)
	{
		try
		{
			SecretKeySpec ks= new SecretKeySpec( key.getBytes(), _cipherAlgorithm);
			Cipher cipher = Cipher.getInstance( _cipherAlgorithm);
	        cipher.init( Cipher.DECRYPT_MODE, ks);
	        byte[] decryptedBytes = cipher.doFinal( new sun.misc.BASE64Decoder().decodeBuffer( encrypted));
	        String decrypted = new String( decryptedBytes);
	        return decrypted;				
		}
		catch( Exception e)
		{
			System.out.println( e.getMessage());
			return encrypted;
		}
	}
	
	public static void main( String[] args) throws Exception
	{
		args = new String[] {"encrypt", "123456"};
	    if( args== null || args.length< 1)
	    {
	    	System.out.println( "invalid argument length");
	    	return;
	    }
	    
	    if( "encrypt".equals( args[0]))
	    {
	    	if( args.length!= 2) System.out.println( "required argument to encrypt.");
	    	System.out.println( encrypt( args[1]));
	    }
	    else if( "decrypt".equals( args[0]))
	    {
	    	if( args.length!= 2) System.out.println( "required argument to decrypt.");
	    	System.out.println( decrypt( args[1]));
	    }
	}
}
