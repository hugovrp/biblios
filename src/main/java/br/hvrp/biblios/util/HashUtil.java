package br.hvrp.biblios.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  Classe utilitária para criptografia de senhas.
 *  Utiliza o algoritmo SHA-256 para gerar hash das senhas.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
public class HashUtil {
	
	/**
	 *  Gera um hash SHA-256 da senha fornecida.
	 *  O hash é retornado como uma string hexadecimal.
	 * 
	 *  @param password senha em texto plano a ser criptografada
	 *  @return hash SHA-256 da senha em formato hexadecimal
	 *  @throws RuntimeException se o algoritmo SHA-256 não estiver disponível
	 */
	 public static String hashPassword(String password) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
	            
	            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
	            for(byte b : encodedhash) {
	                String hex = Integer.toHexString(0xff & b);
	                if (hex.length() == 1) hexString.append('0');
	                hexString.append(hex);
	            }
	            return hexString.toString();
	        } catch(NoSuchAlgorithmException e) {
	            throw new RuntimeException("Erro ao criptografar senha", e);
	        }
	    }
}