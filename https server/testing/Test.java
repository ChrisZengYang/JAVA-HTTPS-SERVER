package testing;
import algorithms.*;
import java.io.IOException;

public class Test {
    public static void main (String[] args) throws IOException {
        Algorithms alg = new Algorithms();

        byte[] digest = alg.HKDF_Extract(SHA._256,
        Algorithms.hexToByteArray(""),
        Algorithms.hexToByteArray("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b")
        );
        // display digest as a hex string
        System.out.println("Extract: " + Algorithms.bytesToHex(digest));
        digest = alg.HKDF_Expand(SHA._256,
        digest,
        Algorithms.hexToByteArray(""),
        42
        );
        /*
        RFC Test Vector:
          
          b11e398dc80327a1c8e7f78c596a4934
          4f012eda2d4efad8a050cc4c19afa97c
          59045a99cac7827271cb41c65e590e09
          da3275600c2f09b8367793a9aca3db71
          cc30c58179ec3e87c14c01d5c1f3434f
          1d87
        
        This program:
          b11e398dc80327a1c8e7f78c596a4934
          4f012eda2d4efad8a050cc4c19afa97c
          59045a99cac7827271cb41c65e590e09
          da3275600c2f09b8367793a9aca3db71
          3155ed32d21d5638a12241c0b461e022
          d8d9*/
        System.out.println("Expand: " + Algorithms.bytesToHex(digest));
    }
}