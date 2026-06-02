package com.crems.quartz.task;

import org.springframework.stereotype.Component;
import com.crems.common.utils.StringUtils;

/**
 * 定时任务调度测试
 * 
 * @author crems
 */
@Component("cremsTask")
public class CremsTask
{
    public void cremsMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void cremsParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void cremsNoParams()
    {
        System.out.println("执行无参方法");
    }
}
