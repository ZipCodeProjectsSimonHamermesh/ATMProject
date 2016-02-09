package io.froilanandfriends.atm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by willhorton on 2/8/16.
 */
public class TestATM {

    ATM atm;

    @Before
    public void setUp() throws Exception {
        atm = ATM.getATM();
    }

    @Test
    public void testWithdraw() throws Exception {

        assertFalse(atm.withdraw(55));  // Test that withdrawal has to be multiple of 10.
        assertFalse(atm.withdraw(24));
        assertFalse(atm.withdraw(10020)); // Test that cannot exceed ATM balance.

        assertTrue(atm.withdraw(500)); // Can withdraw valid amount
        assertEquals(9500, atm.getATMBalance());  // Balance has decreased by 500
        assertEquals(225, (int)(atm.getWithdrawlTray().get(20)));  // Number of twenties in withdrawal tray has decreased by 25

        assertTrue(atm.withdraw(10)); // (Balance == 9490)
        assertEquals(499, (int)(atm.getWithdrawlTray().get(10))); // Number of tens has decreased by 1

        assertTrue(atm.withdraw(4520));  // Withdraw all 20s and start using 10s (balance == 4970)
        assertEquals(0, (int)(atm.getWithdrawlTray().get(20)));
        assertEquals(497, (int)(atm.getWithdrawlTray().get(10)));

        assertTrue(atm.withdraw(4960)); // (Balance == 10)
        assertEquals(1, (int)(atm.getWithdrawlTray().get(10)));

        assertFalse(atm.withdraw(15));
        assertTrue(atm.withdraw(10));
    }

    @Test
    public void testReloadWithdrawalTray() throws Exception {

        // Reload ATM to original capacity
        assertTrue(atm.reloadWithdrawalTray(250, 500));
        assertEquals(250, (int)(atm.getWithdrawlTray().get(20)));
        assertEquals(500, (int)(atm.getWithdrawlTray().get(10)));

        // Test that reload cannot exceed ATM's max capacity
        assertFalse(atm.reloadWithdrawalTray(1200, 51));

    }

    @Test
    public void testDeposit() throws Exception {

        assertTrue(atm.deposit(25, 405));  // Total bills == 775 (including withdrawal trays)
        assertEquals(405, atm.getDepositValue());
        assertEquals(25, atm.getDepositTray());

        assertTrue(atm.deposit(500, 1000));  // Total bills == 1275 (including withdrawal trays)
        assertEquals(1405, atm.getDepositValue());
        assertEquals(525, atm.getDepositTray());

        assertFalse(atm.deposit(726, 10001));  // Would exceed bill limit
        assertEquals(1405, atm.getDepositValue());
        assertEquals(525, atm.getDepositTray());

        assertTrue(atm.deposit(725, 10000));  // Total bills == 2000 (limit)
        assertEquals(11405, atm.getDepositValue());
        assertEquals(1250, atm.getDepositTray());

        atm.emptyDepositTray();  // Test emptying the deposit tray
        assertEquals(0, atm.getDepositTray());
        assertEquals(0, atm.getDepositValue());

    }

}