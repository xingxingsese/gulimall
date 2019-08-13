package com.atguigu.gulimall.search;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Autowired
    JestClient jestClient;
    @Test
    public void contextLoads() throws IOException {

        User user  = new User("台风","taifeng@qq.com",5000);

        Index builder = new Index.Builder(user)
                .index("user")
                .type("info")
                .id("2")
                .build();

        jestClient.execute(builder);

        System.out.println("保存完成");
    }

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class User{
    private String username;
    private String email;
    private Integer age;
}
