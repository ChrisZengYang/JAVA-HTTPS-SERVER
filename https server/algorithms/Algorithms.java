package algorithms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Algorithms {
    static final BigInteger A24 = new BigInteger("121666");

    static int messageBlockSize(SHA whichSha) {
        switch(whichSha) {
            case _256 -> {
                return Hash_Sizes._256_MessageBlock.getValue();
            }
            case _384 -> {
                return Hash_Sizes._384_MessageBlock.getValue();
            }
            case _512 -> {
                return Hash_Sizes._512_MessageBlock.getValue();
            }
            default -> throw new IllegalArgumentException("Unsupported hash type: " + whichSha);
        }
    }

    public static int hashSize(SHA whichSha) {
        switch(whichSha) {
            case _256 -> {
                return Hash_Sizes._256_HashSize.getValue();
            }
            case _384 -> {
                return Hash_Sizes._384_HashSize.getValue();
            }
            case _512 -> {
                return Hash_Sizes._512_HashSize.getValue();
            }
            default -> throw new IllegalArgumentException("Unsupported hash type: " + whichSha);
        }
    }
    public byte[] SHA(SHA whichSHA, byte[] message) {
        switch(whichSHA) {
            case _256 -> {
                return SHA256(message);
            }
            case _384 -> {
                return SHA384(message);
            }
            case _512 -> {
                return SHA512(message);
            }
            default -> throw new IllegalArgumentException("Unsupported hash type: " + whichSHA);
        }
    }

    public static byte[] hexToByteArray (String s) {
        int len = s.length();
        if(len % 2 != 0) {
            throw new IllegalArgumentException("Must have even length");
        }

        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+=2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    static byte[] reverse (byte[] array) {
        byte[] reversed = new byte[array.length];
        for(int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - 1 - i];
        }
        return reversed;
    }

    public byte[] montgomery (BigInteger P, byte[] K) { //p = point (big endian input, needs swapping), k = Scalar (little endian input)
        //Derivative of curve at point Gx, Gy = (3x^2+a)/2y

        //Input is little endian
        byte[] kBytes = K.clone();

        //Clamping (Little-endian)s
        //ONLY PRIVATE KEY IS CLAMPED
        kBytes[0] = (byte) (kBytes[0] & 0b11111000); // clear lowest 3 bits
        kBytes[31] = (byte) (kBytes[31] & 0b01111111); // clear highest bit     
        kBytes[31] = (byte) (kBytes[31] | 0b01000000); //set 2nd highest bit to 1


        System.out.println();
        System.out.println("Clamped bytes: (little-endian) ");
        for(int i = 0; i < 32; i++) { //Little Endianness endianness
            System.out.print(String.format("%02x", kBytes[i]));
        }
        System.out.println();

        //Montgomery Ladder points
        BigInteger[] P1 = {BigInteger.ONE, BigInteger.ZERO}; //Running point 1: Point at infinity
        BigInteger[] P2 = {P, BigInteger.ONE}; //Running point 2: affine x = 9
        BigInteger[] P0 = {P, BigInteger.ONE}; //Difference, P2 - P1

        for (int i = 254; i >= 0; i--) { //Only bits 254 and lower are used
            int bit = (kBytes[i >>> 3] >>> (i & 7)) & 1; //i >>> 3 means divide by 8 (floor division) ; i & 7 means mod 8 ; 1 filters out one bit

            if (bit == 0) {
                P2 = Algorithms.diffAddition(P1, P2, P0);
                P1 = Algorithms.pointDouble(P1);
            } else {
                P1 = Algorithms.diffAddition(P1, P2, P0);
                P2 = Algorithms.pointDouble(P2);
            }
        }


        //Affine x-coordinate
        
        BigInteger affine = Algorithms.affine(P1);


        byte[] pubKey = new byte[32];
        byte[] tmp = affine.toByteArray(); //Create a temporary byte array so we can adjust according to length

        int copyStart = Math.max(0, tmp.length - 32);
        int copyLength = tmp.length - copyStart;
        System.arraycopy(tmp, copyStart, pubKey, 32 - copyLength, copyLength);
        return pubKey;
    }

    static BigInteger affine (BigInteger[] P) {
        BigInteger p = new BigInteger("2").pow(255).subtract(new BigInteger("19"));
        BigInteger zInverse = Algorithms.inverseMod(P[1], p);
        BigInteger xAffine = P[0].multiply(zInverse).mod(p);
        return xAffine;
    }

    static BigInteger[] diffAddition (BigInteger[] P1, BigInteger[] P2, BigInteger[] P0) { //Differential addition
        BigInteger p = new BigInteger("2").pow(255).subtract(new BigInteger("19"));
        
        BigInteger A = P1[0].add(P1[1]).mod(p); //X + Z
        BigInteger B = P1[0].subtract(P1[1]).mod(p); //X - Z

        BigInteger C = P2[0].add(P2[1]).mod(p); //X2 + Z2
        BigInteger D = P2[0].subtract(P2[1]).mod(p); //X2 - Z2

        BigInteger DA = D.multiply(A).mod(p);
        BigInteger CB = C.multiply(B).mod(p);

        BigInteger X3 = P0[1].multiply((DA.add(CB)).multiply(DA.add(CB))).mod(p);
        BigInteger Z3 = P0[0].multiply((DA.subtract(CB)).multiply(DA.subtract(CB))).mod(p);

        return new BigInteger[] {X3, Z3};
    }

    static BigInteger[] pointDouble (BigInteger[] P1) {
        BigInteger p = new BigInteger("2").pow(255).subtract(new BigInteger("19"));
        BigInteger A = P1[0].add(P1[1]).mod(p);
        BigInteger B = P1[0].subtract(P1[1]).mod(p);
        BigInteger AA = A.multiply(A).mod(p);
        BigInteger BB = B.multiply(B).mod(p);
        BigInteger E = AA.subtract(BB).mod(p);

        BigInteger X3 = AA.multiply(BB).mod(p);
        BigInteger Z3 = E.multiply(
            BB.add(A24.multiply(E)).mod(p)
        ).mod(p);

        return new BigInteger[] {X3, Z3};
    }

    static BigInteger eaGCD (BigInteger A, BigInteger B) {
        //Find gcd such that a > b
        BigInteger remainder = BigInteger.ZERO;
        BigInteger a = A;
        BigInteger b = B;
        BigInteger divisor;
        BigInteger quotient;
        do {
            quotient = a.divide(b);
            divisor = quotient.multiply(b);
            remainder = a.subtract(divisor);
            
            a = b;

            if(!remainder.equals(BigInteger.ZERO)) {
                b = remainder;
            }
        } while(!remainder.equals(BigInteger.ZERO));

        return b;
    }

    static BigInteger inverseMod (BigInteger a, BigInteger m) {
        BigInteger t = BigInteger.ZERO;
        BigInteger newt = BigInteger.ONE;

        BigInteger r = m;
        BigInteger newr = a;
        BigInteger quotient;

        while(!newr.equals(BigInteger.ZERO)) {
            quotient = r.divide(newr);

            //Save old t
            BigInteger tempT = t;
            t = newt;
            newt = tempT.subtract(quotient.multiply(newt));


            //Save old r
            BigInteger tempR = r;
            r = newr;
            newr = tempR.subtract(quotient.multiply(newr));
            
        }

        if(!r.equals(BigInteger.ONE)) {
            System.out.println("No Inverse.");
            return new BigInteger("-1");
        }
        if(t.signum() < 0) t = t.add(m);
        return t;
    }


    // HASHING FUNCTIONS
    private byte[] SHA256 (byte[] message) {
        //Initialize hash values:
        //(first 32 bits of the fractional parts of the square roots of the first 8 primes 2..19):
        int h0 = 0x6a09e667;
        int h1 = 0xbb67ae85;
        int h2 = 0x3c6ef372;
        int h3 = 0xa54ff53a;
        int h4 = 0x510e527f;
        int h5 = 0x9b05688c;
        int h6 = 0x1f83d9ab;
        int h7 = 0x5be0cd19;

        //Initialize array of round constants:
        //(first 32 bits of the fractional parts of the cube roots of the first 64 primes 2..311):
        int[] k = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        };


        //Length in bytes
        //(K << 3) is K in bits converted to bytes
        //Total padded length = original message length + byte length of (extra '1' bit + K bits)
        
        //Find the number of '0' bits to pad, which is the smallest K >= 0 such that L + 1 + K + 64 is a multiple of 512
        //To make the total length of the message a multiple of 512 after padding (64 bytes)
        int totalPaddingBytes = (64 - ((message.length + 1 + 8) % 64)) % 64; //8 bytes for the length field

        byte[] m = new byte[message.length + 1 + totalPaddingBytes + 8]; //8 bytes for the length field

        System.arraycopy(message, 0, m, 0, message.length);

        //Append the bit '1' to the message
        m[message.length] = (byte) 0x80; //10000000


        //Append K '0' bits to the message; start at the index of the "1" bit;
        for(int i = message.length + 1; i < m.length - 8; i++) {
            m[i] = 0x00; //Zero out the padding bytes
        }

        //Append L as a 64-bit big-endian integer

        long bitLength = (long) message.length * 8; //long accounts for messages longer than 2^32 bits
        for(int i = 0; i < 8; i++) {
            m[m.length - 1 - i] = (byte) ((bitLength >>> (i * 8)) & 0xFF);
        }

        /*
            Such that the bits in the message are:
            <original message of length L>
            1
            <K zeros>
            <L as 64 bit integer>
            
            (the number of bits will be a multiple of 512)
        */

        //Process message in 512-bit chunks
        for(int i = 0; i < m.length / 64; i++) {
            //The chunk we are working with
            byte[] chunk = new byte[64];
            for(int j = 0; j < 64; j++) {
                chunk[j] = m[i * 64 + j];
            }

            //Split chunk into sixteen 32-bit big-endian words w[0..15]
            int[] w = new int[64];
            for(int j = 0; j < 64; j++) {
                w[j] = 0; //Zero out
            }

            for(int j = 0; j < 16; j++) {
                //Four chunk elements = one word
                w[j] = ((chunk[j * 4] & 0xFF) << 24) | ((chunk[j * 4 + 1] & 0xFF) << 16) | ((chunk[j * 4 + 2] & 0xFF) << 8) | (chunk[j * 4 + 3] & 0xFF);
            }
            //Then we extend the 16 words into 64 words w[16..63] of the message schedule array:

            for(int j = 16; j < 64; j++) {
                /*
                    Example:
                    01100101 -->
                    10110010 -->
                    01011001 -->
                    10101100 -->
                    01010110 -->
                    
                    (rotating right)
                */
                int s0 = Integer.rotateRight(w[j - 15], 7) ^ Integer.rotateRight(w[j - 15], 18) ^ (w[j - 15] >>> 3);
                int s1 = Integer.rotateRight(w[j - 2], 17) ^ Integer.rotateRight(w[j - 2], 19) ^ (w[j - 2] >>> 10);
                w[j] = w[j - 16] + s0 + w[j - 7] + s1;
            }

            //Initialize working variables to current hash value

            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = h5;
            int g = h6;
            int h = h7;

            //Compression function main loop
            //THE ULTIMATE MIXING BOWL
            //SCRAMBLE, MASH, STIR, WHISK, FLIP, AND REPEAT
            //NOW DO IT ANOTHER 63 TIMES
            //THEN BURN THE REST
            //PASS IT THROUGH A FEW MORE TIMES FOR GOOD MEASURE
            //THEN THROUGH THE DIGESTIVE TRACT OF A COW
            //SUCK IT THROUGH A BLACK HOLE
            //THEN YOU'RE DONE... OR MAYBE NOT
            //JUST TO BE SAFE, DO IT ALL AGAIN
            //AND AGAIN
            //AND AFTER THAT, ONCE MORE FOR LUCK
            //AND ONE LAST TIME, BECAUSE WHY NOT
            //NOW YOU'RE REALLY DONE
            //CONGRATULATIONS, YOU'VE CREATED A HASH FUNCTION

            for(int j = 0; j < 64; j++) {
                int S1 = Integer.rotateRight(e, 6) ^ Integer.rotateRight(e, 11) ^ Integer.rotateRight(e, 25);
                int ch = (e & f) ^ ((~e) & g);
                int temp1 = h + S1 + ch + k[j] + w[j];
                int S0 = Integer.rotateRight(a, 2) ^ Integer.rotateRight(a, 13) ^ Integer.rotateRight(a, 22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }

            //FINALLY, ADD THE ANNIHILATED PIECES AS FROSTING
            //yum
            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;
            h5 += f;
            h6 += g;
            h7 += h;
        }

        byte[] digest = new byte[32];

        //Produce the final hash value (big-endian):
        //SERVE THE YUMMY HASH CAKE
        for(int i = 0; i < 4; i++) {
            digest[i] = (byte) ((h0 >>> (24 - i * 8)) & 0xFF);
            digest[i + 4] = (byte) ((h1 >>> (24 - i * 8)) & 0xFF);
            digest[i + 8] = (byte) ((h2 >>> (24 - i * 8)) & 0xFF);
            digest[i + 12] = (byte) ((h3 >>> (24 - i * 8)) & 0xFF);
            digest[i + 16] = (byte) ((h4 >>> (24 - i * 8)) & 0xFF);
            digest[i + 20] = (byte) ((h5 >>> (24 - i * 8)) & 0xFF);
            digest[i + 24] = (byte) ((h6 >>> (24 - i * 8)) & 0xFF);
            digest[i + 28] = (byte) ((h7 >>> (24 - i * 8)) & 0xFF);
        }

        return digest;
    }

    private byte[] SHA384 (byte[] message) {
        //Initialize hash values:
        //(first 64 bits of the fractional parts of the square roots of the first 8 primes 2..19):
        long h0 = 0xcbbb9d5dc1059ed8L;
        long h1 = 0x629a292a367cd507L;
        long h2 = 0x9159015a3070dd17L;
        long h3 = 0x152fecd8f70e5939L;
        long h4 = 0x67332667ffc00b31L;
        long h5 = 0x8eb44a8768581511L;
        long h6 = 0xdb0c2e0d64f98fa7L;
        long h7 = 0x47b5481dbefa4fa4L;

        //Initialize array of round constants:
        //(first 64 bits of the fractional parts of the cube roots of the first 64 primes 2..311):
        long[] k = {
            0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL, 0x3956c25bf348b538L, 
            0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L, 0xd807aa98a3030242L, 0x12835b0145706fbeL, 
            0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L, 0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L, 
            0xc19bf174cf692694L, 0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L, 
            0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L, 0x983e5152ee66dfabL, 
            0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L, 0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 
            0x06ca6351e003826fL, 0x142929670a0e6e70L, 0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 
            0x53380d139d95b3dfL, 0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL, 
            0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L, 0xd192e819d6ef5218L, 
            0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L, 0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 
            0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L, 0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L, 
            0x682e6ff3d6b2b8a3L, 0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL, 
            0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL, 0xca273eceea26619cL, 
            0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L, 0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L, 
            0x113f9804bef90daeL, 0x1b710b35131c471bL, 0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL, 
            0x431d67c49c100d4cL, 0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L
        };

        //Padding

        //Find the number of '0' bits to pad, which is the smallest K >= 0 such that L + 1 + K + 128 is a multiple of 1024
        //To make the total length of the message a multiple of 1024 after padding (128 bytes)
        //Length in bytes
        //(K << 3) is K in bits converted to bytes
        //Total padded length = original message length + byte length of (extra '1' bit + K bits)
        // 
        // 
        int totalPaddingBytes = (128 - ((message.length + 1 + 16) % 128)) % 128; //16 bytes for the length field

        byte[] m = new byte[message.length + 1 + totalPaddingBytes + 16];

        System.arraycopy(message, 0, m, 0, message.length);

        //Append the bit '1' to the message
        m[message.length] = (byte) 0x80; //10000000


        //Append K '0' bits to the message; start at the index of the "1" bit;
        for(int i = message.length + 1; i < message.length + 1 + totalPaddingBytes; i++) {
            m[i] = 0x00; //Zero out the padding bytes
        }

        //Append L as a 128-bit big-endian integer
        long bitLength = (long) message.length * 8; //long may not account for messages longer than 2^64 bits
        for(int i = 0; i < 16; i++) {
            if (i < 8) 
                m[m.length - 16 + i] = 0; // upper 64 bits = 0 for short messages
            else 
                m[m.length - 16 + i] = (byte) ((bitLength >>> (8*(15-i))) & 0xFF);
        }

        /*
            Such that the bits in the message are:
            <original message of length L>
            1
            <K zeros>
            <L as 64 bit integer>
            
            (the number of bits will be a multiple of 512)
        */

        //Process message in 1024-bit chunks
        for(int i = 0; i < m.length / 128; i++) {
            //The chunk we are working with
            byte[] chunk = new byte[128];
            for(int j = 0; j < 128; j++) {
                chunk[j] = m[i * 128 + j];
            }

            //Split chunk into sixteen 64-bit big-endian words w[0..15]
            long[] w = new long[80]; //Eighty total words for SHA-512
            for(int j = 0; j < 80; j++) {
                w[j] = 0; //Zero out
            }

            for(int j = 0; j < 16; j++) {
                //Eight chunk elements = one word
                w[j] = ((long) (chunk[j * 8]     & 0xFF) << 56) |
                       ((long) (chunk[j * 8 + 1] & 0xFF) << 48) |
                       ((long) (chunk[j * 8 + 2] & 0xFF) << 40) |
                       ((long) (chunk[j * 8 + 3] & 0xFF) << 32) |
                       ((long) (chunk[j * 8 + 4] & 0xFF) << 24) |
                       ((long) (chunk[j * 8 + 5] & 0xFF) << 16) |
                       ((long) (chunk[j * 8 + 6] & 0xFF) << 8)  |
                       ((long) (chunk[j * 8 + 7] & 0xFF));
            }
            //Then we extend the 16 words into 80 words w[16..79] of the message schedule array:

            for(int j = 16; j < 80; j++) {
                /*
                    SHA512 specific bitwise operations
                */
                long s0 = Long.rotateRight(w[j - 15], 1) ^ Long.rotateRight(w[j - 15], 8) ^ (w[j - 15] >>> 7);
                long s1 = Long.rotateRight(w[j - 2], 19) ^ Long.rotateRight(w[j - 2], 61) ^ (w[j - 2] >>> 6);
                w[j] = (long) w[j - 16] + s0 + w[j - 7] + s1;
            }

            //Initialize working variables to current hash value

            long a = h0;
            long b = h1;
            long c = h2;
            long d = h3;
            long e = h4;
            long f = h5;
            long g = h6;
            long h = h7;

            //Compression function main loop
            for(int j = 0; j < 80; j++) {
                long S1 = Long.rotateRight(e, 14) ^ Long.rotateRight(e, 18) ^ Long.rotateRight(e, 41);
                long ch = (e & f) ^ ((~e) & g);
                long temp1 = h + S1 + ch + k[j] + w[j];
                long S0 = Long.rotateRight(a, 28) ^ Long.rotateRight(a, 34) ^ Long.rotateRight(a, 39);
                long maj = (a & b) ^ (a & c) ^ (b & c);
                long temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }

            //FINALLY, ADD THE ANNIHILATED PIECES AS FROSTING
            //yum
            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;
            h5 += f;
            h6 += g;
            h7 += h;
        }

        byte[] digest = new byte[48];

        //Produce the final hash value (big-endian):
        //SERVE THE YUMMY HASH CAKE
        for(int i = 0; i < 8; i++) {
            digest[i] = (byte) ((h0 >>> (56 - i * 8)) & 0xFF);
            digest[i + 8] = (byte) ((h1 >>> (56 - i * 8)) & 0xFF);
            digest[i + 16] = (byte) ((h2 >>> (56 - i * 8)) & 0xFF);
            digest[i + 24] = (byte) ((h3 >>> (56 - i * 8)) & 0xFF);
            digest[i + 32] = (byte) ((h4 >>> (56 - i * 8)) & 0xFF);
            digest[i + 40] = (byte) ((h5 >>> (56 - i * 8)) & 0xFF);
        }

        return digest;
    }
    
    private byte[] SHA512 (byte[] message) {
        //Initialize hash values:
        //(first 64 bits of the fractional parts of the square roots of the first 8 primes 2..19):
        long h0 = 0x6a09e667f3bcc908L;
        long h1 = 0xbb67ae8584caa73bL;
        long h2 = 0x3c6ef372fe94f82bL;
        long h3 = 0xa54ff53a5f1d36f1L;
        long h4 = 0x510e527fade682d1L;
        long h5 = 0x9b05688c2b3e6c1fL;
        long h6 = 0x1f83d9abfb41bd6bL;
        long h7 = 0x5be0cd19137e2179L;

        //Initialize array of round constants:
        //(first 64 bits of the fractional parts of the cube roots of the first 64 primes 2..311):
        long[] k = {
            0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL, 0x3956c25bf348b538L, 
            0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L, 0xd807aa98a3030242L, 0x12835b0145706fbeL, 
            0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L, 0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L, 
            0xc19bf174cf692694L, 0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L, 
            0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L, 0x983e5152ee66dfabL, 
            0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L, 0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 
            0x06ca6351e003826fL, 0x142929670a0e6e70L, 0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 
            0x53380d139d95b3dfL, 0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL, 
            0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L, 0xd192e819d6ef5218L, 
            0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L, 0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 
            0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L, 0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L, 
            0x682e6ff3d6b2b8a3L, 0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL, 
            0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL, 0xca273eceea26619cL, 
            0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L, 0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L, 
            0x113f9804bef90daeL, 0x1b710b35131c471bL, 0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL, 
            0x431d67c49c100d4cL, 0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L
        };

        //Padding

        //Find the number of '0' bits to pad, which is the smallest K >= 0 such that L + 1 + K + 128 is a multiple of 1024
        //To make the total length of the message a multiple of 1024 after padding (128 bytes)
        //Length in bytes
        //(K << 3) is K in bits converted to bytes
        //Total padded length = original message length + byte length of (extra '1' bit + K bits)
        // 
        // 
        int totalPaddingBytes = (128 - ((message.length + 1 + 16) % 128)) % 128; //16 bytes for the length field

        byte[] m = new byte[message.length + 1 + totalPaddingBytes + 16];

        System.arraycopy(message, 0, m, 0, message.length);

        //Append the bit '1' to the message
        m[message.length] = (byte) 0x80; //10000000


        //Append K '0' bits to the message; start at the index of the "1" bit;
        for(int i = message.length + 1; i < message.length + 1 + totalPaddingBytes; i++) {
            m[i] = 0x00; //Zero out the padding bytes
        }

        //Append L as a 128-bit big-endian integer
        long bitLength = (long) message.length * 8; //long may not account for messages longer than 2^64 bits
        for(int i = 0; i < 16; i++) {
            if (i < 8) 
                m[m.length - 16 + i] = 0; // upper 64 bits = 0 for short messages
            else 
                m[m.length - 16 + i] = (byte) ((bitLength >>> (8*(15-i))) & 0xFF);
        }

        /*
            Such that the bits in the message are:
            <original message of length L>
            1
            <K zeros>
            <L as 64 bit integer>
            
            (the number of bits will be a multiple of 512)
        */

        //Process message in 1024-bit chunks
        for(int i = 0; i < m.length / 128; i++) {
            //The chunk we are working with
            byte[] chunk = new byte[128];
            for(int j = 0; j < 128; j++) {
                chunk[j] = m[i * 128 + j];
            }

            //Split chunk into sixteen 64-bit big-endian words w[0..15]
            long[] w = new long[80]; //Eighty total words for SHA-512
            for(int j = 0; j < 80; j++) {
                w[j] = 0; //Zero out
            }

            for(int j = 0; j < 16; j++) {
                //Eight chunk elements = one word
                w[j] = ((long) (chunk[j * 8]     & 0xFF) << 56) |
                       ((long) (chunk[j * 8 + 1] & 0xFF) << 48) |
                       ((long) (chunk[j * 8 + 2] & 0xFF) << 40) |
                       ((long) (chunk[j * 8 + 3] & 0xFF) << 32) |
                       ((long) (chunk[j * 8 + 4] & 0xFF) << 24) |
                       ((long) (chunk[j * 8 + 5] & 0xFF) << 16) |
                       ((long) (chunk[j * 8 + 6] & 0xFF) << 8)  |
                       ((long) (chunk[j * 8 + 7] & 0xFF));
            }
            //Then we extend the 16 words into 80 words w[16..79] of the message schedule array:

            for(int j = 16; j < 80; j++) {
                /*
                    SHA512 specific bitwise operations
                */
                long s0 = Long.rotateRight(w[j - 15], 1) ^ Long.rotateRight(w[j - 15], 8) ^ (w[j - 15] >>> 7);
                long s1 = Long.rotateRight(w[j - 2], 19) ^ Long.rotateRight(w[j - 2], 61) ^ (w[j - 2] >>> 6);
                w[j] = (long) w[j - 16] + s0 + w[j - 7] + s1;
            }

            //Initialize working variables to current hash value

            long a = h0;
            long b = h1;
            long c = h2;
            long d = h3;
            long e = h4;
            long f = h5;
            long g = h6;
            long h = h7;

            //Compression function main loop
            for(int j = 0; j < 80; j++) {
                long S1 = Long.rotateRight(e, 14) ^ Long.rotateRight(e, 18) ^ Long.rotateRight(e, 41);
                long ch = (e & f) ^ ((~e) & g);
                long temp1 = h + S1 + ch + k[j] + w[j];
                long S0 = Long.rotateRight(a, 28) ^ Long.rotateRight(a, 34) ^ Long.rotateRight(a, 39);
                long maj = (a & b) ^ (a & c) ^ (b & c);
                long temp2 = S0 + maj;

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }

            //FINALLY, ADD THE ANNIHILATED PIECES AS FROSTING
            //yum
            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;
            h5 += f;
            h6 += g;
            h7 += h;
        }

        byte[] digest = new byte[64];

        //Produce the final hash value (big-endian):
        //SERVE THE YUMMY HASH CAKE
        for(int i = 0; i < 8; i++) {
            int n = 56 - i * 8;
            digest[i] = (byte) ((h0 >>> (n)) & 0xFF);
            digest[i + 8] = (byte) ((h1 >>> (n)) & 0xFF);
            digest[i + 16] = (byte) ((h2 >>> (n)) & 0xFF);
            digest[i + 24] = (byte) ((h3 >>> (n)) & 0xFF);
            digest[i + 32] = (byte) ((h4 >>> (n)) & 0xFF);
            digest[i + 40] = (byte) ((h5 >>> (n)) & 0xFF);
            digest[i + 48] = (byte) ((h6 >>> (n)) & 0xFF);
            digest[i + 56] = (byte) ((h7 >>> (n)) & 0xFF);
        }

        return digest;
    }

    /*
        HKDF ALGORITHM

        1. HKDF-EXTRACT

            PRK = Pseudorandom Key

            HMAC-HASH(salt, message)


     */

    public byte[] HMAC_HASH(SHA whichSHA, byte[] key, byte[] message) {
        byte[] k_opad = new byte[messageBlockSize(whichSHA)];
        int i;
        int blockSize;
        byte[] k_ipad = new byte[messageBlockSize(whichSHA)];
        byte[] temp;

        blockSize = messageBlockSize(whichSHA);

        if(key.length > blockSize) {// Hash the salt if too long
            key = SHA(whichSHA, key);
        }

        /* store key into the pads, XOR'd with ipad and opad values */
        for (i = 0; i < key.length; i++) {
            k_ipad[i] = (byte)(key[i] ^ 0x36);
            k_opad[i] = (byte)(key[i] ^ 0x5C);
        }
        /* remaining pad bytes are '\0' XOR'd with ipad and opad values */
        for ( ; i < blockSize; i++) {
            k_ipad[i] = (byte)0x36;
            k_opad[i] = (byte)0x5C;
        }

        //HMAC Input and Result
        temp = new byte[k_ipad.length+message.length];
        System.arraycopy(k_ipad, 0, temp, 0, k_ipad.length);
        System.arraycopy(message, 0, temp, k_ipad.length, message.length);

        byte[] digest = SHA(whichSHA, temp);

        temp = new byte[k_opad.length+digest.length];
        System.arraycopy(k_opad, 0, temp, 0, k_opad.length);
        System.arraycopy(digest, 0, temp, k_opad.length, digest.length);
        return SHA(whichSHA, temp);
    }

    public byte[] HKDF_Extract(SHA whichSHA, byte[] salt, byte[] IKM) {
        //Extract
        byte[] prk = HMAC_HASH(whichSHA, salt, IKM);

        return prk;
    }

    public byte[] HKDF_Expand(SHA whichSHA, byte[] prk, byte[] info, int L) throws IOException {
        int hashSize = hashSize(whichSHA);
        if(L > 255*hashSize) throw new IOException("L cannot be longer than 255*Hashlen.");

        int N = (int) Math.ceil((double) L/hashSize);
        ByteArrayOutputStream T = new ByteArrayOutputStream();
        byte[] okm = new byte[L];

        byte[] previous = new byte[0];
        for(int i = 0x01; i <= N; i++) {
            // T(i) = HMAC_HASH(PRK, T(i-1) | info | 0x[i])
            byte[] concatenated = new byte[previous.length + info.length + 1];
            System.arraycopy(previous, 0, concatenated, 0, previous.length);
            System.arraycopy(info, 0, concatenated, previous.length, info.length);
            concatenated[previous.length + info.length] = (byte) (i & 0xFF);

            byte[] t_i = HMAC_HASH(whichSHA, prk, concatenated);

            T.write(t_i, 0, t_i.length);
            previous = t_i;
        }

        //First L OCTETS of T

        System.arraycopy(T.toByteArray(), 0, okm, 0, L);
        return okm;
    }

    public byte[] HKDF_Expand_Label(byte[] secret, byte[] label, byte[] context, int length) throws IOException {
        byte[] HkdfLabel = new byte[16+7+label.length+context.length];

        return HKDF_Expand(SHA._384, secret, HkdfLabel, length);
    }

    public byte[] AES_128_GCM(){}
}