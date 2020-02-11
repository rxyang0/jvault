package com.yangrichard.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VaultTest {

    private Vault vault;
    private static final String TEST_PATH = "/Users/premiumrich/Desktop/test-vault/";

    @BeforeEach
    public void runBefore() {
        vault = new Vault(TEST_PATH);
    }

    @Test
    public void testNewVault() {
        assertEquals(TEST_PATH, vault.getPathToVault());
    }

}
