package CA;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;

import sun.security.x509.X509CertImpl;

public class Communicate {

	private static char[] caPassword = "ece6102".toCharArray();
	private static String keystoreFile = "../keyStoreFile.bin";
	private static String caAlias = "ca";
	private static PublicKey pubKey;
	private static PrivateKey privKey;
	private static Hashtable<String, SecretKey> privatekeys = new Hashtable<String,SecretKey>();
	
	private static ServerSocket serverSocket;
	private static Socket socket;
	private static FileOutputStream ksos = null;
	
	public void GetKeys() throws Exception
	{
		FileInputStream input = new FileInputStream(keystoreFile);
	    KeyStore keyStore = KeyStore.getInstance("JKS");
	    //KeyStore ks = KeyStore.getInstance("JCEKS");
	    keyStore.load(input, caPassword);
	    input.close();
	    privKey = (PrivateKey) keyStore.getKey(caAlias, caPassword);
	    java.security.cert.Certificate caCert = keyStore.getCertificate(caAlias);
	    pubKey = caCert.getPublicKey();
	}
	public void listen()
	{
		try{
		serverSocket = new ServerSocket(1001,20);
		GetKeys();    
		ksos = new FileOutputStream("keystoreAuthenticateFile");
		KeyStore ks = KeyStore.getInstance("JCEKS");
		
		ObjectOutputStream out = null;
		ObjectInputStream object = null;
		socket = serverSocket.accept();
		out = new ObjectOutputStream(socket.getOutputStream());
		object = new ObjectInputStream(socket.getInputStream());
		X509Certificate cert = null;
		while(object.available() <= 0 && !socket.isClosed())
	    {	    	
	    	{
	    		synchronized(this)
	    		{
	    			Hashtable request = new Hashtable();
	    			  request = (Hashtable)object.readObject();
	    			  
	    			if(request.containsKey("authenticate"))
	    			  {
	    				  KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
	    				    keyGen.initialize(Skip.sDHParameterSpec);
	    				    KeyPair   keypair = keyGen.genKeyPair();

	    				    // Get the generated public and private keys
	    				    PrivateKey diffiePriv = keypair.getPrivate();
	    				    PublicKey diffiePub = keypair.getPublic();
	    				    
	    				    byte[] clientKey = (byte[])request.get("authenticate");
	    				    cert = (X509Certificate)request.get("cert");
	    			      SecretKey key = InitiateProcess(clientKey,diffiePriv,diffiePub);
	    			     
	    			     
	    			      privatekeys.put(new String(cert.getPublicKey().getEncoded(),"UTF-8"), key);
	    			      
	    			         //ks.store(ksos, caPassword);	
	    			         Hashtable<String, byte[]> table = new Hashtable<String,byte[]>();
	    			         table.put("authenticateResponse", diffiePub.getEncoded());
	    			         out.writeObject(table);
	    			         out.flush();
	    			         continue;
	    			  } 
	    			else
	    			  {
	    				
  				  if(request.containsKey("message"))
  				  {
  					  FileInputStream input = new FileInputStream(keystoreFile);
		          	      KeyStore keyStore = KeyStore.getInstance("JCEKS");
		          	      keyStore.load(input, caPassword);
		          	      input.close();
  					  
  					  Cipher dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
  					  SecretKey key = privatekeys.get(new String(cert.getPublicKey().getEncoded(),"UTF-8"));
  					  if(key != null)
  					  {
  			              dcipher.init(Cipher.DECRYPT_MODE, key);
  			              SealedObject so = (SealedObject)request.get("message"); 
  			              Hashtable process = new Hashtable();
  			              process = (Hashtable)so.getObject(dcipher);		              
			          	  Cipher ecipher = Cipher.getInstance("DES");
	    			      ecipher.init(Cipher.ENCRYPT_MODE, key);
	    			      
	    			      if(process.containsKey("terminate"))
			    		  {
			        		 //keyStore.deleteEntry(cert);
	    			    	  privatekeys.remove(key);
			    			 String logoutSuccess = "1";
			    			 byte[] enc = ecipher.doFinal(logoutSuccess.getBytes("UTF-8"));
			    			 out.write(enc);
			    		  }
	    			      else
				        	  if(process.containsKey("queryId"))
				        	  {
				        		  
    			        		  int requestId = Integer.parseInt(process.get("queryId").toString());
    			        		  Hashtable table = new Hashtable();
    			        		  switch(requestId)
    			                  {
    			        		  case 1:
    			        			  //PublicKey keyClient = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(cert));
    			        			  X509CertImpl newCert = CertificateAuthority.GenerateClientCertificate(cert);
    			        			  SealedObject enc = new SealedObject("1", ecipher);
    			        			  
					            	    enc = new SealedObject(newCert, ecipher);
					            	    table.put("response", enc);
					            	    table.put("responseId","1");
					            	    out.writeObject(table);
					            	    break;
    			        		  case 2:
    			        			  X509Certificate clientCert = (X509Certificate)process.get("cert");
    			        			  if(clientCert != null)
    			        			     clientCert = CertificateAuthority.ValidateCertificate(clientCert);
    			        			  
    			        			  if(clientCert != null)
    			        			  {
					            	    enc = new SealedObject(clientCert, ecipher);
					            	    table.put("response", enc);
					            	    table.put("responseId","1");
    			        			  }
    			        			  else
    			        				  table.put("responseId","99");
					            	    out.writeObject(table);
					            	    break;
    			                  }
				        	  }

  					  }
  					
  				  }
	    		}
	    	}
	    }}
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}
	
	public static String GenerateParameterSet() {
	    try {
	        // Create the parameter generator for a 1024-bit DH key pair
	        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
	        //paramGen.init(1024);
            paramGen.init(1024);
	        // Generate the parameters
	        AlgorithmParameters params = paramGen.generateParameters();
	        DHParameterSpec dhSpec
	            = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);

	        // Return the three values in a string
	        return ""+dhSpec.getP()+","+dhSpec.getG()+","+dhSpec.getL();
	    } catch (NoSuchAlgorithmException e) {
	    } catch (InvalidParameterSpecException e) {
	    }
	    return null;
	}
	
