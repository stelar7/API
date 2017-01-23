package div;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.nio.charset.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

public final class Encryption
{
    
    private Encryption()
    {
        
    }
    
    /**
     * Generate a public key that you can use to encrypt data in a two-way conversation
     *
     * @param prime         a prime number (larger is better)
     * @param primitiveRoot (doesn't really matter, 2,3, etc is fine)
     * @param privateKey    your private key
     * @return a number that represents your public key.
     */
    public static BigInteger diffieHellmanPublicKey(final BigInteger prime, final BigInteger primitiveRoot, final BigInteger privateKey)
    {
        return primitiveRoot.modPow(privateKey, prime);
    }
    
    /**
     * Get the secret shared key from a public key
     *
     * @param prime     the prime number used to generate the public key
     * @param publicKey the public part of a key
     * @param secretKey your secret part of the key
     * @return a number representing your shared secret
     */
    public static BigInteger diffieHellmanSecretKey(final BigInteger prime, final BigInteger publicKey, final BigInteger secretKey)
    {
        
        return publicKey.modPow(secretKey, prime);
    }
    
    /**
     * Generate the decrypted string from the out string (IV:encoded)
     *
     * @param pass the key to decode with
     * @param out  the data to decode
     * @return the decrypted string
     */
    public static String generateDecryptMessage(final String pass, final String out)
    {
        try
        {
            final char[] password = pass.toCharArray();
            final byte[] salt     = pass.getBytes(StandardCharsets.UTF_8);
            
            final byte[] iv         = div.Miscellaneous.hexStringToByteArray(out.split(":")[0]);
            final byte[] ciphertext = div.Miscellaneous.hexStringToByteArray(out.split(":")[1]);
            
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec          spec    = new PBEKeySpec(password, salt, 65_536, 256);
            final SecretKey        tmp     = factory.generateSecret(spec);
            final SecretKey        secret  = new SecretKeySpec(tmp.getEncoded(), "AES");
            
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            
            return new String(cipher.doFinal(ciphertext), "UTF-8");
    
        } catch (final GeneralSecurityException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Generate the encrypted string in the form of IV:encoded
     *
     * @param pass the key to encode with
     * @param data the data to encode
     * @return the encrypted string
     */
    public static String generateEncryptMessage(final String pass, final String data)
    {
        try
        {
            final char[] password = pass.toCharArray();
            final byte[] salt     = pass.getBytes(StandardCharsets.UTF_8);
            
            // derive key
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec          spec    = new PBEKeySpec(password, salt, 65_536, 256);
            final SecretKey        tmp     = factory.generateSecret(spec);
            final SecretKey        secret  = new SecretKeySpec(tmp.getEncoded(), "AES");
            
            // encrypt message
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            final AlgorithmParameters params     = cipher.getParameters();
            final byte[]              iv         = params.getParameterSpec(IvParameterSpec.class).getIV();
            final byte[]              ciphertext = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            return div.Miscellaneous.bytesToHexString(iv) + ":" + div.Miscellaneous.bytesToHexString(ciphertext);
        } catch (final GeneralSecurityException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Do the following, but with reflection to bypass access checks:
     * <p>
     * 1. JceSecurity.isRestricted = false; 2. JceSecurity.defaultPolicy.perms.clear(); 3. JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
     * <p>
     * This removes the restriction on key lenghts
     */
    public static void removeCryptographyRestrictions()
    {
        try
        {
            final Class<?> jceSecurity         = Class.forName("javax.crypto.JceSecurity");
            final Class<?> cryptoPermissions   = Class.forName("javax.crypto.CryptoPermissions");
            final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");
            
            final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
            isRestrictedField.setAccessible(true);
            isRestrictedField.set(null, false);
            
            final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicyField.setAccessible(true);
            final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);
            
            final Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defaultPolicy)).clear();
            
            final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defaultPolicy.add((Permission) instance.get(null));
    
        } catch (final IllegalAccessException | NoSuchFieldException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * check whether p is a safe 2048-bit prime (meaning that both p and (p-1)/2 are prime and that 2^2047 < p < 2^2048)
     *
     * @param p the prime
     * @return true if its safe
     */
    public static boolean validatePrime(final BigInteger p)
    {
        final BigInteger two          = BigInteger.ONE.add(BigInteger.ONE);
        final int        twoPowEleven = 2048;
        
        if (p.isProbablePrime(1))
        {
            if (p.subtract(BigInteger.ONE).divide(two).isProbablePrime(1))
            {
                if (two.pow(twoPowEleven - 1).compareTo(p) < 0)
                {
                    if (two.pow(twoPowEleven).compareTo(p) > 0)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Make sure that g, g_a, and g_b > 1 && less than p-1
     *
     * @param g  the base
     * @param gA person As' public key
     * @param gB person Bs' public key
     * @return true if valid
     */
    public static boolean validatePublic(final BigInteger p, final BigInteger g, final BigInteger gA, final BigInteger gB)
    {
        if ((g.compareTo(BigInteger.ONE) > 0) && (gA.compareTo(BigInteger.ONE) > 0) && (gB.compareTo(BigInteger.ONE) > 0))
        {
            final BigInteger pOne = p.subtract(BigInteger.ONE);
            if ((g.compareTo(pOne) < 0) && (gA.compareTo(pOne) < 0) && (gB.compareTo(pOne) < 0))
            {
                return true;
            }
        }
        return false;
    }
    
}
