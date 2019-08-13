package com.atguigu.es.demo;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {

    @Autowired
    JestClient jestClient;

    /**
     * 新增一个es
     * @throws IOException
     */
    @Test
    public void index() throws IOException {
        User user = new User("张三","136767373@qq.com",28);

        // 获取一个index的建造者 index索引名 type类型名
        Index.Builder builder = new Index.Builder(user)
                .index("user")
                .type("info")
                .id("1");
        // 构造出这个index的动作
        Index index = builder.build();

        // 执行这个动作
        DocumentResult result = jestClient.execute(index);

        // 打印结果
        System.out.println("打印结果为"+ result);
    }


    /**
     * 删除es数据
     */
    @Test
    public void delete() throws IOException {
        Delete.Builder builder = new Delete.Builder("1")
                .index("user")
                .type("info");

        Delete delete = builder.build();

        DocumentResult result = jestClient.execute(delete);

        System.out.println("删除:"+result);
    }

    /**
     * 测试修改es的数据;
     *      * 修改，我们自己手动把要修改的对象放在 doc里面；
     *      * ==用index少传的字段就没了
     *      * ==用update我们自己手动把要修改的对象放在 doc里面
     */
    @Test
    public void updateDate() throws IOException {
        User user = new User();
        user.setAge(5000);
        user.setEmail("zuihou098@qq.com");
        // 更新的时候放一个doc字段
        Map<String,User> u = new HashMap<>();
        u.put("doc",user);

        Update.Builder builder = new Update.Builder(u)
                .index("user")
                .type("info")
                .id("1");

        Update update = builder.build();
        DocumentResult result = jestClient.execute(update);

        System.out.println("修改:"+result);
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