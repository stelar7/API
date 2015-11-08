package div;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.spec.KeySpec;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption
{
    /**
     * check whether p is a safe 2048-bit prime (meaning that both p and (p-1)/2 are prime and that 2^2047 < p < 2^2048)
     * 
     * @param p
     *            the prime
     * @return true if its safe
     */
    public static boolean validatePrime(BigInteger p)
    {
        BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);

        if (p.isProbablePrime(1))
        {
            if (p.subtract(BigInteger.ONE).divide(TWO).isProbablePrime(1))
            {
                if (TWO.pow(2047).compareTo(p) == -1)
                {
                    if (TWO.pow(2048).compareTo(p) == 1)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Do the following, but with reflection to bypass access checks:
     *
     * 1. JceSecurity.isRestricted = false; 
     * 2. JceSecurity.defaultPolicy.perms.clear(); 
     * 3. JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
     * 
     * This removes the restriction on key lenghts
     */
    public static void removeCryptographyRestrictions()
    {
        try
        {
            final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
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

        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Make sure that g, g_a, and g_b > 1 && less than p-1
     * 
     * @param g
     *            the base
     * @param g_a
     *            person As' public key
     * @param g_b
     *            person Bs' public key
     * @return true if valid
     */
    public static boolean validatePublic(BigInteger p, BigInteger g, BigInteger g_a, BigInteger g_b)
    {
        if (g.compareTo(BigInteger.ONE) == 1 && g_a.compareTo(BigInteger.ONE) == 1 && g_b.compareTo(BigInteger.ONE) == 1)
        {
            BigInteger p_1 = p.subtract(BigInteger.ONE);
            if (g.compareTo(p_1) == -1 && g_a.compareTo(p_1) == -1 && g_b.compareTo(p_1) == -1)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate the decrypted string from the out string (IV:encoded)
     * 
     * @param pass
     *            the key to decode with
     * @param out
     *            the data to decode
     * @return the decrypted string
     */
    public static String generateDecryptMessage(String pass, String out)
    {
        try
        {
            char[] password = pass.toCharArray();
            byte[] salt = pass.getBytes(StandardCharsets.UTF_8);

            byte[] iv = div.Miscellaneous.hexStringToByteArray(out.split(":")[0]);
            byte[] ciphertext = div.Miscellaneous.hexStringToByteArray(out.split(":")[1]);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");

            return plaintext;

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate the encrypted string in the form of IV:encoded
     * 
     * @param pass
     *            the key to encode with
     * @param data
     *            the data to encode
     * @return the encrypted string
     */
    public static String generateEncryptMessage(String pass, String data)
    {
        try
        {
            char[] password = pass.toCharArray();
            byte[] salt = pass.getBytes(StandardCharsets.UTF_8);

            // derive key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // encrypt message
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ciphertext = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return div.Miscellaneous.bytesToHexString(iv) + ":" + div.Miscellaneous.bytesToHexString(ciphertext);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the secret shared key from a public key
     * 
     * @param prime
     *            the prime number used to generate the public key
     * @param publicKey
     *            the public part of a key
     * @param secretKey
     *            your secret part of the key
     * @return a number representing your shared secret
     */
    public static BigInteger diffieHellmanSecretKey(BigInteger prime, BigInteger publicKey, BigInteger secretKey)
    {
        BigInteger sharedSecret = publicKey.modPow(secretKey, prime);

        return sharedSecret;
    }

    /**
     * Generate a public key that you can use to encrypt data in a two-way conversation
     * 
     * @param prime
     *            a prime number (larger is better)
     * @param primitiveRoot
     *            (doesn't really matter, 2,3, etc is fine)
     * @param privateKey
     *            your private key
     * 
     * @return a number that represents your public key.
     */
    public static BigInteger diffieHellmanPublicKey(BigInteger prime, BigInteger primitiveRoot, BigInteger privateKey)
    {
        BigInteger publicKey = primitiveRoot.modPow(privateKey, prime);
        return publicKey;
    }

}
