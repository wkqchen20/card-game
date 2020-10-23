# 卡片游戏

一个写着玩的项目，基于命令行玩牌类型的游戏，当前已支持斗地主。

感兴趣欢迎讨论，请不吝指导～

## Quick Start

### 技术栈

* [Netty](https://github.com/netty/netty)，用于 client 端和 server 的通信

* [状态机](https://github.com/hekailiang/squirrel)，server 持有 client 的状态，根据状态迁移，给 client 下发指令，指引 client 操作

* [picocli](https://github.com/remkop/picocli)，命令行界面参数传递

* ...

### 运行

1. 将项目 clone 下来

2. 进入项目根目录，执行 maven clean install

3. 运行 server 。 `java -jar game-server/target/game-server-1.0-SNAPSHOT.jar -p=8000`

4. 运行 client 。 `java -jar game-client/target/game-client-1.0-SNAPSHOT.jar -p=8000`


一个完整的 demo 如下：

![demo](/doc/landlords.gif)

## Module

* game-common

* game-landlords-common: 斗地主相关

* game-core: Player, Game 领域，以及其他一些关键信息

* game-landlords-core: 斗地主游戏相关指令和处理

* game-client: 传递玩家信息和展示服务端指令

* game-server: 接收和根据状态迁移下发新的指令给 client

![module](http://www.plantuml.com/plantuml/png/fP2n2WCX38Ptdq9kzmKwb8EktVe0HSwq71WjYTVdhnLgZdXR5-KZN_9hPYwUoeRHr6jz-ANOwUaHxajS8PN9UhPVlZGSf8oOaqWDbUIMJ21A1Ir4dqBC5DYfUTTVGGyU-mYEkiOi1de3TVVMq1Xp46UTE---TGhmWXJO31aYLX2jRwtNLO2e_IN_gVlukPuRnWy0)


## TODO

* 详细的文档说明
* client断开后的资源清理
* 测试用例
* client端提示优化
* ...




