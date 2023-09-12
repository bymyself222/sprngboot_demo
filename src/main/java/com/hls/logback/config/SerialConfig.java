package com.hls.logback.config;

import com.fazecast.jSerialComm.SerialPort;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 *  用于定义串口通用信息配置
 * */
@Configuration
public class SerialConfig {

    /**
     *  波特率
     * */
    public static int baudRate = 115200;


    /**
     * 数据位
     */
    public static int dataBits = 8;

    /**
     * 停止位 ( 1停止位 = 1  、 1.5停止位 = 2 、2停止位 = 3)
     * */
    public static int stopBits = 1;


    /**
     * 校验模式 ( 无校验 = 0  、奇校验 = 1 、偶校验 = 2、 标记校验 = 3、 空格校验 = 4  )
     * */
    public static int parity = 0;

    /**
     *  是否为 Rs485通信
     * */
    public static boolean rs485Mode = true;

    /**
     *  串口读写超时时间（毫秒）
     * */
    public static int timeOut = 300;

    /**
     * 消息模式
     * 非阻塞模式： #TIMEOUT_NONBLOCKING           【在该模式下，readBytes(byte[], long)和writeBytes(byte[], long)调用将立即返回任何可用数据。】
     * 写阻塞模式： #TIMEOUT_WRITE_BLOCKING        【在该模式下，writeBytes(byte[], long)调用将阻塞，直到所有数据字节都成功写入输出串口设备。】
     * 半阻塞读取模式： #TIMEOUT_READ_SEMI_BLOCKING 【在该模式下，readBytes(byte[], long)调用将阻塞，直到达到指定的超时时间或者至少可读取1个字节的数据。】
     * 全阻塞读取模式：#TIMEOUT_READ_BLOCKING       【在该模式下，readBytes(byte[], long)调用将阻塞，直到达到指定的超时时间或者可以返回请求的字节数。】
     * 扫描器模式：#TIMEOUT_SCANNER                【该模式适用于使用Java的java.util.Scanner类从串口进行读取，会忽略手动指定的超时值以确保与Java规范的兼容性】
     * */
    public static int messageModel = SerialPort.TIMEOUT_READ_BLOCKING;

    /**
     *  已打开的COM串口 (重复打开串口会导致后面打开的无法使用，所以打开一次就要记录到公共变量存储)
     * */
    public final static Map<String, SerialPort> portMap = new HashMap<>();


}

