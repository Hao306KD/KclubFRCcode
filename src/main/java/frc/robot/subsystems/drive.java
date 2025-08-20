// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//基类：基础的类型
//构造函数来初始化子系统 读取电机固定参数

public class drive extends SubsystemBase {

  // 控制
  //父子类
  private final TalonFX m_test_motor1 = new TalonFX(5, "rio");
 // private final TalonFX m_test_motor2 = new TalonFX(12, "rio");

  private final TalonFX m_test_motor3 = new TalonFX(3, "rio");
  private final TalonFX m_test_motor4 = new TalonFX(4, "rio");
  //控制请求
  private final MotionMagicVoltage m_test_motor_request = new MotionMagicVoltage(0.0);

  private final CANcoder m_motor_CANcoderFL = new CANcoder(3, "rio");

  double expected_position = 0.0; // 期望位置
  double Current_position = 0.0; // 当前实际位置
  double accepted_error = 0.1; // 允许的误差范围
  
  public boolean isAtPosition(){
    Current_position = m_test_motor1.getPosition().getValueAsDouble();

    return (Math.abs(Current_position - expected_position) <= accepted_error);
  }
  
  public Command cmd_motorCommand(double Position){
    return run(
      ()->{
      m_test_motor1.setControl(m_test_motor_request.withPosition(Position));
    })
    .until(() -> isAtPosition());
  }

  public double getMotorPosition() {
    // 获取当前电机位置
    return m_test_motor1.getPosition().getValueAsDouble();
  }


  //实际控制
  //封装出来的方法
  //控制电机 1. 位置
  //        2. 速度

  //newPosition能够将高级的控制请求和
  public void setmotorBPosition(double Position) {
    m_test_motor1.setControl(m_test_motor_request.withPosition(Position));
    // m_test_motor2.setControl(m_test_motor_request.withPosition(Position));
  }

  public void setmotorAPosition(double Position) {
    m_test_motor3.setControl(m_test_motor_request.withPosition(Position));
    m_test_motor4.setControl(m_test_motor_request.withPosition(Position));
  }

  //控制电压的command
  public Command motorB_Position_Command(double Position){
    return runOnce(()->{
      setmotorBPosition(Position); // Set the motor to move at 1000 units per second
    }
    );
  }
  public Command motorA_Position_Command(double Position){
    return runOnce(()->{
      setmotorAPosition(Position); // Set the motor to move at 1000 units per second
    }
    );
  }

  /** Creates a new ExampleSubsystem. */
  public drive() {

      var motorEncoderConfigs = new CANcoderConfiguration();
      motorEncoderConfigs.MagnetSensor.MagnetOffset=0.0;//offset
      motorEncoderConfigs.MagnetSensor.AbsoluteSensorDiscontinuityPoint=0.5;
      motorEncoderConfigs.MagnetSensor.SensorDirection=SensorDirectionValue.Clockwise_Positive;
      m_motor_CANcoderFL.getConfigurator().apply(motorEncoderConfigs); //电机头朝前=1 转360=2 带上cancoder电机转一圈还是1
 

      var motorConfigs = new TalonFXConfiguration();

      motorConfigs.Slot0.kS = 0.2;
      motorConfigs.Slot0.kV = 0.0;
      motorConfigs.Slot0.kA = 0;
      motorConfigs.Slot0.kP = 3;
      motorConfigs.Slot0.kI = 0;
      motorConfigs.Slot0.kD = 0;
      motorConfigs.Feedback.RotorToSensorRatio = 13;
      motorConfigs.MotionMagic.MotionMagicAcceleration = 100; // Acceleration is around 40 rps/s
      motorConfigs.MotionMagic.MotionMagicCruiseVelocity = 200; // Unlimited cruise velocity
      motorConfigs.MotionMagic.MotionMagicExpo_kV = 0.12; // kV is around 0.12 V/rps
      motorConfigs.MotionMagic.MotionMagicExpo_kA = 0.1; // Use a slower kA of 0.1 V/(rps/s)
      motorConfigs.MotionMagic.MotionMagicJerk = 0; // Jerk is around 0

      //canCoder参数获取和设置
      //我们用的是fused cancoder
      motorConfigs.Feedback.FeedbackRemoteSensorID = m_motor_CANcoderFL.getDeviceID(); //设置canCoder的ID
      motorConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
      

      //缺少cancoder和电机建立联系
      m_test_motor1.getConfigurator().apply(motorConfigs);
      //m_test_motor2.getConfigurator().apply(motorConfigs);
      m_test_motor3.getConfigurator().apply(motorConfigs);
      m_test_motor4.getConfigurator().apply(motorConfigs);
  }


}
//核心：拆分，把复杂的东西变简单

//subsystem：

//控制机器人
//把所有部分的控制全部写在一起

//子系统 主系统
//主系统 = 整个机器人 + 子系统

//地盘，升降，发射 = 子系统
//控制时把小的部分组合在一起就能控制机器人

//command：
//命令，指令：告诉机器人执行什么人物

//封装好了自己的方法
//command调用写好的方法实现机器人的控制

//简单的command完全类似于一个封装的结构
//command把东西都放进来然后直接运用到程序里 类似于俄罗斯套娃
//复杂的command = 多个简单的command组合起来

//frc变成不同于其他编程的写法
//i. 其他正常的循环 = for，while
//frc里面没有显示的for，while
//frc用的是periodic() 默认循环20毫秒/次
//for，while运行的时间不一定完全一样 受限于电脑和配置的当前运行情况
//periodic() = 20毫秒/次
//frc的控制是基于时间片段的
//. = 往下一级