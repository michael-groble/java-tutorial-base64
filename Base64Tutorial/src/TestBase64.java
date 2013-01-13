import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class TestBase64 {
  private static final String base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

  /**
   * Step 1.
   * Implement {@link Base64#index} to pass this test.
   */
  @Test
  public void index() {
    char[] chars = base64Chars.toCharArray();
    for (int i = 0; i < chars.length; ++i) {
      assertEquals(i, Base64.index(chars[i]));
    }
    for (char c = Character.MIN_VALUE; c < Character.MAX_VALUE; ++c) {
      if (base64Chars.indexOf(c) < 0) {
        assertEquals(-1, Base64.index(c));
      }
    }
  }
  
  /**
   * Step 2.
   * Use {@link Base64#index} to implement {@link Base64#isValid} to pass this test.
   */
  @Test
  public void validBase64() {
    for (char c : base64Chars.toCharArray()) {
      assertTrue(Base64.isValid(c));
    }
    for (char c = Character.MIN_VALUE; c < Character.MAX_VALUE; ++c) {
      if (base64Chars.indexOf(c) < 0) {
        assertFalse(Base64.isValid(c));
      }
    }
  }

  /**
   * Step 3.
   * Implement {@link Base64#isPad} to pass this test.
   */
  @Test
  public void pad() {
    assertTrue(Base64.isPad('='));
    assertFalse(Base64.isPad('\\'));
  }
  
  private InputStream assertBytesToBase64(byte[] bytes, String base64) throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
    ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
    StringBuilder outChars = new StringBuilder();
    
    Base64.Block block = new Base64.Block();
    int expectedBytesRead = Math.min(bytes.length,3);
    assertEquals(expectedBytesRead, block.readBytesFrom(in));
    assertEquals(expectedBytesRead, block.writeBytesTo(outBytes));
    assertArrayEquals(Arrays.copyOf(bytes, expectedBytesRead), outBytes.toByteArray());
    assertEquals(base64.length(), block.writeBase64CharsTo(outChars));
    assertEquals(base64, outChars.toString());
    return in;
  }
  
  /**
   * Step 4.
   * Implement {@link Base64.Block#readBytesFrom}, {@link Base64.Block#writeBytesTo} and
   * {@link Base64.Block#writeBase64CharsTo} to pass the following tests.
   */
  @Test
  public void blockWithZeroBytes() throws IOException {
    assertBytesToBase64(new byte[0], "");
  }
  
  @Test
  public void blockWithOneBytes() throws IOException {
    assertBytesToBase64(new byte[] {(byte)0b101101_11}, "tw==");
    assertBytesToBase64(new byte[] {(byte)0b110101_01}, "1Q==");
    assertBytesToBase64(new byte[] {(byte)0b000111_00}, "HA==");
  }
  
  @Test
  public void blockWithTwoBytes() throws IOException {
    assertBytesToBase64(new byte[] {(byte)0b110110_01, (byte)0b1111_1011}, "2fs=");
    assertBytesToBase64(new byte[] {(byte)0b001010_11, (byte)0b1110_0101}, "K+U=");
    assertBytesToBase64(new byte[] {(byte)0b011110_10, (byte)0b0000_0110}, "egY=");
  }
  
  @Test
  public void blockWithThreeBytes() throws IOException {
    assertBytesToBase64(new byte[] {(byte)0b001010_01, (byte)0b1010_1001, (byte)0b00_000000}, "KakA");
    assertBytesToBase64(new byte[] {(byte)0b011010_11, (byte)0b1111_0111, (byte)0b00_111011}, "a/c7");
    assertBytesToBase64(new byte[] {(byte)0b000001_11, (byte)0b0111_0111, (byte)0b11_101110}, "B3fu");
  }
  
  @Test
  public void blockWithFourBytes() throws IOException {
    byte[] bytes = {(byte)0b001010_01, (byte)0b1010_1001, (byte)0b00_000000, (byte)0b011100_11};
    InputStream in = assertBytesToBase64(bytes, "KakA");
    byte[] remaining = new byte[2];
    assertEquals(1,in.read(remaining));
    assertEquals(remaining[0],bytes[3]);
  }
  
  private Reader assertBase64ToBytes(String base64, byte[] bytes) throws IOException {
    StringReader in = new StringReader(base64);
    ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
    StringBuilder outChars = new StringBuilder();
    
    Base64.Block block = new Base64.Block();
    int expectedCharsRead = Math.min(base64.length(), 4);
    assertEquals(expectedCharsRead, block.readBase64CharsFrom(in));
    assertEquals(expectedCharsRead, block.writeBase64CharsTo(outChars));
    assertEquals(base64.substring(0,expectedCharsRead), outChars.toString());
    assertEquals(bytes.length, block.writeBytesTo(outBytes));
    assertArrayEquals(bytes, outBytes.toByteArray());
    
    return in;
  }
  
  /**
   * Step 5.
   * Implement {@link Base64.Block#readBase64CharsFrom} to pass the following tests.
   */
  @Test
  public void blockZeroChars() throws IOException {
    assertBase64ToBytes("", new byte[0]);
  }
  
  @Test
  public void blockTwoChars() throws IOException {
    assertBase64ToBytes("+w==", new byte[] {(byte)0b111110_11});
    assertBase64ToBytes("mQ==", new byte[] {(byte)0b100110_01});
  }
  
  @Test
  public void blockThreeChars() throws IOException {
    assertBase64ToBytes("gR8=", new byte[] {(byte)0b100000_01, (byte)0b0001_1111});
    assertBase64ToBytes("1/E=", new byte[] {(byte)0b110101_11, (byte)0b1111_0001});
  }
  
  @Test
  public void blockWithFourChars() throws IOException {
    assertBase64ToBytes("aB2Z", new byte[] {(byte)0b011010_00, (byte)0b0001_1101, (byte)0b10_011001});
    assertBase64ToBytes("/A/A", new byte[] {(byte)0b111111_00, (byte)0b0000_1111, (byte)0b11_000000});
  }
  
  @Test
  public void blockWithFiveChars() throws IOException {
    String chars = "12345";
    Reader in = assertBase64ToBytes(chars, new byte[] {(byte)0b110101_11,(byte)0b0110_1101,(byte)0b11_111000});
    char[] remaining = new char[2];
    assertEquals(1,in.read(remaining));
    assertEquals(remaining[0],chars.charAt(4));
  }
  
  /**
   * Step 6.
   * Implement {@link Base64#encode} and {@link Base64#decode} to pass this test.
   */
  @Test
  public void testRoundTrip() throws IOException {
    byte[] bytes = new byte[1024];
    new Random().nextBytes(bytes);
    StringBuilder sb = new StringBuilder();
    ByteArrayInputStream inBytes = new ByteArrayInputStream(bytes);
    int expectedChars = 4 * (int) Math.ceil(bytes.length / 3.0);
    assertEquals(expectedChars, Base64.encode(inBytes,sb));
    ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
    assertEquals(bytes.length, Base64.decode(new StringReader(sb.toString()), outBytes));
    assertArrayEquals(bytes, outBytes.toByteArray());
  }
  
  /**
   * Step 7.
   * Make sure {@link Base64.Block#readBase64CharsFrom} and {@link Base64.Block#validatedLength} detects
   * these errors 
   */
  @Test(expected=IOException.class)
  public void blockWithInvalidLength() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("AA"));
  }
   
  @Test(expected=IOException.class)
  public void blockWithInvalidThreePad() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("A==="));
  }
  
  @Test(expected=IOException.class)
  public void blockWithInvalidMidPad() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("AA=A"));
  }
  
  /**
   * Step 8.
   * Make sure {@link Base64.Block#isValidCharBeforeDoublePad} detects these errors. 
   */
  @Test(expected=IOException.class)
  public void blockWithInvalidTwoPad_BitOne() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("AB=="));
  }

  @Test(expected=IOException.class)
  public void blockWithInvalidTwoPad_BitTwo() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("AS=="));
  }
  
  @Test(expected=IOException.class)
  public void blockWithInvalidTwoPad_BitThree() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("Ak=="));
  }
  
  @Test(expected=IOException.class)
  public void blockWithInvalidTwoPad_BitFour() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("A4=="));
  }
  
  /**
   * Step 9.
   * Make sure {@link Base64.Block#isValidCharBeforeSinglePad} detects these errors. 
   */
  @Test(expected=IOException.class)
  public void blockWithInvalidOnePad_BitOne() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("AAB="));
  }
  
  @Test(expected=IOException.class)
  public void blockWithInvalidOnePad_BitTwo() throws IOException {
    new Base64.Block().readBase64CharsFrom(new StringReader("AA+="));
  }
  
  /**
   * Step 10.
   * Make sure {@link Base64.decode} detects this error.
   */
  @Test(expected=IOException.class)
  public void decodeWithPrematurePadding() throws IOException {
    // each block is individually correct, but not as a single item
    Base64.decode(new StringReader("gR8=gR8="), new ByteArrayOutputStream());
  }
}
