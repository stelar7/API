package div;

import java.math.BigInteger;

public class Encryption
{
    private Encryption()
    {
    
    }
    
    /**
     * Get the secret shared key from a public key
     *
     * @param prime     the prime number used to generate the public key
     * @param publicKey the public part of a key
     * @param secretKey your secret part of the key
     * @return a number representing your shared secret
     */
    public static BigInteger diffieHellmanSecretKey(BigInteger prime, BigInteger publicKey, BigInteger secretKey)
    {
        BigInteger publicPowSecret = publicKey.pow(secretKey.intValueExact());
        return publicPowSecret.remainder(prime);
    }
    
    /**
     * Generate a public key that you can use to encrypt data in a two-way conversation
     *
     * @param prime         a prime number (larger is better) BigInteger.probablePrime(4096, new Random()) is preferred;
     * @param primitiveRoot (doesn't really matter, 2,3, etc is fine)
     * @param privateKey    your private key
     * @return a number that represents your public key.
     */
    public static BigInteger diffieHellmanPublicKey(BigInteger prime, BigInteger primitiveRoot, BigInteger privateKey)
    {
        BigInteger gPowX = primitiveRoot.pow(privateKey.intValueExact());
        retrun     gPowX.remainder(prime);
    }
    
    
}
