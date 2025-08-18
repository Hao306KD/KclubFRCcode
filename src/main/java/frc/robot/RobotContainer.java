// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.drive;
// import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger; //. = 下一级

//package = 功能包：负责某一类特定的功能
//import = 导入：把其他包的类导入进来


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  private final drive m_ExampleDriveSystem = new drive();
    //实体化子系统 + 赋值
    //相当于 car car1 = new car() 加上private实体化
    //命令1 命令2 命令3... 都是另一个子系统的指令
    //把所有命令都要转化到手柄上控制
    //假设不提前声明子系统无法直接调用命令
    //e.g.是subsystem类里面的指令才能被调用
    //跨文件调用
    //想要找到subsystem里面的指令要先找到subsystem （在这里是ExampleSubsystem


  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }
  //构造函数 执行configureBindings()方法

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
          //m_exampleSubsystem = 子系统(里面要用BooleanSupplier来储存多个boolean方法)
          //m_exampleSubsystem::exampleCondition = 子系统里的一个boolean方法
          //所以 ：： 是为了区分开他们让子系统来接收BooleanSupplier来执行指令

          //新按键,数值，调用子系统里的功能
        //这里的Trigger相当于一个被命名为“trigger”的一个开关 是否要出发里面的指令 真正的触发按键在下面".b"那里
          //m_exampleSubsystem::exampleCondition = 子系统里的方法
          //如果子系统里的方法返回true就会触发下面的指令
          //如果返回false就不会触发下面的指令
          //这里是一个lambda表达式，意思是当exampleCondition为true时触发下面的指令
          //ontrue = 按下时触发
          //whiletrye = 按下时持续触发
          //onFalse = 松开时触发
          //这里是在按键b松开时触发

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_ExampleDriveSystem.motorB_Voltage_Command(2));
    m_driverController.b().whileFalse(m_ExampleDriveSystem.motorB_Voltage_Command(0));

    m_driverController.a().onTrue(m_ExampleDriveSystem.motorA_Voltage_Command(2));
    m_driverController.x().onFalse(m_ExampleDriveSystem.motorA_Voltage_Command(0));
    //m_driverController.b().whileFalse(m_ExampleDriveSystem.motor_Voltage_Command(0));
    //xBoxController + 按键 = Trigger
  }
    //m_driverController.b() 运行上方new Trigger里的指令

  // /**
  //  * Use this to pass the autonomous command to the main {@link Robot} class.
  //  *
  //  * @return the command to run in autonomous
  //  */
  // public Command getAutonomousCommand() {
  //   // An example command will be run in autonomous
  //   return Autos.exampleAuto(m_exampleSubsystem);
}