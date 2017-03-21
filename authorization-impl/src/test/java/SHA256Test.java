import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * Created by yakov on 21.03.2017.
 */
public class SHA256Test {
    @Test
    public void digest() throws Exception {
//        String hashedPass=new String(MessageDigest.getInstance("SHA-256").digest("OpenSesame".getBytes("UTF-8")));
        String hashedPass= DigestUtils.sha256Hex("OpenSesame");
        System.out.println(hashedPass);
    }
}
