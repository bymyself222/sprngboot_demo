package com.hls.logback.utils;

import cn.hutool.core.codec.BCD;
import com.fazecast.jSerialComm.SerialPort;
import com.hls.logback.config.SerialConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;  
import org.springframework.stereotype.Service;  
  
import java.io.InputStream;  
import java.util.Arrays;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
  
/**  
* 串口服务类  
* */  
@AllArgsConstructor  
@Slf4j  
@Service  
public class SerialService {  
  
/**  
* 获取串口及状态  
* */  
public Map<String, Boolean> getPortStatus(){  
		Map<String, Boolean> comStatusMap = new HashMap<>();  
		List<SerialPort> commPorts = Arrays.asList(SerialPort.getCommPorts());  
		commPorts.forEach(port->{  
		comStatusMap.put(port.getSystemPortName(), port.isOpen());  
	});  
  
	return comStatusMap;  
}  
  
/**  
* 添加串口连接  
* */  
public SerialPort connectSerialPort(String portName){
	SerialPort commPort = SerialPort.getCommPort(portName);  
	if (commPort.isOpen()){  
		throw new RuntimeException("该串口已被占用");  
	}  
	if (SerialConfig.portMap.containsKey(portName)){
		throw new RuntimeException("该串口已被占用");  
	}  
  
	// 打开端口  
	commPort.openPort();  
	if (!commPort.isOpen()){  
		throw new RuntimeException("打开串口失败");  
	}  
	// 设置串口参数 (波特率、数据位、停止位、校验模式、是否为Rs485)  
	commPort.setComPortParameters(SerialConfig.baudRate, SerialConfig.dataBits,SerialConfig.stopBits, SerialConfig.stopBits, SerialConfig.rs485Mode);  
	// 设置串口超时和模式  
	commPort.setComPortTimeouts(SerialConfig.messageModel ,SerialConfig.timeOut, SerialConfig.timeOut);  
	// 添加至串口记录Map  
	SerialConfig.portMap.put(portName, commPort);
	return commPort;
  
}  
  
/**  
* 关闭串口连接  
* */  
public boolean closeSerialPort(String portName){  
	if (!SerialConfig.portMap.containsKey(portName)){  
		throw new RuntimeException("该串口未启用");  
	}  
	// 获取串口  
	SerialPort port = SerialConfig.portMap.get(portName);  
	// 关闭串口  
	port.closePort();  
	// 需要等待一些时间，否则串口关闭不完全，会导致无法打开  
	try {  
		Thread.sleep(3000);  
	} catch (InterruptedException e) {  
		throw new RuntimeException(e);  
	}  
	if (port.isOpen()){  
		return false;  
	}else {  
		// 关闭成功返回  
		return true;  
	}  
  
  
}  
  
/**  
* 串口发送数据  
* */  
public void sendComData(String portName, byte[]sendBytes){  
	if (!SerialConfig.portMap.containsKey(portName)){  
		throw new RuntimeException("该串口未启用");  
	}  
	// 获取串口  
	SerialPort port = SerialConfig.portMap.get(portName);  
	// 发送串口数据  
	int i = port.writeBytes(sendBytes, sendBytes.length);  
	if (i == -1){  
		log.error("发送串口数据失败{}, 数据内容{}",portName, BCD.bcdToStr(sendBytes));  
		throw new RuntimeException("发送串口数据失败");  
	}  
}  
  
/**  
* 串口读取数据  
* */  
public byte[] readComData(String portName){  
	if (!SerialConfig.portMap.containsKey(portName)){  
		throw new RuntimeException("该串口未启用");  
	}  
	// 获取串口  
	SerialPort port = SerialConfig.portMap.get(portName);   
	// 读取串口流  
	InputStream inputStream = port.getInputStream();  
 
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
	return readByte;  
}  
  
}
