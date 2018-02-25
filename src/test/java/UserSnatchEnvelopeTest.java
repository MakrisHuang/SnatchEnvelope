import com.makris.config.ApplicationConfiguration;
import com.makris.site.pojo.Envelope;
import com.makris.site.pojo.User;
import com.makris.site.service.EnvelopeService;
import com.makris.site.service.UserService;
import com.makris.site.service.UserSnatchEnvelopeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@SpringBootTest(classes = ApplicationConfiguration.class)
public class UserSnatchEnvelopeTest {
    @Autowired
    private UserSnatchEnvelopeService userSnatchEnvelopeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnvelopeService envelopeService;

    @Test
    public void insertUserSnatchEnvelope(){
        User user = new User();
        user.setUsername("User");
        user.setPassword("password");
        user.setRemainTimes(5);
        user.setMoney(100);
        userService.insertUser(user);
        long userId = userService.findUserByName("User").getId();

        Envelope envelope = new Envelope();
        envelope.setUserId(userId);
        envelope.setTotalPrice(200000D);
        envelope.setUnitPrice(10D);
        envelope.setTotalAmount(20000);
        envelope.setRemainAmount(20000);
        Date date = new Date();
        envelope.setSendDate(new Timestamp(date.getTime()));
        envelope.setVersion(1);
        envelopeService.insertEnvelope(envelope);
        Envelope insertedEnvelope = envelopeService.getEnvelopeByUserId(userId);

        userSnatchEnvelopeService.snatchEnvelope(insertedEnvelope.getId(), userId);

        // 總紅包數量會被減一
        Envelope decreasedEnvelope = envelopeService.getEnvelopeById(insertedEnvelope.getId());
        Integer remainingAmount = envelope.getRemainAmount() - 1;
        assertEquals(decreasedEnvelope.getRemainAmount(), remainingAmount);

        userService.deleteUser(userId);
    }
}