    public static SecretKey InitiateProcess(byte[] key, PrivateKey diffiePriv, PublicKey diffiePub)
    {
    	
	try {
		
	    // Get the generated public and private keys
		FileInputStream input = new FileInputStream(keystoreFile);
	    KeyStore keyStore = KeyStore.getInstance("JKS");
	    keyStore.load(input, caPassword);

	    //PrivateKey privateKey = (PrivateKey) keyStore.getKey(caAlias, caPassword);
	    java.security.cert.Certificate caCert = keyStore.getCertificate(caAlias);

	    
		 
        // Get public key
        //publicKey = caCert.getPublicKey();
        Hashtable<String,byte[]> response = new Hashtable<String,byte[]>();
        
	    // Retrieve the public key bytes of the other party
        byte[] publicKeyBytes = key;

	    // Convert the public key bytes into a PublicKey object
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
	    //KeyFactory keyFact = KeyFactory.getInstance("DH");
	    KeyFactory keyFact = KeyFactory.getInstance("DH");
	    diffiePub = keyFact.generatePublic(x509KeySpec);

	    // Prepare to generate the secret key with the private key and public key of the other party
	    KeyAgreement ka = KeyAgreement.getInstance("DH");
	    ka.init(diffiePriv);
	    ka.doPhase(diffiePub, true);

	    // Specify the type of key to generate;
	    // see Listing All Available Symmetric Key Generators
	    String algorithm = "DES";

	    // Generate the secret key
	    SecretKey secretKey = ka.generateSecret(algorithm);
        return secretKey;
	    // Use the secret key to encrypt/decrypt data;
	    // see Encrypting a String with DES
	} 
	catch (java.security.InvalidKeyException e) {
		return null;
	} 
	catch (java.security.spec.InvalidKeySpecException e) {
		System.out.println(e.getMessage());
		return null;
	} 

	catch (java.security.NoSuchAlgorithmException e) {
		return null;
	}
	catch(Exception evt)
	{
		return null;
	}
    }
    public static void main(String args[])
    {
    	new Communicate().listen();
    }
}
