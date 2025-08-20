// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



package frc.robot.subsystems;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.SingleFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CANdleSystem extends SubsystemBase {
    private final CANdle m_candle = new CANdle(1, "rio");
    private final int LedCount = 300;//设的比较小会导致灯带只走一半

    private Animation m_toAnimate = null;//下面要用的动画

    //是属性，因为不是构造函数也没有方法，没有参数也没有括号()
    public enum AnimationTypes { //类型叫AnimationTypes
                              //enum是枚举变量，类似于一个类
                               //枚举变量是一组常量，通常用于表示一组相关的常量
                                 //常量是指在程序中不会改变的值
                                 //e.g. 学生1，学生2，学生3等
        ColorFlow,//e.g.Type1
        Fire,//Type2
        Larson,//Type3
        Rainbow,
        RgbFade,
        SingleFade,
        Strobe,
        Twinkle,
        TwinkleOff,
        SetAll
    }
    private AnimationTypes m_currentAnimation;//当前所在的动画位置 当前的状态

    //构造函数
    public CANdleSystem() {
        //this.joystick = joy;
        changeAnimation(AnimationTypes.SetAll);
        CANdleConfiguration configAll = new CANdleConfiguration();//类型名字保持一致
        configAll.statusLedOffWhenActive = true;
        configAll.disableWhenLOS = false;
        configAll.stripType = LEDStripType.GRB;
        configAll.brightnessScalar = 0.1;
        configAll.vBatOutputMode = VBatOutputMode.Modulated;
        m_candle.configAllSettings(configAll, 100);
    }

    //不是构造函数：有void，名字跟类型不一样
    //方法

    //如果要增加一个状态，就在这里加一个case

    //假设我现在是状态1，我可以做一个int flag，假设flag=1我就在1
    //如果我想变成状态2，我就让flag=2


    public void incrementAnimation() {//状态增加，从状态1变换到状态2
        switch(m_currentAnimation) {//判断当前的状态 调用changeAnimation方法来改变状态
            //m_currentAnimation是当前的状态 已经在下方更改为下一步的状态
            case ColorFlow: changeAnimation(AnimationTypes.Fire); break;
            case Fire: changeAnimation(AnimationTypes.Larson); break;
            case Larson: changeAnimation(AnimationTypes.Rainbow); break;
            case Rainbow: changeAnimation(AnimationTypes.RgbFade); break;
            case RgbFade: changeAnimation(AnimationTypes.SingleFade); break;
            case SingleFade: changeAnimation(AnimationTypes.Strobe); break;
            case Strobe: changeAnimation(AnimationTypes.Twinkle); break;
            case Twinkle: changeAnimation(AnimationTypes.TwinkleOff); break;
            case TwinkleOff: changeAnimation(AnimationTypes.ColorFlow); break;
            case SetAll: changeAnimation(AnimationTypes.ColorFlow); break;
        }
    }
    public void decrementAnimation() {//状态减少，从状态2变换到状态1 //decrease跟increment也是一样的道理只是反过来
        switch(m_currentAnimation) {
            case ColorFlow: changeAnimation(AnimationTypes.TwinkleOff); break;
            case Fire: changeAnimation(AnimationTypes.ColorFlow); break;
            case Larson: changeAnimation(AnimationTypes.Fire); break;
            case Rainbow: changeAnimation(AnimationTypes.Larson); break;
            case RgbFade: changeAnimation(AnimationTypes.Rainbow); break;
            case SingleFade: changeAnimation(AnimationTypes.RgbFade); break;
            case Strobe: changeAnimation(AnimationTypes.SingleFade); break;
            case Twinkle: changeAnimation(AnimationTypes.Strobe); break;
            case TwinkleOff: changeAnimation(AnimationTypes.Twinkle); break;
            case SetAll: changeAnimation(AnimationTypes.ColorFlow); break;
        }
    }


    public void setColors() {
        changeAnimation(AnimationTypes.SetAll);
    }

    public void setOff() {
        m_candle.animate(null);
        m_candle.animate(null);
        m_candle.setLEDs(0, 0, 0);
        m_candle.setLEDs(0, 0, 0);
        changeAnimation(AnimationTypes.SetAll);

    }

    public void setFire() {
        m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
    }

    public void setColorFlow() {
        m_toAnimate = new ColorFlowAnimation(128, 20, 70, 0, 0.7, LedCount, Direction.Forward);
    }

    /* Wrappers so we can access the CANdle from the subsystem */
    public double getVbat() { return m_candle.getBusVoltage(); }
    public double get5V() { return m_candle.get5VRailVoltage(); }
    public double getCurrent() { return m_candle.getCurrent(); }
    public double getTemperature() { return m_candle.getTemperature(); }
    public void configBrightness(double percent) { m_candle.configBrightnessScalar(percent, 0); }
    public void configLos(boolean disableWhenLos) { m_candle.configLOSBehavior(disableWhenLos, 0); }
    public void configLedType(LEDStripType type) { m_candle.configLEDType(type, 0); }
    public void configStatusLedBehavior(boolean offWhenActive) { m_candle.configStatusLedState(offWhenActive, 0); }

    public void changeAnimation(AnimationTypes toChange) {//接收下一步toChange的参数
        m_currentAnimation = toChange;//更新自己下一步的状态 改到当前状态 //标志位只是在记录还没有改变灯带颜色（预备）
        
        switch(toChange)//改变tochange然后tochange会赋予到m_currentAnimation上
        //toChange表示当前的状态（标志位）不会实际更改
        {
            //这里才是在真正改变灯带颜色
            //如果是ColorFlow就改变灯带颜色为ColorFlow
            case ColorFlow:
                m_toAnimate = new ColorFlowAnimation(128, 20, 70, 0, 0.7, LedCount, Direction.Forward);
                break;
            case Fire:
                m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
                break;
            case Larson:
                m_toAnimate = new LarsonAnimation(0, 255, 46, 0, 1, LedCount, BounceMode.Front, 3);
                break;
            case Rainbow:
                m_toAnimate = new RainbowAnimation(1, 0.1, LedCount);
                break;
            case RgbFade:
                m_toAnimate = new RgbFadeAnimation(0.7, 0.4, LedCount);
                break;
            case SingleFade:
                m_toAnimate = new SingleFadeAnimation(50, 2, 200, 0, 0.5, LedCount);
                break;
            case Strobe:
                m_toAnimate = new StrobeAnimation(240, 10, 180, 0, 98.0 / 256.0, LedCount);
                break;
            case Twinkle:
                m_toAnimate = new TwinkleAnimation(30, 70, 60, 0, 0.4, LedCount, TwinklePercent.Percent6);
                break;
            case TwinkleOff:
                m_toAnimate = new TwinkleOffAnimation(70, 90, 175, 0, 0.8, LedCount, TwinkleOffPercent.Percent100);
                break;
            case SetAll:
                m_toAnimate = null;
                break;
        }
        System.out.println("Changed to " + m_currentAnimation.toString());
    }
    public Command changeAnimation1(AnimationTypes CandleLight) {
        return run(()->{
            changeAnimation(CandleLight); // Set the motor to move at 1000 units per second
        });
      }



    public Command setFireWithMotor() {

        return startEnd(
                () ->{
                    m_toAnimate = new FireAnimation(0.5, 0.7, LedCount, 0.7, 0.5);
                },
                () ->{
                    setOff();
                }

        );

    }

    

    //.animate(灯带对象)
    //如果想让一个炫酷灯带一直播放 不会切换
    //用periodic()方法来调用animate()方法
    //这样每20毫秒就会调用一次animate()方法
    //也就是每20毫秒就会更新一次炫酷灯带的状态 就能实现一直播放

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // if(m_toAnimate == null) {
        //     m_candle.setLEDs((int)(joystick.getLeftTriggerAxis() * 255), 
        //                       (int)(joystick.getRightTriggerAxis() * 255), 
        //                       (int)(joystick.getLeftX() * 255));
        // } else {
        m_candle.animate(m_toAnimate);
        // }
       // m_candle.modulateVBatOutput(joystick.getRightY());
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
