import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Utility class for encoding and decoding Base64
 *
 */
public class Base64 {
  
  private static final int[] indices = new int[128];
  private static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
  private static final char PAD = '=';
  
  static {
    // TODO fill in block to correctly initialize indices
  }

  /**
   * Index of Base64 character.
   * 
   * @param c character
   * @return index (0-63) of the corresponding Base64 character, or -1 if not a Base64 character
   */
  public static int index(char c) {
    return -1;
  }

  /**
   * Determine if character is valid Base64.
   * 
   * @param c character
   * @return true if character is Base64, e.g. A-Z, a-z, 0-9, + or /
   */
  public static boolean isValid(char c) {
    return false;
  }

  /**
   * Determine if character is the Base64 pad character.
   * 
   * @param c character
   * @return true if pad '='
   */
  public static boolean isPad(char c) {
    return false;
  }
  
  /**
   * Block of Base64 characters.
   * 
   * Every 3 bytes are mapped to 4 6-bit characters.
   * 
   * <pre>
   *       packed bytes
   * /  p0  \/  p1  \/  p2 \
   * 876543218765432187654321
   *
   * 654321654321654321654321
   * \ e0 /\ e1 /\ e2 /\ e3 /
   *      expanded bytes
   * </pre>
   */
  public static class Block {
    private final byte[] packed = new byte[3];
    private final int[] index = new int[4];
    private final char[] expanded = new char[4];
    private int nExpanded = 0;
    
    /**
     * Read up to three bytes from the input stream.
     * 
     * @param in
     * @return number of bytes read from input
     * @throws IOException
     */
    public int readBytesFrom(InputStream in) throws IOException {
      return 0;
    }
    
    /**
     * Convert index values (0-63) to corresponding Base64 characters, adding pad characters as needed
     */
    private void setExpandedFromIndex() {

    }
    
    /**
     * Read 4 characters from the input stream.
     * 
     * @param in
     * @return number of characters read, including padding, if any pad characters
     * @throws IOException if input has characters, but not 4 or if the encoded characters
     *         are not strictly Base64 encoded.
     */
    public int readBase64CharsFrom(Reader in) throws IOException {
      return 0;
    }

    /**
     * Convert Baase64 character values to corresponding index values.
     * 
     * @throws IOException if characters are not valid Base64 chars
     */
    private void setIndexFromExpanded() throws IOException {

    }
    
    /**
     * Write the block as Base64 characters to the output.
     * 
     * Typically called after {@link #readBytesFrom}.
     * 
     * @param out
     * @return number of characters written, including padding.
     * @throws IOException
     */
    public int writeBase64CharsTo(Appendable out) throws IOException {
      return 0;
    }
    
    /**
     * Write the block as bytes to the output.
     * 
     * Typically called after {@link #readBase64CharsFrom}.
     * @param out
     * @return number of bytes written.
     * @throws IOException
     */
    public int writeBytesTo(OutputStream out) throws IOException {
      return 0;
    }
    
    /**
     * Validate the padding of the Base64 characters.
     * @param chars block of 4 Base64 characters
     * @return number of unpadded characters: 2, 3 or 4
     */
    private int validatedLength(char[] chars) {
      return 0;
    }

    /**
     * Validate that the character is allowable before a single pad.
     * 
     * A single pad is used when we don't have a third byte
     * so the previous base 64 character must have low-order 0 bits
     * <pre>
     * /byte 1\/byte 2\/byte 3\
     * 876543218765432187654321
     *                 00000000
     * 654321654321654321654321
     * \64-1/\64-2/\64-3/\64-4/
     *  any   any    ?     =
     * </pre>
     * @param c
     * @return true if valid
     */
    private static boolean isValidCharBeforeSinglePad(char c) {
      return true;
    }

    /**
     * Validate that the character is allowable before a double pad.
     * 
     * A double pad is used when we only have 1 byte to encode
     * <pre>
     * /byte 1\/byte 2\/byte 3\
     * 876543218765432187654321
     *         0000000000000000
     * 654321654321654321654321
     * \64-1/\64-2/\64-3/\64-4/
     *  any    ?     =     =
     * </pre>
     * @param c
     * @return true if valid
     */
    private static boolean isValidCharBeforeDoublePad(char c) {
      return true;
    }    
  }
  
  /**
   * Encode the input stream of bytes as Base64 characters to the output.
   * 
   * @param in
   * @param out
   * @return number of characters writtent to the output
   * @throws IOException
   */
  public static int encode(InputStream in, Appendable out) throws IOException {
    return 0;
  }
  
  /**
   * Decode the input of Base64 characters as bytes to the output stream.
   * @param in
   * @param out
   * @return
   * @throws IOException if the input is not strictly Base64 encoded
   */
  public static int decode(Reader in, OutputStream out) throws IOException {
    return 0;
  }
}
