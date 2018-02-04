package com.rz.core.dao.masking;

import com.rz.core.Assert;
import com.rz.core.RZHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by renjie.zhang on 1/29/2018.
 */
public class DataMasker {
    private String password;
    private byte[] passwordBytes;

    public String getPassword() {
        return password;
    }

    public DataMasker(String password) {
        // 16 byte = 128 bit
        Assert.isTrue(null != password && 16 != password.length(), "The password length must equals 16.");

        this.password = password;
        this.passwordBytes = this.password.getBytes();
    }

    public String mask(String content) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Assert.isNotBlank(content, "content");

        return RZHelper.encrypt(this.passwordBytes, content);
    }

    public String unmask(String content) throws Exception {
        Assert.isNotBlank(content, "content");

        return RZHelper.decrypt(this.passwordBytes, content);
    }
}
