@echo off
echo 正在启动JavaFX-SM应用...

REM 设置JavaFX模块路径 - 根据您的Maven仓库位置调整
set JAVAFX_PATH=%USERPROFILE%\.m2\repository\org\openjfx

REM 编译项目
call mvn clean compile

REM 使用javafx-maven-plugin运行应用
call mvn javafx:run

echo 应用已关闭。
pause 