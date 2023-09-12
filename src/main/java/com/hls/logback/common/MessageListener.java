package com.hls.logback.common;

import cn.hutool.core.codec.BCD;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class MessageListener implements SerialPortDataListener {
    /**
     * 监听事件设置
     */
    @Override
    public int getListeningEvents() {
        // 持续返回数据流模式
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        // 收到数据立即返回
        // return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    /**
     * 收到数据监听回调
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        // 因为是阻塞是监听线程，所以使用线程处理
        Thread thread = new Thread(() -> {
            // 读取串口流
            InputStream inputStream = event.getSerialPort().getInputStream();

            // 获取串口返回的流大小
            int availableBytes = 0;
            try {
                availableBytes = inputStream.available();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 读取指定的范围的数据流
            byte[] readByte = new byte[availableBytes];
            int bytesRead = 0;
            try {
                bytesRead = inputStream.read(readByte);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭串口流失败" + e.getMessage());
            }
            // 有数据才执行
            if (readByte.length > 1) {
                try {
					log.info("收到串口数据: {}", new String(readByte));
//                    log.info("收到串口数据: {}", BCD.bcdToStr(readByte));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 开启线程
        thread.start();
    }

}
