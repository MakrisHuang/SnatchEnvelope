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
public class UserTest {
    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(UserTest.class);

    @Test
    public void insertUser(){
        User user = new User();
        user.setUsername("User");
        user.setPassword("password");
        user.setMoney(100);
        user.setRemainTimes(5);
        int count = userService.insertUser(user);
        assertEquals(count, 1);
    }

    @Test
    public void findUserByName(){
        User user = new User();
        user.setUsername("User2");
        user.setPassword("password");
        user.setMoney(50);
        user.setRemainTimes(10);
        int result = userService.insertUser(user);
        User user2 = userService.findUserByName("User2");
        assertEquals(user2.getUsername(), user.getUsername());
    }

    @Test
    public void deleteUser(){
        User user = new User();
        user.setUsername("User3");
        user.setPassword("password");
        user.setMoney(50);
        user.setRemainTimes(105);
        userService.insertUser(user);
        User user2 = userService.findUserByName(user.getUsername());
        int result = userService.deleteUser(user2.getId());
        assertEquals(result, 1);
        User user_deleted = userService.findUserByName(user.getUsername());
        assertEquals(user_deleted, null);
    }

    @Test
    public void updateUser(){
        User user = new User();
        user.setUsername("User4");
        user.setPassword("password");
        user.setMoney(50);
        user.setRemainTimes(105);
        userService.insertUser(user);

        user.setUsername("Updated User");
        userService.updateUser(user);
        User updatedUser = userService.findUserByName(user.getUsername());
        assertEquals(user.getUsername(), updatedUser.getUsername());

        userService.deleteUser(user.getId());
    }


}