package element;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public class Test2 {

	public static void main(String[] args) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(1024);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			
			System.out.println("1:" + keyPair.toString());
			
			PublicKey puk = keyPair.getPublic();
			System.out.println("2:" + puk.toString());
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}

}
