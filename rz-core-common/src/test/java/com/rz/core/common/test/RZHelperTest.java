package com.rz.core.common.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.rz.core.RZHelper;

public class RZHelperTest {
    public static void main(String[] args) {
        RZHelperTest rzHelperTest = new RZHelperTest();
        try {
            // rzHelperTest.test();
            rzHelperTest.test1();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End HelperTest...");
    }

    public void test() throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        String value = RZHelper.encrypt("asd");
        System.out.println(value);
        value = RZHelper.decrypt(value);
        System.out.println(value);
    }

    private void test1() {
        String value = "hashCode = hashCode * seed + (int) value.charAt(i);";
        System.out.println(RZHelper.getBKDRHashCode(value));
    }
}
