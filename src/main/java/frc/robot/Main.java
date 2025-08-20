// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * Do NOT add any static variables to this class, or any initialization at all. Unless you know what
 * you are doing, do not modify this file except to change the parameter class to the startRobot
 * call.
 */
public final class Main {
  private Main() {}

  /**
   * Main initialization function. Do not perform any initialization here.
   *
   * <p>If you change your main robot class, change the parameter type.
   */
  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}


// git init // 如果需要新建一个 git 仓库
// git clone url // 克隆远端文件 url 远程仓库的地址
// git checkout branchName // 切换git分支，branchName 分支名称

// git pull // 假设你已经在现在的分支上修改了东西要提交，pull 拉下该分支最新的远程代码，一块提交，不然有可能会把别人已经推送到这个分支的东西搞没了（会被骂）
// git add filePath // 单个文件添加到暂存区， filePath 单个文件路径
// git add . // . 代表全部，全部添加到暂存区

// 1.6 git commit -m '描述内容' // 推送到本地仓库，并写上备注（改了啥东西）
// 1.7 git push origin branchName // branchName 远程分支名，推送到远程分支