import com.makris.config.ApplicationConfiguration;
import com.makris.site.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@MybatisTest
@SpringBootTest(classes = ApplicationConfiguration.class)
public class UserFunctionalityTest {
    @Autowired
    private UserService userService;

    @Test
    public void insertUser(){
//        User user = new User();
//        user.setUsername("User");
//        user.setPassword("password");
//        user.setMoney(100);
//        user.setRemainTimes(5);
//        user.setEnvelops(null);
//        int count = userService.insertUser(user);
//        assertEquals(count, 1);
    }
}
