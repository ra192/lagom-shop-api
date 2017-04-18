import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by yakov on 21.03.2017.
 */
public class SHA256Test {
    @Test
    public void digest() throws Exception {
//        String hashedPass=new String(MessageDigest.getInstance("SHA-256").digest("OpenSesame".getBytes("UTF-8")));
//        https://omesti.bancore.com/getit/api/mobile/authorize.do?redirect_uri=https://google.com&response_type=token&merchant_id=11&client_id=10110&encKey=C85DFF8D8E37297A228CCFC258A71EE4A0B66977499271AB666AD00D0C4AE58F
        String[] params = {"11", "10110","token","https://google.com","F4B708D5F9AE01CEEEBBF9D6FE7993C5"};
        String hashedPass = DigestUtils.sha256Hex(Arrays.asList(params).stream().reduce(String::concat).get());

        System.out.println(hashedPass.toUpperCase());
    }
}
