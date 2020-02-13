package exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CryptoExceptionTest {

    private CryptoException e;

    @BeforeEach
    public void runBefore() {
        e = new CryptoException(new Exception("test"));
    }

    @Test
    public void testConstructor() {
        assertEquals("java.lang.Exception: test", e.getMessage());
    }

}
