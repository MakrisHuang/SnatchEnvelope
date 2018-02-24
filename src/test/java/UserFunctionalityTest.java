import com.makris.config.ApplicationConfiguration;
import com.makris.site.pojo.User;
import com.makris.site.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@SpringBootTest(classes = ApplicationConfiguration.class)
public class UserFunctionalityTest {
    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(UserFunctionalityTest.class);

    @Test
    public void insertUser(){
        User user = new User();
        user.setUsername("User");
        user.setPassword("password");
        user.setMoney(100);
        user.setRemainTimes(5);
        user.setEnvelops(null);
        System.out.println(user.getUsername());
        logger.debug("show user");
        logger.debug(user);
        int count = userService.insertUser(user);
        assertEquals(count, 1);
    }
}