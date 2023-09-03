package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AdminTest {

    @Test
    void getInstance() {
        Assertions.assertTrue(true);
    }

    @Test
    void isUniqueLogin() {
        User user=CRUDUtils.getUser(2);
        String login= user.getLogin();
        boolean result=false;
        Assertions.assertEquals(result, Admin.isUniqueLogin(login));
    }
}