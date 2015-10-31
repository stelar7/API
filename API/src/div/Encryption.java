package div;

import java.math.BigInteger;

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
